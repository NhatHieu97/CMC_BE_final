package com.example.demoproject.repository;

import com.example.demoproject.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This is project repository interface - extends JPA Repository
 * to support mysql query by jpa template.
 *
 * @author Thach. Pham - CMC Global CSC
 * */
@Repository
public interface IProjectRepository extends JpaRepository<Project, Long> {
}
