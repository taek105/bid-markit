package com.capstone.bidmarkit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BidMarKitApplication {

    public static void main(String[] args) {
        SpringApplication.run(BidMarKitApplication.class, args);
    }

}
