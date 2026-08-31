package com.nicolasmartineli.taskflow_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TaskflowApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskflowApiApplication.class, args);
	}

}
