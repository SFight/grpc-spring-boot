package com.vtears.grpcspring.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;



@PropertySource(value = "classpath:application.properties") // 设置配置文件,  value 接收 String[] 类型的值, 多个路径 value = {"classpath:xxx", "classpath:xxx"} 的格式设置
@ConfigurationProperties(prefix="grpc.server") /** 属性值匹配的前缀 */
//@Data
public class GrpcServerProperties {
    private int port;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
