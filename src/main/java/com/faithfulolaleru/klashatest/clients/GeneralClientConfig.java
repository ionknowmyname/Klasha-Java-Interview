package com.faithfulolaleru.klashatest.clients;

import feign.okhttp.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeneralClientConfig {

    @Bean
    public OkHttpClient client() {
        return new OkHttpClient();
    }
}
