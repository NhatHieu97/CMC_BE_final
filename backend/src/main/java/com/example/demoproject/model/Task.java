package com.example.demoproject.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import java.util.List;

/**
 * Task Object-Relational Mapping with database.
 *
 * @author Hieu. Ho Nhat - CMC Global CSC
 * @author Anh. Vu Tuan
 * @author Minh. Ho Si Tuan - CMC Global CSC
 * @author Thach. Pham - CMC Global CSC
 * */
@Data
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
     * project which contain current task.
     */
    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private Project project;

    /**
     * constructor with params
     *
     * @param id - task id
     * @param name - task name
     * @param date - date start of task
     * @param end - date end of task
     * @param progress - complete percent of task
     * @param main - true/null is main task
     * @param sub - true/null is sub task
     * @param dependencies - List<dependency> in task
     * @param project - project which task in
     */
    public Task(Long id, String name, String date, String end, int progress, Boolean main, Boolean sub, List<Dependency> dependencies, Project project) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.end = end;
        this.progress = progress;
        this.main = main;
        this.sub = sub;
        this.dependencies = dependencies;
        this.project = project;
    }
}

