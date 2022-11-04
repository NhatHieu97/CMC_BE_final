package com.example.demoproject.controller;

import com.example.demoproject.model.Project;
import com.example.demoproject.service.IProjectService;
import com.example.demoproject.utils.Messages;
import com.example.demoproject.utils.ResponseHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

/**
 * This is controller class for project object.
 * @author Thach. Pham - CMC Global CSC
 * @author Anh. Vu Tuan
 * */
@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/projects")
public class ProjectController {
    /**
     * inject projectService.
     */
    @Autowired
    private IProjectService projectService;
    /**
     * inject responseHandler.
     */
    private ResponseHandler responseHandler;
    /**
     * inject logging service.
     */
    private static final Logger LOGGER =
            LogManager.getLogger(TasksController.class);
    /**
     * {@code GET /api/v1/projects} : retrieve all projects.
     *
     * @return List<Project> - all projects.
     */
    @GetMapping
    public ResponseEntity<Object> findAllProjects() {
        LOGGER.info("Retrieve all projects");
        return responseHandler.generateResponse(HttpStatus.OK,
                Messages.DATA_LOAD_SUCCESS, projectService.findAllProjects());
    }

    /**
     * {@code GET /api/v1/projects/:id} : retrieve project by id.
     *
     * @param id - Project id.
     * @return the {@link ResponseEntity}
     * with status {@code 200 (OK)} and the project in body
     * or status {@code 400 (BAD REQUEST)} if it not exists.
     */
    @GetMapping("{id}")
    public ResponseEntity<?> findProjectById(@PathVariable final long id) {
        LOGGER.info("Retrieve project by project id (id={})", id);
        Project res = projectService.findProjectById(id);
        if (res != null) {
            return responseHandler.generateResponse(HttpStatus.OK,
                    Messages.DATA_LOAD_SUCCESS, res);
        } else {
            return responseHandler.generateResponse(HttpStatus.BAD_REQUEST,
                    Messages.DATA_NOT_FOUND, null);
        }
    }

    /**
     * {@code POST /api/v1/projects } : create new project.
     *
     * @param project - new project with its information.
     * @return the {@link ResponseEntity}
     * with status {@code 200 (OK)} and new project in body
     * or status {@code 400 (BAD REQUEST)} if new project not verified.
     */
    @PostMapping
    public ResponseEntity<?> createProject(final Project project) {
        LOGGER.info("Create new project with project name (name={})",
                project.getName());
        if  (projectService.validateProject(project)) {
            return responseHandler.generateResponse(HttpStatus.CREATED,
                    Messages.DATA_CREATE_SUCCESS,
                    projectService.createProject(project));
        } else {
            return responseHandler.generateResponse(HttpStatus.BAD_REQUEST,
                    Messages.DATA_INVALID, null);
        }
    }

    /**
     * {@code PUT /api/v1/projects/:id} : update information of project.
     *
     * @param id - project id.
     * @param project - project with new information.
     * @return the {@link ResponseEntity}
     * with status {@code 200 (OK)} and project after update.
     *  or status {@code 400 (BAD REQUEST)}
     *  if project id not exist or data of project invalid.
     */
    @PutMapping("{id}")
    public ResponseEntity<?> updateProjectById(@PathVariable final long id,
                                               final Project project) {
        LOGGER.info("Update project with project id (id={})", id);
        if (projectService.validateProject(project)) {
            Project res = projectService.updateProject(id, project);
            if (res != null) {
                return responseHandler.generateResponse(HttpStatus.OK,
                        Messages.DATA_UPDATE_SUCCESS, res);
            }
            return responseHandler.generateResponse(HttpStatus.BAD_REQUEST,
                    Messages.DATA_NOT_FOUND, null);
        }
        return responseHandler.generateResponse(HttpStatus.BAD_REQUEST,
                Messages.DATA_INVALID, null);
    }

    /**
     * {@code DELETE /api/v1/projects/:id} : delete project by id.
     * @param id - project id
     * @return {@code 204 (NO CONTENT)} if success
     * or status {@code 400 (BAD REQUEST)} if it not exists or delete fail.
     */
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteProjectById(@PathVariable final long id) {
        LOGGER.info("Delete project by project id (id={})", id);
        Project flag = projectService.findProjectById(id);
        if (flag != null) {
            if (projectService.deleteProjectById(id)) {
                return responseHandler.generateResponse(HttpStatus.NO_CONTENT,
                        Messages.DATA_DELETE_SUCCESS, null);
            }
            return responseHandler.generateResponse(HttpStatus.BAD_REQUEST,
                    Messages.DATA_DELETE_FAILED, null);
        }
        return responseHandler.generateResponse(HttpStatus.BAD_REQUEST,
                Messages.DATA_NOT_FOUND, null);
    }
}
