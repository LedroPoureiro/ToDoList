package com.projects.project2.services;

import com.projects.project2.model.Status;
import org.springframework.beans.factory.annotation.Autowired;
import com.projects.project2.model.Task;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.projects.project2.repositories.TaskRepo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    TaskRepo taskRepo;

    public List<Task> getTasks()
    {
        return taskRepo.findAll();
    }

    public Task getTaskByID(int taskID)
    {
        return taskRepo.findById(taskID).orElse(null);
    }

    public List<Task> getTasksByStatus(String statusStr)
    {
        Status status = Status.valueOf(statusStr.toUpperCase());
        return taskRepo.findByStatus(status);
    }

    public Task createTask(String content, String dueDate)
    {
        Task task = new Task();
        task.setContent(content);
        task.setDueDate(LocalDate.parse(dueDate));
        task.setStatus(Status.PENDING);
        return taskRepo.save(task);
    }

    public void deleteTask(int taskID)
    {
        taskRepo.deleteById(taskID);
    }

    public Optional<Task> updateTask(int taskID, String content, String dueDate, String status)
    {
        return taskRepo.findById(taskID)
                .map(task -> {
                    if(content != null){
                        task.setContent(content);
                    }
                    if(dueDate != null) {
                        task.setDueDate(LocalDate.parse(dueDate));
                    }
                    if(status != null) {
                        task.setStatus(Status.valueOf(status.toUpperCase()));
                    }
                    return taskRepo.save(task);
                });
    }
}
