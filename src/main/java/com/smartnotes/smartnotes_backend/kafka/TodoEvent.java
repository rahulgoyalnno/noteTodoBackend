package com.smartnotes.smartnotes_backend.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodoEvent {
    private Long   todoId;
    private String title;
    private String userEmail;
    private String eventType;   // "TODO_DUE_SOON", "TODO_OVERDUE", "TODO_CREATED"
}
