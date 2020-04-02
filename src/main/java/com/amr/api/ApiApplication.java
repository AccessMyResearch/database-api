package com.amr.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@Slf4j
public class ApiApplication extends SpringBootServletInitializer {
    public static void main(String[] args){
//        log.info("AMR - API - SPRING BOOT APPLICATION ::: STARTING");
        SpringApplication.run(ApiApplication.class, args);
//        log.info("AMR - API - SPRING BOOT APPLICATION ::: STARTING");
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ApiApplication.class);
    }
}
