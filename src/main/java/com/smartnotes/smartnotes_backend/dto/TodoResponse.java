package com.smartnotes.smartnotes_backend.dto;

import com.smartnotes.smartnotes_backend.entity.Todo.Priority;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TodoResponse {
    private Long          id;
    private String        title;
    private String        description;
    private boolean       completed;
    private Priority      priority;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private boolean       overdue;       // computed field — not stored in DB
    private Long          noteId;        // which note this belongs to (if any)
}