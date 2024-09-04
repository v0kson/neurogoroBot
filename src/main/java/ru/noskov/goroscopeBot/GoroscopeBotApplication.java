package ru.noskov.goroscopeBot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
public class GoroscopeBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoroscopeBotApplication.class, args);
	}

}
