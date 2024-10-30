package com.teamsync.backend.controller;

import com.teamsync.backend.model.Task;
import com.teamsync.backend.model.TaskStatus;
import com.teamsync.backend.model.User;
import com.teamsync.backend.repository.UserRepository;
import com.teamsync.backend.service.TaskService;
import com.teamsync.backend.vo.TaskRequestVo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Task>> getBacklogTasks(@PathVariable Long projectId) {
        return ResponseEntity.ok(taskService.getBacklogTasks(projectId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Task>> getTasksByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(taskService.getTasksByUser(userId));
    }

    @PostMapping
    public ResponseEntity<?> createTask(Authentication authentication, @Valid @RequestBody TaskRequestVo taskRequest,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        String username = authentication.getName();
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }
        Task task = Task.builder()
                .title(taskRequest.getTitle())
                .description(taskRequest.getDescription())
                .status(TaskStatus.valueOf(taskRequest.getStatus()))
                .userId(userOpt.get().getId())
                .priority(taskRequest.getPriority())
                .deadline(taskRequest.getDeadline())
                .build();
        Task savedTask = taskService.createTask(task);
        return ResponseEntity.ok(savedTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(Authentication authentication, @PathVariable Long id, @Valid @RequestBody TaskRequestVo taskRequest, BindingResult bindingResult) {
        Optional<User> userOpt = userRepository.findByUsername(authentication.getName());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        Optional<Task> taskOpt = taskService.updateTask(id, Task.builder()
                .title(taskRequest.getTitle())
                .description(taskRequest.getDescription())
                .status(TaskStatus.valueOf(taskRequest.getStatus()))
                .priority(taskRequest.getPriority())
                .deadline(taskRequest.getDeadline())
                .build());
        if (taskOpt.isPresent()) {
            return ResponseEntity.ok(taskOpt.get());
        }
        return ResponseEntity.status(404).body("Task not found");
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteTask(Authentication authentication, @PathVariable Long taskId) {
        Optional<User> userOpt = userRepository.findByUsername(authentication.getName());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }
        boolean deleted = taskService.deleteTask(taskId);
        if (deleted) {
            return ResponseEntity.ok("Task deleted successfully");
        }
        return ResponseEntity.status(404).body("Task not found");
    }

    @GetMapping("/board/{projectId}")
    public ResponseEntity<Map<String, List<Task>>> getBoardTasks(@PathVariable Long projectId) {
        List<Task> toDoTasks = taskService.getTasksByStatus(projectId, TaskStatus.TO_DO);
        List<Task> inProgressTasks = taskService.getTasksByStatus(projectId, TaskStatus.IN_PROGRESS);
        List<Task> doneTasks = taskService.getTasksByStatus(projectId, TaskStatus.DONE);

        Map<String, List<Task>> boardTasks = new HashMap<>();
        boardTasks.put("toDo", toDoTasks);
        boardTasks.put("inProgress", inProgressTasks);
        boardTasks.put("done", doneTasks);

        return ResponseEntity.ok(boardTasks);
    }

}

