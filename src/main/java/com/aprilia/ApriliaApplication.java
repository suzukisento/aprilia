package com.aprilia;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.aprilia.mapper")
public class ApriliaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApriliaApplication.class, args);
    }

}
