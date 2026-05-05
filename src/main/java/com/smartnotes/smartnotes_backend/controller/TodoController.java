package com.smartnotes.smartnotes_backend.controller;

import com.smartnotes.smartnotes_backend.dto.TodoRequest;
import com.smartnotes.smartnotes_backend.dto.TodoResponse;
import com.smartnotes.smartnotes_backend.entity.User;
import com.smartnotes.smartnotes_backend.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    public ResponseEntity<TodoResponse> create(
            @RequestBody TodoRequest req,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(todoService.create(req, user));
    }

    @GetMapping
    public ResponseEntity<List<TodoResponse>> getAll(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(todoService.getAll(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoResponse> update(
            @PathVariable Long id,
            @RequestBody TodoRequest req,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(todoService.update(id, req, user));
    }

    @PatchMapping("/{id}/toggle")     // PATCH for partial update (toggle only)
    public ResponseEntity<TodoResponse> toggle(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(todoService.toggle(id, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        todoService.delete(id, user);
        return ResponseEntity.noContent().build();
    }
}