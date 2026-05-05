package com.smartnotes.smartnotes_backend.kafka;


import com.smartnotes.smartnotes_backend.repository.TodoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class TodoDeadlineScheduler {

    private final TodoRepository               todoRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    // runs every 60 seconds
    @Scheduled(fixedRate = 60_000)
    @Transactional
    public void checkDeadlines() {
        LocalDateTime now  = LocalDateTime.now();
        LocalDateTime soon = now.plusHours(1);

        // fire event for each overdue todo
        todoRepository.findOverdueTodos(now).forEach(todo -> {
            TodoEvent event = new TodoEvent(
                    todo.getId(),
                    todo.getTitle(),
                    todo.getUser().getEmail(),
                    "TODO_OVERDUE"
            );
            kafkaTemplate.send("todo.events",
                    todo.getId().toString(), event);
            log.info("Overdue event fired for todo: '{}'", todo.getTitle());
        });

        // fire event for todos due within the hour
        todoRepository.findUpcomingTodos(now, soon).forEach(todo -> {
            TodoEvent event = new TodoEvent(
                    todo.getId(),
                    todo.getTitle(),
                    todo.getUser().getEmail(),
                    "TODO_DUE_SOON"
            );
            kafkaTemplate.send("todo.events",
                    todo.getId().toString(), event);
            log.info("Due soon event fired for todo: '{}'", todo.getTitle());
        });
    }
}