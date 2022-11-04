package com.example.demoproject.repository;

import com.example.demoproject.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This is task repository interface - extends JPA Repository
 * to support mysql query by jpa template.
 *
 * @author Hieu. Ho Nhat - CMC Global CSC
 * @author Anh. Vu Tuan
 * */
public interface ITasksRepository extends JpaRepository<Task, Long> {
}
