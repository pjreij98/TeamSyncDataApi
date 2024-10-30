package com.teamsync.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_projects")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProject {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "project_id")
    private Long projectId;
}
