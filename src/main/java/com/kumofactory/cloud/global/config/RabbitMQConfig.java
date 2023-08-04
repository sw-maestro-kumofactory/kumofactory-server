package com.kumofactory.cloud.config;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
		private final String queueName = "aws.cdk.queue";

		@Bean
		public Queue queue() {
				return new Queue(queueName, false);
		}

		@Bean
		public ConnectionFactory connectionFactory() {
				CachingConnectionFactory factory = new CachingConnectionFactory();
				factory.setHost("localhost");
				factory.setUsername("guest");
				factory.setPassword("guest");
				return factory.getRabbitConnectionFactory();
		}

		@Bean
		public AmqpTemplate rabbitTemplate() {
				RabbitTemplate template = new RabbitTemplate((org.springframework.amqp.rabbit.connection.ConnectionFactory) connectionFactory());
				template.setRoutingKey(queueName);
				return template;
		}
}
