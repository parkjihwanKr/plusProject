package com.pjh.plusproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@ConfigurationPropertiesScan
@SpringBootApplication(scanBasePackages = "com.pjh.plusproject")
public class PlusProjectApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(PlusProjectApplication.class, args);
    }

    // war 파일 배포를 위해 선언
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder){
        return builder.sources(PlusProjectApplication.class);
    }
}
