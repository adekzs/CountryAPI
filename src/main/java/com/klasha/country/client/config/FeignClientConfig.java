package com.klasha.country.client.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.Logger;
import feign.RequestInterceptor;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import feign.hc5.ApacheHttp5Client;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.context.annotation.Bean;

@RequiredArgsConstructor
@Log4j2
public class FeignClientConfig {

    private final ObjectMapper objectMapper;

    @Bean
    public RequestInterceptor optimusTransferRequestInterceptor() {
        return new CountryRequestInterceptor();
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new MyErrorDecoder(objectMapper);
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        if (log.isDebugEnabled()) {
            return Logger.Level.FULL;
        }
        return Logger.Level.BASIC;
    }

    @Bean
    public CloseableHttpClient feignClient() {
        return HttpClients.createDefault();
    }

    @Bean
    public Feign.Builder feignBuilder() {
        return Feign.builder()
                .retryer(Retryer.NEVER_RETRY)
                .client(new ApacheHttp5Client());
    }


}
