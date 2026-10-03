package com.taskmanagement.infrastructure.api;

import com.taskmanagement.adapter.persistence.IOSaveToMemory;
import com.taskmanagement.application.control.TaskControl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "com.taskmanagement")
public class ApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

    @Bean
    IOSaveToMemory taskRepository() {
        return new IOSaveToMemory();
    }

    @Bean
    TaskControl taskControl(IOSaveToMemory repository) {
        return new TaskControl(null, repository, repository);
    }
}
