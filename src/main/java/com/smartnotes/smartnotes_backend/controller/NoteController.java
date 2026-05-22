package com.smartnotes.smartnotes_backend.controller;


import com.smartnotes.smartnotes_backend.dto.NoteRequest;
import com.smartnotes.smartnotes_backend.dto.NoteResponse;
import com.smartnotes.smartnotes_backend.dto.TodoResponse;
import com.smartnotes.smartnotes_backend.entity.User;
import com.smartnotes.smartnotes_backend.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @PostMapping
    public ResponseEntity<NoteResponse> create(
            @RequestBody NoteRequest req,
            @AuthenticationPrincipal User user) {        // ← Spring injects the logged-in user
        return ResponseEntity.ok(noteService.create(req, user));
    }

    @GetMapping
    public ResponseEntity<List<NoteResponse>> getAll(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(noteService.getAll(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteResponse> getOne(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(noteService.getOne(id, user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteResponse> update(
            @PathVariable Long id,
            @RequestBody NoteRequest req,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(noteService.update(id, req, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        noteService.delete(id, user);
        return ResponseEntity.noContent().build();
    }

    // Get all todos linked to a specific note
    @GetMapping("/{id}/todos")
    public ResponseEntity<List<TodoResponse>> getTodosForNote(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(noteService.getTodosForNote(id, user));
    }
}