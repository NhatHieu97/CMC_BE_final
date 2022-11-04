package com.example.taskservice.service;

import com.example.taskservice.model.dto.Project;
import com.example.taskservice.model.dto.ResponseTemplate;
import com.example.taskservice.model.Task;
import com.example.taskservice.model.dto.ResponseTemplateDTO;
import com.example.taskservice.model.dto.TaskCreateDTO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This is task service interface.
 *
 * @author Hieu. Ho Nhat - CMC Global CSC
 * @author Anh. Vu Tuan
 * @author Minh. Ho Si Tuan - CMC Global CSC
 * @author Thach. Pham - CMC Global CSC
 * */
@Service
public interface ITaskService {
    /**
     * Retrieve task by its Id.
     * @param id - task Id.
     * @return ResponseTemplateVO with Id input.
     */
    ResponseTemplate findByIdTask(Long id);

    /**
     * Retrieve task by its Id.
     * @param id - task Id.
     * @return Task with Id input.
     * @return Task with id input.
     */
    Task findById(Long id);

    /**
     * Retrieve all tasks.
     * @return List of all task.
     */
    List<Task> findAll();

    /**
     * Retrieve all subTask in main Task.
     * @param id - main task id.
     * @return List of all subTask in main task.
     */
    List<ResponseTemplate> listSub(Long id);

    /**
     * List all task not a subTask (only a mainTask)
     *
     * @return List of main Task (not a subtask).
     */
    List<ResponseTemplateDTO> listMain();

    /**
     * Create mainTask.
     *
     * @param taskCreateDTO - new task.
     * @return Task after create.
     */
    TaskCreateDTO createMainTask(TaskCreateDTO taskCreateDTO);

    /**
     * Update task detail.
     *
     * @param id   - task id.
     * @param task - task has new information.
     * @return Task
     */
    TaskCreateDTO updateTask (Long id, TaskCreateDTO task);

    /**
     * Create subTask.
     *
     * @param id            - main Task id.
     * @param taskCreateDTO - new subTask with label.
     * @return Task - after create.
     */
    TaskCreateDTO createSubTask(Long id, TaskCreateDTO taskCreateDTO);

    /**
     * Delete Task, subtasks of Task and update parent task progress.
     * @param id - dependency
     * @return true/false
     */
    boolean deleteTaskById(final long id);


    /**
     * Retrieve all task in tag
     * @param id - label id.
     * @return List tag of task id
     */
    List<Task> listLabelById(Long id);


}
