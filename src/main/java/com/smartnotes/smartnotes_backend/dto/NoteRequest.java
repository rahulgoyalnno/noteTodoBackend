package com.smartnotes.smartnotes_backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class NoteRequest {
    private String title;
    private String content;
    private List<String> tags;
}
