package com.smartnotes.smartnotes_backend.service;


import com.smartnotes.smartnotes_backend.dto.TodoRequest;
import com.smartnotes.smartnotes_backend.dto.TodoResponse;
import com.smartnotes.smartnotes_backend.entity.Todo;
import com.smartnotes.smartnotes_backend.entity.User;
import com.smartnotes.smartnotes_backend.kafka.NoteEventProducer;
import com.smartnotes.smartnotes_backend.kafka.TodoEvent;
import com.smartnotes.smartnotes_backend.repository.NoteRepository;
import com.smartnotes.smartnotes_backend.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final NoteRepository noteRepository;
    private final NoteEventProducer producer;          // reuse same producer

    public TodoResponse create(TodoRequest req, User user) {
        Todo todo = new Todo();
        todo.setTitle(req.getTitle());
        todo.setDescription(req.getDescription());
        todo.setPriority(req.getPriority() != null ? req.getPriority() : Todo.Priority.MEDIUM);
        todo.setDueDate(req.getDueDate());
        todo.setUser(user);

        // optionally link to a note
        if (req.getNoteId() != null) {
            noteRepository.findByIdAndUser(req.getNoteId(), user)
                    .ifPresent(todo::setNote);
        }

        Todo saved = todoRepository.save(todo);

        producer.publishTodo(new TodoEvent(
                saved.getId(), saved.getTitle(),
                user.getEmail(), "TODO_CREATED"
        ));

        return toResponse(saved);
    }

    public List<TodoResponse> getAll(User user) {
        return todoRepository
                .findByUserOrderByCompletedAscDueDateAsc(user)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public TodoResponse toggle(Long id, User user) {
        Todo todo = todoRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Todo not found"));
        todo.setCompleted(!todo.isCompleted());          // flip completed state
        return toResponse(todoRepository.save(todo));
    }

    public TodoResponse update(Long id, TodoRequest req, User user) {
        Todo todo = todoRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Todo not found"));
        todo.setTitle(req.getTitle());
        todo.setDescription(req.getDescription());
        todo.setPriority(req.getPriority());
        todo.setDueDate(req.getDueDate());
        return toResponse(todoRepository.save(todo));
    }

    public void delete(Long id, User user) {
        Todo todo = todoRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Todo not found"));
        todoRepository.delete(todo);
    }

    // computed toResponse — overdue is calculated here, not stored
    private TodoResponse toResponse(Todo todo) {
        TodoResponse res = new TodoResponse();
        res.setId(todo.getId());
        res.setTitle(todo.getTitle());
        res.setDescription(todo.getDescription());
        res.setCompleted(todo.isCompleted());
        res.setPriority(todo.getPriority());
        res.setDueDate(todo.getDueDate());
        res.setCreatedAt(todo.getCreatedAt());
        res.setNoteId(todo.getNote() != null ? todo.getNote().getId() : null);

        // compute overdue at response time
        res.setOverdue(
                todo.getDueDate() != null
                        && todo.getDueDate().isBefore(LocalDateTime.now())
                        && !todo.isCompleted()
        );

        return res;
    }
}
