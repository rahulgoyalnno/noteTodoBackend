package com.smartnotes.smartnotes_backend.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteEvent {
    private Long   noteId;
    private String title;
    private String content;
    private String userEmail;
    private String eventType;   // "NOTE_CREATED" or "NOTE_UPDATED"
}