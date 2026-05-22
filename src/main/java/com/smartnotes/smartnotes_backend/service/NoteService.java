package com.smartnotes.smartnotes_backend.service;

import com.smartnotes.smartnotes_backend.dto.NoteRequest;
import com.smartnotes.smartnotes_backend.dto.NoteResponse;
import com.smartnotes.smartnotes_backend.dto.TodoResponse;
import com.smartnotes.smartnotes_backend.entity.Note;
import com.smartnotes.smartnotes_backend.entity.Todo;
import com.smartnotes.smartnotes_backend.entity.User;
import com.smartnotes.smartnotes_backend.kafka.NoteEvent;
import com.smartnotes.smartnotes_backend.kafka.NoteEventProducer;
import com.smartnotes.smartnotes_backend.repository.NoteRepository;
import com.smartnotes.smartnotes_backend.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final NoteEventProducer noteEventProducer;   // ← add this
    private final TodoRepository todoRepository;


    public NoteResponse create(NoteRequest req, User user) {
        Note note = new Note();
        note.setTitle(req.getTitle());
        note.setContent(req.getContent());
        note.setTags(req.getTags());
        note.setUser(user);                      // tie note to the logged-in user
        Note saved = noteRepository.save(note);
        // Fire Kafka event — AI will process this asynchronously
        noteEventProducer.publish(new NoteEvent(
                saved.getId(),
                saved.getTitle(),
                saved.getContent(),
                user.getEmail(),
                "NOTE_CREATED"
        ));
        return toResponse(saved);
    }

    public List<NoteResponse> getAll(User user) {
        return noteRepository.findByUserOrderByUpdatedAtDesc(user)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public NoteResponse getOne(Long id, User user) {
        Note note = noteRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Note not found"));
        return toResponse(note);
    }

    public NoteResponse update(Long id, NoteRequest req, User user) {
        Note note = noteRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Note not found"));
        note.setTitle(req.getTitle());
        note.setContent(req.getContent());
        note.setTags(req.getTags());
        Note saved = noteRepository.save(note);

        noteEventProducer.publish(new NoteEvent(
                saved.getId(), saved.getTitle(),
                saved.getContent(), user.getEmail(), "NOTE_UPDATED"
        ));

        return toResponse(saved);
    }

    public void delete(Long id, User user) {
        Note note = noteRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Note not found"));
        noteRepository.delete(note);
    }

    private NoteResponse toResponse(Note note) {
        NoteResponse res = new NoteResponse();
        res.setId(note.getId());
        res.setTitle(note.getTitle());
        res.setContent(note.getContent());
        res.setTags(note.getTags());
        res.setAiSummary(note.getAiSummary());
        res.setAiFollowUp(note.getAiFollowUp());
        res.setCreatedAt(note.getCreatedAt());
        res.setUpdatedAt(note.getUpdatedAt());
        return res;
    }

    // Add method
    public List<TodoResponse> getTodosForNote(Long noteId, User user) {
        Note note = noteRepository.findByIdAndUser(noteId, user)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        return todoRepository.findByNoteAndUser(note, user)
                .stream()
                .map(this::todoToResponse)
                .toList();
    }

    // Add this private helper (converts Todo → TodoResponse)
    private TodoResponse todoToResponse(Todo todo) {
        TodoResponse res = new TodoResponse();
        res.setId(todo.getId());
        res.setTitle(todo.getTitle());
        res.setDescription(todo.getDescription());
        res.setCompleted(todo.isCompleted());
        res.setPriority(todo.getPriority());
        res.setDueDate(todo.getDueDate());
        res.setCreatedAt(todo.getCreatedAt());
        res.setNoteId(todo.getNote() != null ? todo.getNote().getId() : null);
        res.setOverdue(
                todo.getDueDate() != null
                        && todo.getDueDate().isBefore(java.time.LocalDateTime.now())
                        && !todo.isCompleted()
        );
        return res;
    }
}
