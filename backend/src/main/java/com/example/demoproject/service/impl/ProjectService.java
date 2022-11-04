package com.example.demoproject.service.impl;

import com.example.demoproject.model.Project;
import com.example.demoproject.repository.IProjectRepository;
import com.example.demoproject.service.IProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This is project service class
 * implements project service interface (DI)
 * Has CRUD functions with project.
 *
 * @author Anh. Vu Tuan
 * @author Thach. Pham - CMC Global CSC
 * */
@Service
public class ProjectService implements IProjectService {
    /**
     * inject projectRepository.
     */
    @Autowired
    private IProjectRepository projectRepository;

    /**
     * Verify project.
     * @param project
     * @return true/false
     * */
    @Override
    public boolean validateProject(final Project project) {
        final int maxLengthOfProjectName = 255;
        if (project.getName().length() < 0
                || project.getName().length() > maxLengthOfProjectName) {
            return false;
        } else {
            for (Project i: projectRepository.findAll()) {
                if (project.getName().equals(i.getName())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Retrieve all projects.
     * @return  list has all projects
     */
    @Override
    public List<Project> findAllProjects() {
        return projectRepository.findAll();
    }

    /**
     * Retrieve a project by id.
     * @param id - id of project need to retrieve detail.
     * @return  project with id input or new project if it not exist
     */
    @Override
    public Project findProjectById(final long id) {
        return projectRepository.findById(id).orElse(null);
    }

    /**
     * Create new project.
     * @param project - with new information.
     * @return new Project
     */
    @Override
    public Project createProject(final Project project) {
        return projectRepository.save(project);
    }

    /**
     * update information (name) of project.
     * @param id - id of project need to update information.
     * @return project after update or null
     */
    @Override
    public Project updateProject(final Long id, final Project project) {
        return projectRepository.findById(id)
                .map(updateProject -> {
                    updateProject.setName(project.getName());
                    return projectRepository.save(updateProject);
                })
                .orElseGet(() -> {
                    return null;
                });
    }

    /**
     * Delete project and its tasks by project id.
     * @param id - id of project.
     * @return true/false
     */
    @Override
    public boolean deleteProjectById(final long id) {
        try {
            projectRepository.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
