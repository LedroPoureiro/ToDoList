package com.projects.project2.model.dto;

import java.time.LocalDate;

public record ReturnTaskDto(
        int id,
        String title,
        String description,
        LocalDate dueDate,
        boolean completed
) {
}

