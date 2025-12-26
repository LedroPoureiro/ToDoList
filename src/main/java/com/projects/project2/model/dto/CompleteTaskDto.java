package com.projects.project2.model.dto;

public record CompleteTaskDto(
        String content,
        String dueDate,
        String status
) {
}
