package com.example.taskservice.repository;

import com.example.taskservice.model.TaskLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITaskLableRepository extends JpaRepository<TaskLabel, Long> {
}
