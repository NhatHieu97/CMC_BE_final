package com.example.taskservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
public class TaskLabel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id", referencedColumnName = "id")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "label_id", referencedColumnName = "id")
    private Label label;

    public TaskLabel(Long id, Task task, Label label) {
        this.id = id;
        this.task = task;
        this.label = label;
    }
}
