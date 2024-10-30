package com.teamsync.backend.repository;

import com.teamsync.backend.model.Project;
import com.teamsync.backend.model.UserProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProjectRepository extends JpaRepository<UserProject, Long> {
    @Query(value = "select p from UserProject up inner join Project p on up.projectId = p.id where up.userId = ?1")
    List<Project> findProjectDetailsByUserId(Long userId);
}
