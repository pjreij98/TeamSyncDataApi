package com.teamsync.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "epics")
@Data
public class Epic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    // Getters and Setters
}
