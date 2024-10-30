package com.teamsync.backend.service;

import com.teamsync.backend.model.Epic;
import com.teamsync.backend.repository.EpicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EpicService {

    @Autowired
    private EpicRepository epicRepository;

    public Epic createEpic(Epic epic) {
        return epicRepository.save(epic);
    }

    public List<Epic> getEpicsByProject(Long projectId) {
        return epicRepository.findByProjectId(projectId);
    }
}
