package com.vtears.grpcspring;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component("GrpcLauncher")
public class GrpcLauncher {
    private Logger logger = LoggerFactory.getLogger(GrpcSpringApplication.class);


    /**
     * 定义Grpc Server
     */
    private Server server;


    @Value("${grpc.server.port1}")
    private Integer grpcServerPort;
    /**
     * GRPC 服务启动方法
     * @param grpcServiceBeanMap
     */
    public void grpcStart(Map<String, Object> grpcServiceBeanMap) {
        try{
            ServerBuilder serverBuilder = ServerBuilder.forPort(grpcServerPort);
            for (Object bean : grpcServiceBeanMap.values()){
                serverBuilder.addService((BindableService) bean);
                logger.info(bean.getClass().getSimpleName() + " is regist in Spring Boot");
            }
            server = serverBuilder.build().start();
            logger.info("grpc server is started at " +  grpcServerPort);
            server.awaitTermination();
            Runtime.getRuntime().addShutdownHook(new Thread(()->{grpcStop();}));
        } catch (IOException e){
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * GRPC 服务Stop方法
     */
    private void grpcStop(){
        if (server != null){
            server.shutdownNow();
        }
    }
}
