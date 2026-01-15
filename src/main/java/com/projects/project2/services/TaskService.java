package com.projects.project2.services;

import com.projects.project2.model.Status;
import com.projects.project2.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import com.projects.project2.model.Task;
import org.springframework.stereotype.Service;
import com.projects.project2.repositories.TaskRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepo;

    public List<Task> getTasks(User user)
    {
        return taskRepo.findByUser(user);
    }

    public Task getTaskByID(int taskID, User user)
    {
        return taskRepo.findByIdAndUserId(taskID, user.getId()).orElse(null);
    }

//    public List<Task> getTasksByStatus(String statusStr)
//    {
//        Status status = Status.valueOf(statusStr.toUpperCase());
//        return taskRepo.findByStatus(status);
//    }

    public Task createTask(String title, String description, String dueDate, User user)
    {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setDueDate(LocalDate.parse(dueDate));
        task.setCompleted(false);
        task.setUser(user);
        return taskRepo.save(task);
    }

    public void deleteTask(int taskID)
    {
        taskRepo.deleteById(taskID);
    }

    public Optional<Task> updateTask(int taskID, String title, String description, String dueDate, Boolean completed)
    {
        return taskRepo.findById(taskID)
                .map(task -> {
                    if(title != null){
                        task.setTitle(title);
                    }
                    if(description != null){
                        task.setDescription(description);
                    }
                    if(dueDate != null) {
                        task.setDueDate(LocalDate.parse(dueDate));
                    }
                    if(completed != null) {
                        task.setCompleted(completed);
                    }
                    return taskRepo.save(task);
                });
    }
}
