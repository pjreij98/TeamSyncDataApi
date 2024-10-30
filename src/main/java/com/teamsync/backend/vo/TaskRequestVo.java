package com.teamsync.backend.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

// DTO
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequestVo {
    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotBlank(message = "Description is mandatory")
    private String description;

    @NotBlank(message = "Status is mandatory")
    private String status;

    @NotBlank(message = "Priority is mandatory")
    private String priority;

    @NotNull(message = "Deadline is mandatory")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime deadline;

    private Long projectId;

    private Long sprintId;

    private Long userId;
}