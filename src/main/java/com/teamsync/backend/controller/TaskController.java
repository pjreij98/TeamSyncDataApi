package com.teamsync.backend.controller;

import com.teamsync.backend.model.Task;
import com.teamsync.backend.model.TaskStatus;
import com.teamsync.backend.model.User;
import com.teamsync.backend.repository.UserRepository;
import com.teamsync.backend.service.TaskService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserRepository userRepository;

    // DTO
    public static class TaskRequest {
        @NotBlank(message = "Title is mandatory")
        private String title;

        @NotBlank(message = "Description is mandatory")
        private String description;

        @NotBlank(message = "Status is mandatory")
        private String status;

        @NotBlank(message = "Priority is mandatory")
        private String priority;

        @NotNull(message = "Deadline is mandatory")
        private LocalDateTime deadline;
    }

    @GetMapping
    public ResponseEntity<?> getAllTasks(Authentication authentication) {
        String username = authentication.getName();
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (!userOpt.isPresent()) {
            return ResponseEntity.status(404).body("User not found");
        }
        List<Task> tasks = taskService.getTasksByUser(userOpt.get());
        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    public ResponseEntity<?> createTask(Authentication authentication, @Valid @RequestBody TaskRequest taskRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        String username = authentication.getName();
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }
        Task task = Task.builder()
                .title(taskRequest.title)
                .description(taskRequest.description)
                .status(TaskStatus.valueOf(taskRequest.status))
                .user(userOpt.get())
                .priority(taskRequest.priority)
                .deadline(taskRequest.deadline)
                .build();
        Task savedTask = taskService.createTask(task);
        return ResponseEntity.ok(savedTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(Authentication authentication, @PathVariable Long id, @Valid @RequestBody TaskRequest taskRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        Optional<Task> taskOpt = taskService.updateTask(id, Task.builder()
                .title(taskRequest.title)
                .description(taskRequest.description)
                .status(TaskStatus.valueOf(taskRequest.status))
                .priority(taskRequest.priority)
                .deadline(taskRequest.deadline)
                .build());
        if (taskOpt.isPresent()) {
            return ResponseEntity.ok(taskOpt.get());
        }
        return ResponseEntity.status(404).body("Task not found");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(Authentication authentication, @PathVariable Long id) {
        boolean deleted = taskService.deleteTask(id);
        if (deleted) {
            return ResponseEntity.ok("Task deleted successfully");
        }
        return ResponseEntity.status(404).body("Task not found");
    }
}

