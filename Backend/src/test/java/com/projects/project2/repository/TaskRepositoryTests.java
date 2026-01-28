package com.projects.project2.repository;

import com.projects.project2.model.Task;
import com.projects.project2.model.User;
import com.projects.project2.repositories.TaskRepository;
import com.projects.project2.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class TaskRepositoryTests {
    @Autowired
    private TaskRepository taskRepo;

    @Autowired
    private UserRepository userRepo;

    @Test
    public void TaskRepository_Save_ReturnSavedTask() {
        User user = User.builder().username("antonio").password("123").build();
        User savedUser = userRepo.save(user);
        Task task = Task.builder().title("myTitle").description("descriptionName").completed(false).user(savedUser).build();

        Task savedTask = taskRepo.save(task);

        assertThat(savedTask).isNotNull();
        assertThat(savedTask.getId()).isGreaterThan(0);
    }

    @Test
    public void TaskRepository_FindAll_ReturnMoreThanOneTask() {
        User user = User.builder().username("antonio").password("123").build();
        User savedUser = userRepo.save(user);
        Task task1 = Task.builder().title("myTitle").description("descriptionName").completed(false).user(savedUser).build();
        Task task2 = Task.builder().title("myTitle2").description("descriptionName2").completed(false).user(savedUser).build();

        taskRepo.save(task1);
        taskRepo.save(task2);
        List<Task> taskList = taskRepo.findAll();

        assertThat(taskList).isNotNull();
        assertThat(taskList.size()).isEqualTo(2);
    }

    @Test
    public void TaskRepository_FindById_ReturnNotNull() {
        User user = User.builder().username("antonio").password("123").build();
        User savedUser = userRepo.save(user);
        Task task = Task.builder().title("myTitle").description("descriptionName").completed(false).user(savedUser).build();

        taskRepo.save(task);
        Task returnedTask = taskRepo.findById(task.getId()).get();

        assertThat(returnedTask).isNotNull();
    }

    @Test
    public void TaskRepository_FindByUser_ReturnMoreThanOneTask() {
        User user = User.builder().username("antonio").password("123").build();
        User savedUser = userRepo.save(user);
        Task task = Task.builder().title("myTitle").description("descriptionName").completed(false).user(savedUser).build();

        taskRepo.save(task);
        List<Task> taskList = taskRepo.findByUser(savedUser);

        assertThat(taskList.size()).isEqualTo(1);
    }

    @Test
    public void TaskRepository_UpdateTask_ReturnNotNull(){
        User user = User.builder().username("antonio").password("123").build();
        User savedUser = userRepo.save(user);
        Task task = Task.builder().title("myTitle").description("descriptionName").completed(false).user(savedUser).build();

        taskRepo.save(task);
        Task returnedTask = taskRepo.findById(task.getId()).get();
        returnedTask.setTitle("aaa");
        returnedTask.setDescription("bbb");

        Task updatedTask = taskRepo.save(returnedTask);

        assertThat(updatedTask.getTitle()).isNotNull();
        assertThat(updatedTask.getDescription()).isNotNull();
    }

    @Test
    public void TaskRepository_DeleteTask_ReturnTaskIsEmpty() {
        User user = User.builder().username("antonio").password("123").build();
        User savedUser = userRepo.save(user);
        Task task = Task.builder().title("myTitle").description("descriptionName").completed(false).user(savedUser).build();

        taskRepo.save(task);
        taskRepo.deleteById(task.getId());
        Optional<Task> returnedTask = taskRepo.findById(task.getId());

        assertThat(returnedTask).isEmpty();
    }
}
