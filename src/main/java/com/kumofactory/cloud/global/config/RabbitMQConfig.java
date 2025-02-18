package com.kumofactory.cloud.global.config;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String topicExchangeName = "aws.cdk";
    public static final String awsCdkResultQueue = "aws-cdk-result";
    private final String queueName = "kumofactory-queue";

    @Value("${rabbitmq.host}")
    private String host;

    @Value("${rabbitmq.user}")
    private String user;

    @Value("${rabbitmq.password}")
    private String password;

    @Bean
    public Queue queue() {
        return new Queue(queueName, false);
    }

    @Bean
    public Queue awsCdkResultQueue() {
        return new Queue(awsCdkResultQueue, false);
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
        factory.setHost(host);
        factory.setUsername(user);
        factory.setPassword(password);
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
