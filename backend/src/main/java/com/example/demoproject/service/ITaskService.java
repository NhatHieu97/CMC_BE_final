package com.example.demoproject.service;

import com.example.demoproject.model.Task;
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
     * @return Task with Id input.
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
    List<Task> listSub(Long id);

    /**
     * List all task not a subTask (only a mainTask)
     * @return List of main Task (not a subtask).
     */
    List<Task> listMain();

    /**
     * Create mainTask.
     * @param task - new task.
     * @return Task after create.
     */
    Task createMainTask(Task task);

    /**
     * Update task detail.
     * @param id - task id.
     * @param task - task has new information.
     * @return Task
     */
    Task updateTask (Long id,Task task);

    /**
     * Create subTask.
     * @param id - main Task id.
     * @param task - new subTask.
     * @return Task - after create.
     */
    Task createSubTask(Long id, Task task);

    /**
     * Delete Task, subtasks of Task and update parent task progress.
     * @param id
     * @return true/false
     */
    boolean deleteTaskById(final long id);
}
