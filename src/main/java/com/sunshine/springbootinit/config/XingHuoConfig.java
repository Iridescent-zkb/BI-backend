package com.sunshine.springbootinit.config;

import io.github.briqt.spark4j.SparkClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 *
 */
@Configuration
@ConfigurationProperties(prefix = "xunfei.client")
@Data
public class XingHuoConfig {
    private String appid = "a06dc8cd";
    private String apiSecret = "ZWJlN2M0Y2JjNmFiYWY2YzEwYzJmMzlj";
    private String apiKey = "f5a6a90275b2ce9d258f6d4daee3b04b";


    @Bean
    public SparkClient sparkClient() {
        SparkClient sparkClient = new SparkClient();
        sparkClient.apiKey = apiKey;
        sparkClient.apiSecret = apiSecret;
        sparkClient.appid = appid;
        return sparkClient;
    }
}

