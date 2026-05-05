package com.smartnotes.smartnotes_backend.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic noteEventsTopic() {
        return new NewTopic("note.events", 1, (short) 1);
    }

    @Bean
    public NewTopic todoEventsTopic() {
        return new NewTopic("todo.events", 1, (short) 1);
    }
}