package com.smartnotes.smartnotes_backend.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class NoteResponse {
    private Long id;
    private String title;
    private String content;
    private List<String> tags;
    private String aiSummary;
    private String aiFollowUp;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // Notice: no user field — never leak user data back in a list response
}