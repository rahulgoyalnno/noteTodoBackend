package com.smartnotes.smartnotes_backend.repository;


import com.smartnotes.smartnotes_backend.entity.Note;
import com.smartnotes.smartnotes_backend.entity.Todo;
import com.smartnotes.smartnotes_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    // all todos for a user, incomplete first, then by due date
    List<Todo> findByUserOrderByCompletedAscDueDateAsc(User user);

    // security check — only return if owned by this user
    Optional<Todo> findByIdAndUser(Long id, User user);

    // Kafka scheduler uses this — find all overdue incomplete todos
    @Query("SELECT t FROM Todo t JOIN FETCH t.user WHERE t.dueDate < :now AND t.completed = false")
    List<Todo> findOverdueTodos(LocalDateTime now);

    // find todos due within the next hour (for reminders)
    @Query("SELECT t FROM Todo t JOIN FETCH t.user WHERE t.dueDate BETWEEN :now AND :soon AND t.completed = false")
    List<Todo> findUpcomingTodos(LocalDateTime now, LocalDateTime soon);

    List<Todo> findByNoteAndUser(Note note, User user);
}