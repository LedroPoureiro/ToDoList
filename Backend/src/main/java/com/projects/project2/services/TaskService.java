package com.projects.project2.services;

import com.projects.project2.model.User;
import com.projects.project2.model.Task;
import org.springframework.stereotype.Service;
import com.projects.project2.repositories.TaskRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskService.class);
    private final TaskRepository taskRepo;

    public TaskService(TaskRepository taskRepository)
    {
        this.taskRepo = taskRepository;
    }

    public List<Task> getTasks(User user)
    {
        return taskRepo.findByUser(user);
    }

    public Task getTaskByID(int taskID, User user)
    {
        Optional<Task> taskOptional = taskRepo.findByIdAndUserId(taskID, user.getId());

        if(taskOptional.isEmpty() && taskRepo.existsById(taskID)) {
            log.warn("Security: User {} attempted to access task {} owned by another user", user.getUsername(), taskID);
        }

        return taskOptional.orElse(null);
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

    public boolean deleteTask(int taskID, User user)
    {
        if (taskRepo.findByIdAndUserId(taskID, user.getId()).isPresent()) {
            taskRepo.deleteById(taskID);
            return true;
        }
        if(taskRepo.existsById(taskID))
        {
            log.warn("Security: User {} attempted to delete task {} owned by another user", user.getUsername(), taskID);
        }
        return false;
    }

    public Optional<Task> updateTask(int taskID, String title, String description, String dueDate, Boolean completed, User user)
    {
        Optional<Task> taskOptional = taskRepo.findByIdAndUserId(taskID, user.getId());
        if(taskOptional.isEmpty()){
            if(taskRepo.existsById(taskID)) {
                log.warn("Security: User {} attempted to update task {} owned by another user", user.getUsername(), taskID);
            } else {
                log.info("Update failed: Task {} does not exist (requested by user {})", taskID, user.getUsername());
            }
            return Optional.empty();
        }
        return taskOptional
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
