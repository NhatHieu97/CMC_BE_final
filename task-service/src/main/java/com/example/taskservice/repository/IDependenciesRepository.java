package com.example.taskservice.repository;

import com.example.taskservice.model.Dependency;
import com.example.taskservice.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * This is dependency repository interface - extends JPA Repository
 * to support mysql query by jpa template.
 * @author Hieu. Ho Nhat - CMC Global CSC
 * @author Anh. Vu Tuan
 * @author Thach. Pham - CMC Global CSC
 * */
@Repository
public interface IDependenciesRepository extends JpaRepository<Dependency, Long> {
    /**
     * Find dependency by subTask id
     * (get mainTask Id by subTask Id).
     *
     * @param taskId - subTask id.
     * @return Dependency - has subTaskId (input) and mainTaskId.
     */
    Dependency findBySubTaskId(Long taskId);
    /**
     * Find all Dependency by Task
     * List<dependency> of task
     * (Get list subTask of mainTask by Task (mainTask)).
     *
     * @param task - main task.
     * @return List<Dependency>
     */
    List<Dependency> findAllByTask(Task task);
}
