package com.example.taskservice.model.dto;

import com.example.taskservice.model.Dependency;
import com.example.taskservice.model.Label;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * This Task Object using when createTask with label.
 *
 * @author Thach. Pham - CMC Global CSC
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskCreateDTO {
    /**
     * id of task.
     */
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
     * id project which contain current task.
     */
    private Long projectId;
    /**
     * Label Id
     */
    private List<Label> labels;
}
