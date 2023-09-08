package com.example.atipieraproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(MongoReactiveAutoConfiguration.class)
public class AtipieraProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(AtipieraProjectApplication.class, args);
    }

}
