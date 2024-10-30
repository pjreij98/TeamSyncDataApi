package com.teamsync.backend.controller;

import com.teamsync.backend.model.Project;
import com.teamsync.backend.model.User;
import com.teamsync.backend.model.UserProject;
import com.teamsync.backend.repository.ProjectRepository;
import com.teamsync.backend.repository.UserProjectRepository;
import com.teamsync.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProjectRepository userProjectRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @GetMapping("/{userId}/projects")
    public ResponseEntity<?> getUserProjects(@PathVariable Long userId) {
        List<Project> userProject = userProjectRepository.findProjectDetailsByUserId(userId);
        if (userProject != null) {
            return ResponseEntity.ok(userProject);
        }
        return ResponseEntity.status(404).body("User not found");
    }

    @PutMapping("/{userId}/active-project/{projectId}")
    public ResponseEntity<?> setActiveProject(@PathVariable Long userId, @PathVariable Long projectId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Project> projectOpt = projectRepository.findById(projectId);

        if (userOpt.isEmpty() || projectOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User or Project not found");
        }

        User user = userOpt.get();
        user.setActiveProjectId(projectId);
        userRepository.save(user);

        return ResponseEntity.ok("Active project set successfully");
    }

}
