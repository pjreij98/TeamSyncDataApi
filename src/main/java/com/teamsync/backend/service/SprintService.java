package com.teamsync.backend.service;

import com.teamsync.backend.model.Sprint;
import com.teamsync.backend.model.TaskStatus;
import com.teamsync.backend.repository.SprintRepository;
import com.teamsync.backend.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SprintService {

    @Autowired
    private SprintRepository sprintRepository;

    @Autowired
    private TaskRepository taskRepository;

    public Sprint createSprint(Sprint sprint) {
        return sprintRepository.save(sprint);
    }

    public List<Sprint> getSprintsByProject(Long projectId) {
        return sprintRepository.findByProjectId(projectId);
    }

    public List<Sprint> getSprintsAndReports(Long projectId) {
        List<Sprint> sprints = sprintRepository.findByProjectId(projectId);
        for (Sprint sprint : sprints) {
            sprint.setCompletedTasks(taskRepository.countBySprintIdAndStatus(sprint.getId(), TaskStatus.DONE));
            sprint.setTotalTasks(taskRepository.countBySprintId(sprint.getId()));
        }
        return sprints;
    }

    public Map<String, Object> getCumulativeFlowMetrics(Long projectId) {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalTasks", taskRepository.countByProjectId(projectId));
        metrics.put("completedTasks", taskRepository.countByProjectIdAndStatus(projectId, TaskStatus.DONE));
        metrics.put("inProgressTasks", taskRepository.countByProjectIdAndStatus(projectId, TaskStatus.IN_PROGRESS));
        return metrics;
    }

}

