package com.example.taskservice.repository;

import com.example.taskservice.model.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This is label repository interface - extends JPA Repository
import com.example.taskservice.model.TaskLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This is dependency repository interface - extends JPA Repository
 * to support mysql query by jpa template.
 * @author Hieu. Ho Nhat - CMC Global CSC
 * @author Anh. Vu Tuan
 * @author Thach. Pham - CMC Global CSC
 * @author Minh. Ho Si Tuan - CMC Global CSC
 * */
@Repository
public interface ILabelRepository extends JpaRepository<Label, Long> {
}
