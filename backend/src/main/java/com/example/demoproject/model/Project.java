package com.example.demoproject.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import java.util.List;

/**
 * Project Object-Relational Mapping with database.
 *
 * @author Hieu. Ho Nhat - CMC Global CSC
 * @author Anh. Vu Tuan
 * @author Thach. Pham - CMC Global CSC
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Project {
    /**
     * id of project
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * name of project.
     */
    @Column(unique = true)
    private String name;
    /**
     * List<Task> in project.
     */
    @JsonBackReference(value = "project_tasks")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "project")
    private List<Task> tasks;
}
