package com.example.demoproject.service;

import com.example.demoproject.model.Project;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This is project service interface.
 *
 * @author Anh. Vu Tuan
 * @author Thach. Pham - CMC Global CSC
 * */
@Service
public interface IProjectService {
    /**
     * Validate project input.
     * @param project - a project with full information.
     * @return true/false - project is valid or no
     */
    boolean validateProject(final Project project);
    /**
     * Retrieve all projects.
     * @return List<project> - list of all projects.
     */
    List<Project> findAllProjects();
    /**
     * Retrieve project by its id.
     * @param id - project id of project need to retrieve.
     * @return project has project id input or null.
     */
    Project findProjectById(long id);
    /**
     * Create project with new information.
     * @param project - new project.
     * @return Project - new project with its project id.
     */
    Project createProject(final Project project);
    /**
     * Update new information of project.
     * @param id - project id which need to update information.
     * @param project - project with new information.
     * @return project with new information.
     */
    Project updateProject(Long id, Project project);

    /**
     * Delete project and its task by project Id.
     * @param id - id of project.
     * @return true for success or false for else.
     */
    boolean deleteProjectById(long id);
}
