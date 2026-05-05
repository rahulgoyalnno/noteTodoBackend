package com.smartnotes.smartnotes_backend.dto;

import com.smartnotes.smartnotes_backend.entity.Todo.Priority;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TodoRequest {
    private String        title;
    private String        description;
    private Priority priority;      // LOW, MEDIUM, HIGH
    private LocalDateTime dueDate;
    private Long          noteId;        // optional — link to a note
}
