package com.vtears.grpcspring;

import com.vtears.grpcspring.annotations.GrpcService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Map;

@SpringBootApplication
public class GrpcSpringApplication {

    public static void main(String[] args) {
        // 启动SpringBoot web
        ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(GrpcSpringApplication.class, args);

        Map<String, Object> grpcServiceBeanMap =  configurableApplicationContext.getBeansWithAnnotation(GrpcService.class);
        GrpcLauncher grpcLauncher = configurableApplicationContext.getBean("GrpcLauncher",GrpcLauncher.class);
        grpcLauncher.grpcStart(grpcServiceBeanMap);
    }

}
