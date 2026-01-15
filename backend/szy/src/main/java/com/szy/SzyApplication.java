package com.szy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableTransactionManagement
@SpringBootApplication
@EnableScheduling
public class SzyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SzyApplication.class, args);
    }

}



