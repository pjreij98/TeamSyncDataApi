package com.teamsync.backend.repository;

import com.teamsync.backend.model.Task;
import com.teamsync.backend.model.TaskStatus;
import com.teamsync.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserId(Long userId);
    List<Task> findByProjectId(Long projectId);
    List<Task> findBySprintId(Long sprintId); // Tasks in a sprint

    List<Task> findByProjectIdAndSprintIdIsNull(Long projectId); // Backlog

    List<Task> findByProjectIdAndStatus(Long projectId, TaskStatus status);

    Integer countByProjectId(Long projectId);

    Integer countByProjectIdAndStatus(Long projectId, TaskStatus taskStatus);

    Integer countBySprintIdAndStatus(Long id, TaskStatus taskStatus);

    Integer countBySprintId(Long id);
}
