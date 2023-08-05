package com.kumofactory.cloud.global.config;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
		public static final String topicExchangeName = "aws.cdk";
		private final String queueName = "kumofactory-queue";

		@Bean
		public Queue queue() {
				return new Queue(queueName, false);
		}

		@Bean
		DirectExchange exchange() {
				return new DirectExchange("direct");
		}

		@Bean
		TopicExchange exchange2() {
				return new TopicExchange(topicExchangeName);
		}

		@Bean
		Binding binding(Queue queue, TopicExchange exchange) {
				return BindingBuilder.bind(queue).to(exchange).with("foo.bar");
		}

		@Bean
		public ConnectionFactory connectionFactory() {
				CachingConnectionFactory factory = new CachingConnectionFactory();
				factory.setHost("rabbitmq");
				factory.setUsername("guest");
				factory.setPassword("guest");
				return factory.getRabbitConnectionFactory();
		}

		@Bean
		public AmqpTemplate rabbitTemplate() {
				RabbitTemplate template = new RabbitTemplate((org.springframework.amqp.rabbit.connection.ConnectionFactory) connectionFactory());
				template.setRoutingKey(queueName);
				template.setMessageConverter(new Jackson2JsonMessageConverter());
				return template;
		}
}
