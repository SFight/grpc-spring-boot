package com.vtears.grpcspring;

import com.vtears.grpcspring.annotations.GrpcService;
import com.vtears.grpcspring.properties.GrpcServerProperties;
import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Configuration
@EnableConfigurationProperties(value = {GrpcServerProperties.class})
public class GrpcServerRunner implements CommandLineRunner, DisposableBean {
//    private Logger log = LoggerFactory.getLogger(GrpcServerRunner.class);

    private final AbstractApplicationContext applicationContext;
    private final GrpcServerProperties grpcServerProperties;
    private Server server;

    @Autowired
    public GrpcServerRunner(AbstractApplicationContext applicationContext, GrpcServerProperties grpcServerProperties) {
        this.applicationContext = applicationContext;
        this.grpcServerProperties = grpcServerProperties;
    }

    @Override
    public void destroy() throws Exception {
        log.info("关闭 gRPC 服务端 ...");
        Optional.ofNullable(server).ifPresent(Server::shutdown);
        log.info("gRPC 服务端已经关闭");
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("开始创建 gRPC 服务 ...");

        final ServerBuilder<?> serverBuilder = ServerBuilder.forPort(grpcServerProperties.getPort());

        // 扫描出所有的服务实现类
        // 获取所有的类型为 BindableService 类的 bean 的名称，gRPC 的实现类均是 BindableService 的子类
        String[] beanNames = applicationContext.getBeanNamesForType(BindableService.class);
        for (String name : beanNames) {
            log.info(name);
        }

        // 获取所有使用了注解 @GrpcServer 的 bean 类
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(GrpcService.class);

        // 类型为 BindableService 且使用了 @GrpcServer 注解的 bean 类，才是一个有效的 gRPC 服务类
        // 因此继承实现了 BindableService 类但是没有使用 @GrpcServer 注解的类和使用了 @GrpcServer 注解的类但不是 BindableService 子类的 bean 都需要剔除掉
        List<String> beanNameList = new ArrayList<>();
        for (String name : beanNames) {
            if (beansWithAnnotation != null && beansWithAnnotation.containsKey(name)) {
                beanNameList.add(name);
            }
        }

        // 根据 beanName 获取这些 bean 实例，并注册服务
        for (String name : beanNameList) {
            BindableService bindableService = applicationContext.getBeanFactory().getBean(name, BindableService.class);
            serverBuilder.addService(bindableService);
            log.info("{} 服务已经注册", name);
        }

        // 启动服务类
        server = serverBuilder.build().start();
        log.info("gRPC 服务端已经启动, 端口号 : {}", grpcServerProperties.getPort());
        startDaemonAwaitThread();
    }

    private void startDaemonAwaitThread() {
        Thread awaitThread = new Thread() {
            @Override
            public void run() {
                try {
                    GrpcServerRunner.this.server.awaitTermination();
                } catch (InterruptedException e) {
                    log.error("启动 gRPC 服务出现异常", e);
                }
            }

        };
        awaitThread.setDaemon(false);
        awaitThread.start();
    }
}
