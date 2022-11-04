package com.example.taskservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Dependency Object-Relational Mapping with database.
 *
 * @author Hieu. Ho Nhat - CMC Global CSC
 * @author Anh. Vu Tuan
 * @author Minh. Ho Si Tuan - CMC Global CSC
 * @author Thach. Pham - CMC Global CSC
 * */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Dependency {

    /**
     * id of dependency.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * subtask id of current task.
     */
    private Long subTaskId;
    /**
     * Task - current task (main task).
     */
    @ManyToOne
    @JoinColumn(name = "main_task_id", referencedColumnName = "id")
    private Task task;


    /**
     * Constructor with params.
     *
     * @param subTaskId - subtask id of current task.
     * @param task - current task.
     */
    public Dependency(Long subTaskId, Task task) {
        this.subTaskId = subTaskId;
        this.task = task;
    }
}
