package com.pjh.plusproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@ConfigurationPropertiesScan
@SpringBootApplication(scanBasePackages = "com.pjh.plusproject")
public class PlusProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlusProjectApplication.class, args);
    }

}
