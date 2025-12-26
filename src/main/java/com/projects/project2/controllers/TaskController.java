package com.projects.project2.controllers;

import com.projects.project2.model.Task;
import com.projects.project2.model.dto.CompleteTaskDto;
import com.projects.project2.model.dto.TaskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.projects.project2.services.TaskService;

import java.util.List;
import java.util.Optional;

@RestController
public class TaskController {
    @Autowired
    TaskService taskService;

    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getTasks()
    {
        return new ResponseEntity<>(taskService.getTasks(), HttpStatus.OK);
    }

    @GetMapping("/tasks/{taskID}")
    public ResponseEntity<Task> getTaskById(@PathVariable int taskID)
    {
        Task task = taskService.getTaskByID(taskID);
        if(task != null)
        {
            return new ResponseEntity<>(task, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/tasks/status/{status}")
    public ResponseEntity<List<Task>> getTaskByStatus(@PathVariable String status)
    {
        return new ResponseEntity<>(taskService.getTasksByStatus(status), HttpStatus.OK);

    }



    @PostMapping("/tasks")
    public ResponseEntity<Task> createTask(@RequestBody TaskDto taskDto)
    {
        return new ResponseEntity<>(taskService.createTask(taskDto.content(), taskDto.dueDate()), HttpStatus.CREATED);
    }

    @DeleteMapping("/tasks/{taskID}")
    public ResponseEntity<Void> deleteTask(@PathVariable int taskID)
    {
        taskService.deleteTask(taskID);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/tasks/{taskID}")
    public ResponseEntity<Task> updateTask(@PathVariable int taskID, @RequestBody CompleteTaskDto taskDto)
    {
        System.out.println(taskDto.content());
        System.out.println(taskDto.dueDate());
        System.out.println(taskDto.status());

        Optional<Task> savedTask = taskService.updateTask(taskID, taskDto.content(), taskDto.dueDate(), taskDto.status());
        return savedTask.map( task -> new ResponseEntity<>(task, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }



}
