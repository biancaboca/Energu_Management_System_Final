package com.monitoring.comunication.config;

import com.monitoring.comunication.Service.MessageConsumer;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class RabbitMQConfig {
    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();

        container.setConnectionFactory(connectionFactory);
        container.setMessageListener(listenerAdapter);

        container.setQueueNames("stringDevice");

        return container;
    }

    @Bean
    SimpleMessageListenerContainer container1(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();

        container.setConnectionFactory(connectionFactory);
        container.setMessageListener(listenerAdapter);

        container.setQueueNames("insertDevice");

        return container;
    }
    @Bean
    SimpleMessageListenerContainer container2(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();

        container.setConnectionFactory(connectionFactory);
        container.setMessageListener(listenerAdapter);

        container.setQueueNames("updateDevice");

        return container;
    }
    @Bean
    SimpleMessageListenerContainer container3(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();

        container.setConnectionFactory(connectionFactory);
        container.setMessageListener(listenerAdapter);

        container.setQueueNames("deleteDevice");

        return container;
    }

  @Bean
    MessageListenerAdapter listenerAdapter(MessageConsumer listener) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(listener);
        adapter.addQueueOrTagToMethodName("stringDevice", "onMessageReceived");
        adapter.addQueueOrTagToMethodName("insertDevice", "onMessageReceivedFromInsetDevice");
        adapter.addQueueOrTagToMethodName("updateDevice", "onMessageReceivedFromUpdateDevice");
        adapter.addQueueOrTagToMethodName("deleteDevice", "onMessageReceivedFromDeleteDevice");

        return adapter;
    }
}