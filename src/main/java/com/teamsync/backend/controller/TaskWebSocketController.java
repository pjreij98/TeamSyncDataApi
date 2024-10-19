package com.teamsync.backend.controller;

import com.teamsync.backend.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class TaskWebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Broadcast task updates
    public void broadcastTaskUpdate(Task task) {
        messagingTemplate.convertAndSend("/topic/task-updates", task);
    }

    // Broadcast new tasks
    public void broadcastNewTask(Task task) {
        messagingTemplate.convertAndSend("/topic/new-task", task);
    }

    // Broadcast task deletions
    public void broadcastTaskDeletion(Long taskId) {
        messagingTemplate.convertAndSend("/topic/task-deleted", taskId);
    }
}
