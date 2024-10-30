package com.teamsync.backend.controller;

import com.teamsync.backend.model.Sprint;
import com.teamsync.backend.service.SprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportsController {

    @Autowired
    private SprintService sprintService;

    @GetMapping("/sprint/{projectId}")
    public ResponseEntity<?> getSprintReport(@PathVariable Long projectId) {
        List<Sprint> sprints = sprintService.getSprintsAndReports(projectId);
        return ResponseEntity.ok(sprints);
    }

    // Example: Cumulative flow metrics
    @GetMapping("/cumulative-flow/{projectId}")
    public ResponseEntity<?> getCumulativeFlowReport(@PathVariable Long projectId) {
        Map<String, Object> reportData = sprintService.getCumulativeFlowMetrics(projectId);
        return ResponseEntity.ok(reportData);
    }
}
