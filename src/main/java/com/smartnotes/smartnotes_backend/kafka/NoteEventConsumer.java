package com.smartnotes.smartnotes_backend.kafka;

import com.smartnotes.smartnotes_backend.repository.NoteRepository;
import com.smartnotes.smartnotes_backend.service.AiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NoteEventConsumer {

    private final AiService aiService;
    private final NoteRepository noteRepository;

    @KafkaListener(topics = "note.events", groupId = "smartnotes-group")
    public void consume(NoteEvent event) {
        log.info("Processing AI for note: '{}'", event.getTitle());
        // Skip if content is too short to be worth analysing
        if (event.getContent() == null || event.getContent().length() < 20) {
            log.info("Note too short for AI analysis, skipping.");
            return;
        }

        // Call Ollama — runs in Kafka consumer thread, not the HTTP request thread
        String summary    = aiService.summarise(event.getTitle(), event.getContent());
        String followUp   = aiService.suggestFollowUp(event.getTitle(), event.getContent());

        // Save AI results back to the note
        noteRepository.findById(event.getNoteId()).ifPresent(note -> {
            if (summary  != null) note.setAiSummary(summary);
            if (followUp != null) note.setAiFollowUp(followUp);
            noteRepository.save(note);
            log.info("AI enrichment saved for noteId: {}", event.getNoteId());
        });
    }
}