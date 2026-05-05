package com.smartnotes.smartnotes_backend.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NoteEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publish(NoteEvent event) {
        kafkaTemplate.send("note.events", event.getNoteId().toString(), event);
        log.info("Published event: {} for noteId: {}", event.getEventType(), event.getNoteId());
    }

    public void publishTodo(TodoEvent event) {
        kafkaTemplate.send("todo.events",
                event.getTodoId().toString(), event);
        log.info("Published todo event: {} for todoId: {}",
                event.getEventType(), event.getTodoId());
    }
}