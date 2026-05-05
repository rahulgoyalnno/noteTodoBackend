package com.smartnotes.smartnotes_backend.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TodoEventConsumer {

    @KafkaListener(topics = "todo.events", groupId = "smartnotes-group")
    public void consume(TodoEvent event) {
        switch (event.getEventType()) {
            case "TODO_CREATED" ->
                    log.info("New todo created: '{}' for {}", event.getTitle(), event.getUserEmail());

            case "TODO_DUE_SOON" ->
                    log.warn("⚠️  Due soon: '{}' for {}", event.getTitle(), event.getUserEmail());

            case "TODO_OVERDUE" ->
                    log.error("🔴 OVERDUE: '{}' for {}", event.getTitle(), event.getUserEmail());
            // Phase 4: send email/push notification here
        }
    }
}
