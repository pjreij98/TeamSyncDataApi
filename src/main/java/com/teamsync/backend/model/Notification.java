package com.teamsync.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "notifications")
@Data
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;
    private boolean isRead;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    // Getters and Setters
}
