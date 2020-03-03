package com.amr.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class ApiApplication {
    public static void main(String[] args){
//        log.info("AMR - API - SPRING BOOT APPLICATION ::: STARTING");
        SpringApplication.run(ApiApplication.class, args);
//        log.info("AMR - API - SPRING BOOT APPLICATION ::: STARTING");
    }
}
