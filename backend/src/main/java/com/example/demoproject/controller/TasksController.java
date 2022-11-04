package com.example.demoproject.controller;

import com.example.demoproject.model.Task;

import com.example.demoproject.service.ITaskService;
import com.example.demoproject.utils.Messages;
import com.example.demoproject.utils.ResponseHandler;
import com.example.demoproject.utils.ValidateDate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is controller class for task object.
 * @author Hieu. Ho Nhat - CMC Global CSC
 * @author Anh. Vu Tuan
 * @author Minh. Ho Si Tuan - CMC Global CSC
 * @author Thach. Pham - CMC Global CSC
 * */
@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/task")
public class TasksController {
    /**
     * inject taskService.
     */
    @Autowired
    private ITaskService taskService;
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
     * {@code GET  /api/v1/task/:id} : get the task with specific id.
     *
     * @param id - task id.
     * @return the {@link ResponseEntity}
     * with status {@code 200 (OK)} and the task in body.
     * or status {@code 400 (BAD REQUEST)} if it not exists.
     */
    @GetMapping("{id}")
    private ResponseEntity<Object> getDetailTask(@PathVariable final long id) {
        LOGGER.info("BEGIN Get detail task (id={})", id);
        Task task = taskService.findById(id);
        if (task == null) {
            return responseHandler.generateResponse(
                    HttpStatus.BAD_REQUEST, Messages.DATA_NOT_FOUND, null);
        }
        return responseHandler.generateResponse(
                HttpStatus.OK, Messages.DATA_LOAD_SUCCESS, task);
    }

    /**
     * {@code GET  /api/v1/task} : get list of tasks.
     *
     * @return list Tasks.
     */
    @GetMapping("")
    public ResponseEntity<List<Task>> findAll() {
        LOGGER.info("Retrieve all tasks");
        return new ResponseEntity<>(taskService.findAll(), HttpStatus.OK);
    }

    /**
     * {@code GET  /api/v1/task/listSub/:id} : get the task sub.
     *
     * @return the {@link ResponseEntity}
     * with status {@code 200 (OK)} and the list of task in body.
     * or status {@code 400 (BAD REQUEST)} if it not exists.
     */
    @GetMapping("listSub/{id}")
    public ResponseEntity<Object> getById(final @PathVariable Long id) {
        LOGGER.info("Get task by id (id={})", id);
        List<Task> taskList = taskService.listSub(id);
        if (taskList == null) {
            return responseHandler.generateResponse(
                    HttpStatus.BAD_REQUEST, Messages.DATA_NOT_FOUND, null);
        }
        return responseHandler.generateResponse(
                HttpStatus.OK, Messages.DATA_LOAD_SUCCESS, taskList);
    }

    /**
     * {@code GET  /api/v1/task/listMain} : get the task main.
     *
     * @return the {@link ResponseEntity}
     * with status {@code 200 (OK)} and the list of task in body.
     * or status {@code 400 (BAD REQUEST)} if it not exists.
     */
    @GetMapping("/listMain")
    public ResponseEntity<Object> getAllMainTasks() {
        LOGGER.info("Retrieve all main tasks");
        List<Task> taskList = taskService.listMain();

        if (taskList.isEmpty()) {
            return responseHandler.generateResponse(
                    HttpStatus.BAD_REQUEST, Messages.DATA_NOT_FOUND, null);
        }
        return responseHandler.generateResponse(
                HttpStatus.OK, Messages.DATA_LOAD_SUCCESS, taskList);
    }

    /**
     * {@code POST /api/v1/task/subtask} : create sub task.
     *
     * @param id - main task id
     * @param task - new task (subtask)
     * @param bindingResult - BindingResult object
     * @return the {@link ResponseEntity}
     * with status {@code 200 (OK)} and the new subtask in body.
     * or status {@code 400 (BAD REQUEST)} if bindingResult has errors field.
     */
    @PostMapping("/createSub/{id}")
    public ResponseEntity<Object> createSubTask(
            final @Validated @PathVariable Long id,
            final @RequestBody Task task,
            BindingResult bindingResult) {
        LOGGER.info("Create subtask with mainTask id (id={})", id);
        if (bindingResult.hasFieldErrors()) {
            Map<String, Object> response = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> { });
            return responseHandler.generateResponse(
                    HttpStatus.BAD_REQUEST, Messages.DATA_INVALID, response);
        }
        Task taskParent = taskService.findById(id);
        boolean checkTimeStart = ValidateDate.checkDate(taskParent.getDate(),task.getDate());
        boolean checkTimeEnd = ValidateDate.checkDate(task.getEnd(), taskParent.getEnd());
        if (checkTimeStart == false){
            Map<String, Object> response = new HashMap<>();
            response.put(Messages.CHECK_START_DATE, Messages.MS_START_DATE);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);


        }if (checkTimeEnd == false){
            Map<String, Object> response = new HashMap<>();
            response.put(Messages.CHECK_END_DATE, Messages.MS_END_DATE);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        Task taskUpdate = taskService.createSubTask(id, task);
        return responseHandler.generateResponse(
                HttpStatus.OK, Messages.DATA_LOAD_SUCCESS, taskUpdate);
    }

    /**
     *{@code PUT  /api/v1/task/update/: id} : update task.
     *
     * @param id - task id.
     * @param task - task with new information.
     * @param bindingResult - BindingResult Object
     * @return the {@link ResponseEntity}
     * with status {@code 200 (OK)} and task is updated
     * or status {@code 400 (BAD REQUEST)} if bindingResult has errors field.
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<Object> update(
            final @Validated @PathVariable Long id,
            final @RequestBody Task task,
            BindingResult bindingResult) {
        LOGGER.info("update task has id (id={})", id);
        if (bindingResult.hasFieldErrors()) {
            Map<String, Object> response = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> { });
            return responseHandler.generateResponse(
                    HttpStatus.BAD_REQUEST, Messages.DATA_INVALID, response);
        }
        Task taskUpdate = taskService.updateTask(id, task);
        return responseHandler.generateResponse(
                HttpStatus.OK, Messages.DATA_LOAD_SUCCESS, taskUpdate);
    }

    /**
     * {@code POST /api/v1/task } : create new task.
     *
     * @param task - task object.
     * @param bindingResult - BindingResult Object.
     * @return the {@link ResponseEntity}
     * with status {@code 201 (CREATE)} and task is updated
     * or status {@code 400 (BAD REQUEST)} if new task has invalid data.
     */
    @PostMapping("/create")
    public ResponseEntity<?> createMainTask(
            @Validated @RequestBody final Task task,
            BindingResult bindingResult) {
        LOGGER.info("create main task (taskName={}", task.getName());
        if (bindingResult.hasFieldErrors(String.valueOf(task))) {
            Map<String, Object> response = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> {
            });
            return responseHandler.generateResponse(HttpStatus.NOT_ACCEPTABLE,
                    Messages.DATA_INVALID, response);
        } else {
            boolean checkTime = ValidateDate.checkDate(task.getDate(),task.getEnd());
            if (checkTime == false){
                Map<String, Object> response = new HashMap<>();
                response.put("checkDate", "the end time must be after than start");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            Task mainTask  = taskService.createMainTask(task);
            return responseHandler.generateResponse(HttpStatus.OK,
                    Messages.DATA_UPDATE_SUCCESS,
                    mainTask);
        }
    }
    /**
     * {@code DELETE /api/v1/tasks/:id} : delete tasks by id.
     *
     * @param id - task id.
     * @return {@code 204 (NO CONTENT)} if success
     * or status {@code 400 (BAD REQUEST)} if it not exists or delete fail.
     */
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteTaskById(@PathVariable final long id) {
        LOGGER.info("Delete task by id (id={})", id);
        if (taskService.findById(id) != null) {
            if (taskService.deleteTaskById(id)) {
                return responseHandler.generateResponse(
                        HttpStatus.OK, Messages.DATA_DELETE_SUCCESS, null);
            }
            return responseHandler.generateResponse(
                    HttpStatus.BAD_REQUEST, Messages.DATA_DELETE_FAILED, null);
        }
        return responseHandler.generateResponse(
                HttpStatus.BAD_REQUEST, Messages.DATA_NOT_FOUND, null);
    }
}


