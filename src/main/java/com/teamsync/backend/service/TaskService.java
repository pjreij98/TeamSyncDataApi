package com.teamsync.backend.service;

import com.teamsync.backend.controller.TaskWebSocketController;
import com.teamsync.backend.model.Task;
import com.teamsync.backend.model.User;
import com.teamsync.backend.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskWebSocketController webSocketController;

    public List<Task> getTasksByUser(User user) {
        return taskRepository.findByUser(user);
    }

    public Task createTask(Task task) {
        Task savedTask = taskRepository.save(task);
        webSocketController.broadcastNewTask(savedTask);
        return savedTask;
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
}
