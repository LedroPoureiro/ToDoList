package com.projects.project2.controllers;

import com.projects.project2.model.Task;
import com.projects.project2.model.User;
import com.projects.project2.model.dto.ReturnTaskDto;
import com.projects.project2.model.dto.TaskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.projects.project2.services.TaskService;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class TaskController {
    @Autowired
    TaskService taskService;

    @GetMapping("/tasks")
    public ResponseEntity<List<ReturnTaskDto>> getTasks(@AuthenticationPrincipal User user)
    {
//        System.out.println(user.getUsername());
//        System.out.println(user.getId());
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

    @PostMapping("/tasks")
    public ResponseEntity<ReturnTaskDto> createTask(@RequestBody TaskDto taskDto, @AuthenticationPrincipal User user)
    {
        Task createdTask = taskService.createTask(taskDto.title(), taskDto.description(), taskDto.dueDate(), user);
        return new ResponseEntity<>(new ReturnTaskDto(createdTask.getId() ,createdTask.getTitle(),createdTask.getDescription(), createdTask.getDueDate(), createdTask.isCompleted()), HttpStatus.CREATED);
    }

    @DeleteMapping("/tasks/{taskID}")
    public ResponseEntity<Void> deleteTask(@PathVariable int taskID, @AuthenticationPrincipal User user)
    {
        taskService.deleteTask(taskID);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/tasks/{taskID}")
    public ResponseEntity<ReturnTaskDto> updateTask(@PathVariable int taskID, @RequestBody TaskDto taskDto, @AuthenticationPrincipal User user)
    {
        Optional<Task> savedTask = taskService.updateTask(taskID, taskDto.title(), taskDto.description(),taskDto.dueDate(), taskDto.completed());
        return savedTask.map( task -> new ResponseEntity<>(new ReturnTaskDto(task.getId() ,task.getTitle(),task.getDescription(), task.getDueDate(), task.isCompleted()), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
