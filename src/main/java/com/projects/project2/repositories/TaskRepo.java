package com.projects.project2.repositories;

import com.projects.project2.model.Status;
import com.projects.project2.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepo extends JpaRepository<Task, Integer> {

    List<Task> findByStatus(Status status);
}
