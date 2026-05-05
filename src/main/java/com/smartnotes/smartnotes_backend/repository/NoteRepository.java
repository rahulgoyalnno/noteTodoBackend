package com.smartnotes.smartnotes_backend.repository;


import com.smartnotes.smartnotes_backend.entity.Note;
import com.smartnotes.smartnotes_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {

    // Spring Data reads the method name and writes the SQL
    List<Note> findByUserOrderByUpdatedAtDesc(User user);

    // Security check: find note only if it belongs to this user
    Optional<Note> findByIdAndUser(Long id, User user);

    // Search by title keyword
    List<Note> findByUserAndTitleContainingIgnoreCase(User user, String keyword);
}
