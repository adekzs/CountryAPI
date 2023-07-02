package com.klasha.country.client.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class CountryRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        log.info("Path is: "+ requestTemplate.path());
    }
}
