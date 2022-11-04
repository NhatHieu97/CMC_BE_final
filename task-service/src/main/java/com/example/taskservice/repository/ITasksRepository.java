package com.example.taskservice.repository;

import com.example.taskservice.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This is task repository interface - extends JPA Repository
 * to support mysql query by jpa template.
 *
 * @author Hieu. Ho Nhat - CMC Global CSC
 * @author Anh. Vu Tuan
 * */
@Repository
public interface ITasksRepository extends JpaRepository<Task, Long> {

}
