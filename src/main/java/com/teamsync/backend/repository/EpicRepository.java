package com.teamsync.backend.repository;


import com.teamsync.backend.model.Epic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EpicRepository extends JpaRepository<Epic, Long> {
    List<Epic> findByProjectId(Long projectId);
}
