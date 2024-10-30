package com.teamsync.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "sprints")
@Data
public class Sprint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    private int totalTasks;
    private int completedTasks;

    // Getters and Setters
}
