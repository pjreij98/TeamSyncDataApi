package com.teamsync.backend.controller;

import com.teamsync.backend.model.Epic;
import com.teamsync.backend.service.EpicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/epics")
public class EpicController {

    @Autowired
    private EpicService epicService;

    @PostMapping
    public ResponseEntity<Epic> createEpic(@RequestBody Epic epic) {
        return ResponseEntity.ok(epicService.createEpic(epic));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Epic>> getEpicsByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(epicService.getEpicsByProject(projectId));
    }
}
