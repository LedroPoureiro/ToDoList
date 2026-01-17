package com.projects.project2.repositories;

import com.projects.project2.model.Task;
import com.projects.project2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    //List<Task> findByStatus(Status status);

    List<Task> findByUser(User user);
    Optional<Task> findByIdAndUserId(int taskId, Long userId);
}
