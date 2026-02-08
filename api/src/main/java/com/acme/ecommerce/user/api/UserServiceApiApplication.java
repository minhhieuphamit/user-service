package com.acme.ecommerce.user.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.acme.ecommerce.user")
@EntityScan(basePackages = "com.acme.ecommerce.user.core.domain.entity")
@EnableJpaRepositories(basePackages = "com.acme.ecommerce.user.infra.repository")
public class UserServiceApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApiApplication.class, args);
    }
}
