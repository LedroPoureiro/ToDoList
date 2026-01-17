package com.projects.project2.model.dto;

public record TaskDto (
        String title,
        String description,
        String dueDate,
        Boolean completed
){
}
