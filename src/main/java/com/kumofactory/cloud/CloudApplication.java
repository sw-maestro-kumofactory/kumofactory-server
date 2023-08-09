package com.kumofactory.cloud;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.nio.charset.StandardCharsets;

@SpringBootApplication
public class CloudApplication {
		private final static String QUEUE_NAME = "hello";

		public static void main(String[] args) {
				SpringApplication.run(CloudApplication.class, args);
		}

}
