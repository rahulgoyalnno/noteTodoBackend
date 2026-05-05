package com.smartnotes.smartnotes_backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "todos")
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    private boolean completed = false;

    private LocalDateTime dueDate;        // Kafka scheduler will use this later

    @Enumerated(EnumType.STRING)          // stores "LOW"/"MEDIUM"/"HIGH" as text
    private Priority priority = Priority.MEDIUM;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)    // optionally link a todo to a note
    @JoinColumn(name = "note_id")
    private Note note;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public enum Priority { LOW, MEDIUM, HIGH }
}