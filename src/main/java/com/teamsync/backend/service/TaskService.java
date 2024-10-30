package com.teamsync.backend.service;

import com.teamsync.backend.controller.TaskWebSocketController;
import com.teamsync.backend.model.*;
import com.teamsync.backend.repository.ProjectRepository;
import com.teamsync.backend.repository.TaskRepository;
import com.teamsync.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskWebSocketController webSocketController;

    public List<Task> getTasksByUser(Long userId) {
        return taskRepository.findByUserId(userId);
    }

    public Task createTask(Task task) {
        Optional<Project> project = projectRepository.findById(task.getProjectId());
        if (project.isEmpty()) {
            throw new RuntimeException("Project not found");
        }

        return taskRepository.save(task);
    }

    public Optional<Task> updateTask(Long id, Task updatedTask) {
        Optional<Task> existingTaskOpt = taskRepository.findById(id);
        if (existingTaskOpt.isPresent()) {
            Task existingTask = existingTaskOpt.get();
            existingTask.setTitle(updatedTask.getTitle());
            existingTask.setDescription(updatedTask.getDescription());
            existingTask.setStatus(updatedTask.getStatus());
            existingTask.setPriority(updatedTask.getPriority());
            existingTask.setDeadline(updatedTask.getDeadline());
            Task savedTask = taskRepository.save(existingTask);
            webSocketController.broadcastTaskUpdate(savedTask);
            return Optional.of(savedTask);
        }
        return Optional.empty();
    }

    public boolean deleteTask(Long id) {
        Optional<Task> taskOpt = taskRepository.findById(id);
        if (taskOpt.isPresent()) {
            taskRepository.delete(taskOpt.get());
            webSocketController.broadcastTaskDeletion(id);
            return true;
        }
        return false;
    }

    // Fetch all tasks for the project that aren't assigned to a sprint (Backlog)
    public List<Task> getBacklogTasks(Long projectId) {
        return taskRepository.findByProjectIdAndSprintIdIsNull(projectId);
    }

    public List<Task> getTasksByStatus(Long projectId, TaskStatus status) {
        return taskRepository.findByProjectIdAndStatus(projectId, status);
    }

}
