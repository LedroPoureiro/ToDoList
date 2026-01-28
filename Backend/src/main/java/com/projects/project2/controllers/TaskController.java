package com.projects.project2.controllers;

import com.projects.project2.model.Task;
import com.projects.project2.model.User;
import com.projects.project2.model.dto.ReturnTaskDto;
import com.projects.project2.model.dto.TaskDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.projects.project2.services.TaskService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@Tag(name = "Tasks", description = "Task management APIs")
@SecurityRequirement(name = "bearerAuth")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService)
    {
        this.taskService = taskService;
    }


    @Operation(
            summary = "Get all tasks",
            description = "Retrieves all tasks for the authenticated user"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved tasks",
                    content = @Content(schema = @Schema(implementation = ReturnTaskDto.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or missing token",
                    content = @Content
            )
    })
    @GetMapping("/tasks")
    public ResponseEntity<List<ReturnTaskDto>> getTasks(@AuthenticationPrincipal User user)
    {
        List<Task> tasks = taskService.getTasks(user);
        return new ResponseEntity<>(
                tasks.stream()
                        .map(task -> new ReturnTaskDto(
                                task.getId(),
                                task.getTitle(),
                                task.getDescription(),
                                task.getDueDate(),
                                task.isCompleted()
                        ))
                        .toList(),
                HttpStatus.OK
        );
    }



    @Operation(
            summary = "Get task by its identifier",
            description = "Retrieves task with the provided id for the authenticated user"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieve task",
                    content = @Content(schema = @Schema(implementation = ReturnTaskDto.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or missing token",
                    content = @Content
            )
    })
    @GetMapping("/tasks/{taskID}")
    public ResponseEntity<ReturnTaskDto> getTaskById(@PathVariable int taskID, @AuthenticationPrincipal User user)
    {
        Task task = taskService.getTaskByID(taskID, user);
        if(task != null)
        {
            return new ResponseEntity<>(new ReturnTaskDto(task.getId() ,task.getTitle(), task.getDescription(), task.getDueDate(), task.isCompleted()), HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    @GetMapping("/tasks/status/{status}")
//    public ResponseEntity<List<Task>> getTaskByStatus(@PathVariable String status, @AuthenticationPrincipal User user)
//    {
//        List<Task> tasks = taskService.getTasksByStatus(status);
//        return new ResponseEntity<>(tasks, HttpStatus.OK);
//
//    }


    @Operation(
            summary = "Create a new task",
            description = "Creates a new  task for the authenticated user"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Successfully created task",
                    content = @Content(schema = @Schema(implementation = ReturnTaskDto.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or missing token",
                    content = @Content
            )
    })
    @PostMapping("/tasks")
    public ResponseEntity<ReturnTaskDto> createTask(@RequestBody TaskDto taskDto, @AuthenticationPrincipal User user)
    {
        Task createdTask = taskService.createTask(taskDto.title(), taskDto.description(), taskDto.dueDate(), user);
        return new ResponseEntity<>(new ReturnTaskDto(createdTask.getId() ,createdTask.getTitle(),createdTask.getDescription(), createdTask.getDueDate(), createdTask.isCompleted()), HttpStatus.CREATED);
    }



    @Operation(
            summary = "Delete a task",
            description = "Deletes a task by its identifier for the authenticated user"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Successfully deleted task"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or missing token"
            )
    })
    @DeleteMapping("/tasks/{taskID}")
    public ResponseEntity<Void> deleteTask(@PathVariable int taskID, @AuthenticationPrincipal User user)
    {
        boolean result = taskService.deleteTask(taskID, user);
        return result ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }



    @Operation(
            summary = "Update a task",
            description = "Updates a task by its identifier for the authenticated user: allowing the change of title, description, due date and completion state"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully updated task",
                    content = @Content(schema = @Schema(implementation = ReturnTaskDto.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or missing token",
                    content = @Content
            )
    })
    @PatchMapping("/tasks/{taskID}")
    public ResponseEntity<ReturnTaskDto> updateTask(@PathVariable int taskID, @RequestBody TaskDto taskDto, @AuthenticationPrincipal User user)
    {
        Optional<Task> savedTask = taskService.updateTask(taskID, taskDto.title(), taskDto.description(),taskDto.dueDate(), taskDto.completed(), user);
        return savedTask.map( task -> new ResponseEntity<>(new ReturnTaskDto(task.getId() ,task.getTitle(),task.getDescription(), task.getDueDate(), task.isCompleted()), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
