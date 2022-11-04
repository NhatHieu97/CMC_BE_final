package com.example.taskservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Task Object-Relational Mapping with database.
 *
 * @author Hieu. Ho Nhat - CMC Global CSC
 * @author Anh. Vu Tuan
 * @author Minh. Ho Si Tuan - CMC Global CSC
 * @author Thach. Pham - CMC Global CSC
 * */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Task {
    /**
     * Id of task.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Name of task.
     */
    private String name;
    /**
     * start date of task.
     */
    private String date;
    /**
     * end date of task.
     */
    private String end;
    /**
     * complete percent of task.
     */
    private int progress;
    /**
     * this task is a main task (true) or else (null).
     */
    private  Boolean main;
    /**
     * this task is a sub task (true) or else (null)
     */
    private  Boolean sub;
    /**
     * List<dependency> in a task.
     * Contain subtask list of current task.
     */
    @JsonBackReference(value = "dependencies_tasks")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "task")
    private List<Dependency> dependencies;
    /**
     * id project which contain current task.
     */

    private Long projectId;

    @JsonBackReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "task")
    private List<TaskLabel> taskLabel;

    public Task(Long id, String name, String date, String end, int progress, Boolean main, Boolean sub, List<Dependency> dependencies, Long projectId, List<TaskLabel> taskLabel) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.end = end;
        this.progress = progress;
        this.main = main;
        this.sub = sub;
        this.dependencies = dependencies;
        this.projectId = projectId;
        this.taskLabel = taskLabel;
    }
}
