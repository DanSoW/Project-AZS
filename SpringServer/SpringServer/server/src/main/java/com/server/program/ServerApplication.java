package com.server.program;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

//***********************************************************
//Запуск серверной части приложения
//***********************************************************

@Configuration
@SpringBootApplication(scanBasePackages = "com.server")
public class ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

}
