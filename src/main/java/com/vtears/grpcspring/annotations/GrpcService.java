package com.vtears.grpcspring.annotations;

import io.grpc.ServerInterceptor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 自定义注解，用于获取Spring扫描到的类
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface GrpcService {
    Class<? extends ServerInterceptor>[] interceptors() default {};
    boolean applyGlobalInterceptors() default true;
}
