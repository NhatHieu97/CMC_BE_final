package com.example.taskservice.service.impl;

import com.example.taskservice.model.Dependency;
import com.example.taskservice.model.Label;
import com.example.taskservice.model.Task;
import com.example.taskservice.model.TaskLabel;
import com.example.taskservice.model.dto.Project;
import com.example.taskservice.model.dto.ResponseTemplate;
import com.example.taskservice.model.dto.ResponseTemplateDTO;
import com.example.taskservice.model.dto.TaskCreateDTO;
import com.example.taskservice.repository.IDependenciesRepository;
import com.example.taskservice.repository.ILabelRepository;
import com.example.taskservice.repository.ITaskLabelRepository;
import com.example.taskservice.repository.ITasksRepository;
import com.example.taskservice.service.ITaskLabelService;
import com.example.taskservice.service.ITaskService;
import com.example.taskservice.utils.Converter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * This is task service class
 * implements task service interface (DI).
 *
 * @author Hieu. Ho Nhat - CMC Global CSC
 * @author Anh. Vu Tuan
 * @author Minh. Ho Si Tuan - CMC Global CSC
 * @author Thach. Pham - CMC Global CSC
 * */
@Service
public class TaskService implements ITaskService {

    @Autowired
    private ITaskLabelService taskLabelService;



    /**
     * inject restTemplate.
     */
    @Autowired
    private RestTemplate restTemplate;

    /**
     * inject dependenciesRepository.
     */
    @Autowired
    private IDependenciesRepository dependenciesRepository;
    /**
     * inject taskLabelRepository.
     */
    @Autowired
    private ITaskLabelRepository taskLabelRepository;

    /**
     * inject labelRepository.
     */
    @Autowired
    private ILabelRepository labelRepository;
    /**
     * inject tasksRepository.
     */
    @Autowired
    private ITasksRepository tasksRepository;

    /**
     * inject logging service.
     */
    private static final Logger LOGGER =
            LogManager.getLogger(TaskService.class);
    //==================================private function===============
    /**
     * Get All child Task of main Task.
     * Note: need to check task exist before call this function
     * and mainTask isnt added to TaskList.
     * @param id - id of task want to get child task.
     * @param taskMap - Map to save all task by key:taskId and value:Task
     * @param childTasks - Set to save all childTask of task.
     */
    private void getChildTask(final Long id,
                              final Map<Long, Task> taskMap,
                              Set<Task> childTasks) {
        Task temp = taskMap.get(id);    // main task
        // scan all subtask to get task list
        if (temp.getMain() != null && temp.getMain() == true) {
            temp.getDependencies().forEach(dependencies -> {
                childTasks.add(taskMap.get(dependencies.getSubTaskId()));
                this.getChildTask(
                        dependencies.getSubTaskId(), taskMap, childTasks);
            });
        }
    }
    /**
     * Calculate value of progress.
     * @param id - id of task need to calculate progress.
     * @param taskMap - map of all task.
     * @return int - progress value of task.
     */
    private int progressCalculator(final long id,
                                   final Map<Long, Task> taskMap) {
        int progress = 0;   // sum progress of subtask
        // scan all subtask of main task to get sum progress
        for (Dependency dependencies: taskMap.get(id).getDependencies()) {
            progress += (taskMap.get(dependencies.getSubTaskId())
                    .getProgress());
        }
        // if main task don't have subtask
        if (taskMap.get(id).getDependencies().size() == 0) {
            return 0;
        }
        // return average of subtask progress (progress of main task)
        return (int) Math.floor(progress
                / (taskMap.get(id).getDependencies().size()));
    }
    /**
     * Using recursive to get all subtask of task id
     * (which will to be is delete).
     * @param id - id of task want to get subtask.
     * @param taskMap - List of all task
     * @param taskIdDelete - List of task will to be is delete
     */
    private void getDeletedTaskList(final long id,
                                    Map<Long, Task> taskMap,
                                    List<Long> taskIdDelete) {
        Task temp = taskMap.get(id);    // main task
        // scan all subtask to get task list
        if (temp.getMain() != null && temp.getMain() == true) {
            temp.getDependencies().forEach(dependencies -> {
                taskIdDelete.add(dependencies.getSubTaskId());
                this.getDeletedTaskList(
                        dependencies.getSubTaskId(), taskMap, taskIdDelete);
            });
        }
        // remove task by id (this tasks will be delete) from task map
        taskMap.remove(id);
    }
    /**
     * Update progress for task and parent Task.
     * @param id - task id
     * @param taskMap - the Map of task has key is taskId
     *                and value is Task Object.
     */
    private void updateProgressForTaskAndParentTask(
            final long id, Map<Long, Task> taskMap) {
        List<Task> tasksList = new ArrayList<>();   // this list has new updated tasks
        Map<Long, Long> dependenciesMap = new HashMap<>();  // save all dependencies by sub_task_id
        Task task = tasksRepository.findById(id).orElse(null); // task is updating...
        // Generate DependenciesMap
        dependenciesRepository.findAll()
                .forEach(dependencies ->
                        dependenciesMap.put(dependencies.getSubTaskId(),
                                dependencies.getTask().getId()));
        if (task != null) {
            // update for current task
            if (task.getDependencies().size() == 0) {
                task.setMain(null);
            }
            task.setProgress(this.progressCalculator(
                    task.getId(), taskMap));
            tasksList.add(task);
            // update for parent task
            while (task.getSub() != null) {
                task = taskMap.get(
                        dependenciesMap.get(task.getId()));
                task.setProgress(this.progressCalculator(
                        task.getId(), taskMap));
                tasksList.add(task);
            }
            // save all to db
            tasksRepository.saveAll(tasksList);
        }
    }

    /**
     * Get list new label of task will be create.
     * @param task - current task.
     * @param labels - list label of task before execute.
     * @return List new label of task.
     */
    private List<TaskLabel> getLabelsUpdate(Task task, List<Label> labels) {
        // update label
        Map<Long, Label> labelMap = new HashMap<>();    // map to save all label
        Map<Long, Task> taskMap = new HashMap<>();  // map to save all task
        TaskLabel newTaskLabel; // new taskLabel will be create
        Set<Task> childTask = new HashSet<>();  // List of child task
        List<TaskLabel> taskLabelUpdates = new ArrayList<>();   // list to save all taskLabel will be create
        Set<String> set2CheckDuplicateTaskLabel = new HashSet<>();  // set using to check duplicate taskLabel
        // save all Task in map to taskMap
        tasksRepository.findAll().forEach(taskIterator -> {
            taskMap.put(taskIterator.getId(), taskIterator);
        });
        // Save all TaskLabel to set2CheckDuplicateTaskLabel
        for (TaskLabel taskLabelIterator : taskLabelRepository.findAll()) {
            set2CheckDuplicateTaskLabel.add(taskLabelIterator.getLabel().getId() + "-" + taskLabelIterator.getTask().getId());
        }
        // save all child task of task to childTask list
        this.getChildTask(task.getId(), taskMap, childTask);
        // Add old Label of task Input to labelMap
        taskLabelRepository.findAllByTask(task)
                .forEach(taskLabelIterator -> {
                    labelMap.put(taskLabelIterator.getLabel().getId(),
                            taskLabelIterator.getLabel()
                    );
                });
        // get update label list
        // if old label list dont have new label
        for (Label labelIterator : labels) {
            if (labelMap.get(labelIterator.getId()) == null) {
                newTaskLabel = new TaskLabel();
                newTaskLabel.setTask(task);
                newTaskLabel.setLabel(labelIterator);
                taskLabelUpdates.add(newTaskLabel);
                for (Task childTaskIterator : childTask) {
                    if (!set2CheckDuplicateTaskLabel.contains(labelIterator.getId() + "-" + childTaskIterator.getId())) {
                        newTaskLabel = new TaskLabel();
                        newTaskLabel.setLabel(labelIterator);
                        newTaskLabel.setTask(childTaskIterator);
                        taskLabelUpdates.add(newTaskLabel);
                    }
                }
            }
        }
        // Dont check in parent task because dont have remove label of task feature
        return taskLabelUpdates;
    }
    //================================implement function===============
    /**
     * Retrieve Task by task id.
     * @param id - task id
     * @return Task
     */
    public ResponseTemplate findByIdTask(final Long id) {
        Task task = tasksRepository.findById(id).orElse(null);
        ResponseTemplate vo = new ResponseTemplate();
        Project project = restTemplate.getForObject("http://localhost:8081/api/v1/projects/" + task.getProjectId(), Project.class);
        vo.setTask(task);
        vo.setProject(project);
        return vo;
    }

    @Override
    public Task findById(Long id) {
        return tasksRepository.findById(id).orElse(null);
    }

    /**
     * Retrieve List of all tasks.
     * @return list task
     */
    public List<Task> findAll() {
        return  tasksRepository.findAll();
    }



    /**
     * List all main task and not a subtask.
     *
     * @return List of main tasks.
     */

    @Override
    public List<ResponseTemplateDTO> listMain() {
        List<Task> taskPage = tasksRepository.findAll();
        List<ResponseTemplateDTO> templateArrayList = new ArrayList<>();
        List<TaskCreateDTO> taskCreateDTOList = new ArrayList<>();
        TaskCreateDTO taskCreateDTO = null;
        
        for (int i = 0; i < taskPage.size(); i++){
            if (taskPage.get(i).getSub() == null){
                taskCreateDTO = new TaskCreateDTO();
                BeanUtils.copyProperties(taskPage.get(i), taskCreateDTO);
                List<TaskLabel> taskLabels = taskLabelService.findAllTaskLabel();
                List<Label> labelList = new ArrayList<>();
                for (int j = 0; j < taskLabels.size(); j++){
                    if(taskLabels.get(j).getTask().getId() == taskPage.get(i).getId()){
                        labelList.add(taskLabels.get(j).getLabel());
                    }
                }
                taskCreateDTO.setLabels(labelList);
//                taskCreateDTOList.add(taskCreateDTO);


                ResponseTemplateDTO vo = new ResponseTemplateDTO();
                Project project = restTemplate.getForObject("http://localhost:8081/api/v1/projects/" + taskPage.get(i).getProjectId(), Project.class);
                vo.setTaskCreateDTO(taskCreateDTO);
                vo.setProject(project);
                templateArrayList.add(vo);
            }
        }
        return templateArrayList;

    }



    /**
     * Retrieve list subtask of mainTask.
     * @param id - mainTask id.
     * @return list of subtask.
     */
    @Override
    public List<ResponseTemplate> listSub(Long id) {
        // Find main task by Id
        Task task = tasksRepository.findById(id).orElse(null);
        // Find add dependencies of main task
        List<Dependency> dependencyList = dependenciesRepository.findAllByTask(task);
        List<ResponseTemplate> taskList = new ArrayList<>();

        ResponseTemplate vo = new ResponseTemplate();

        for (int i = 0; i < dependencyList.size(); i++){
            // Find sub task by id
            Task subTask = tasksRepository.findById(dependencyList.get(i).getSubTaskId()).orElse(null);
            System.out.println(subTask.getProjectId());
            //get project with api http://localhost:8081/api/v1/projects/
            Project project = restTemplate.getForObject("http://localhost:8081/api/v1/projects/" + subTask.getProjectId(), Project.class);
            vo.setTask(subTask);
            vo.setProject(project);
            // Add subtask to return list.
            taskList.add(vo);
        }
        return taskList;
    }
    /**
     * Retrieve list label of Task.
     * @param id - taskLabel id.
     * @return list Task.
     */
    @Override
    public List<Task> listLabelById(final Long id) {
        LOGGER.info("Begin Service: list Task by label_id");
        // Get Label in filter
        Label label = labelRepository.findById(id).orElse(null);
        List<TaskLabel> taskLabels =  taskLabelRepository.findAllByLabel(label);
        List<Task> taskList = new ArrayList<>();

        taskLabels.forEach(taskLabel1 -> {
            taskList.add(taskLabel1.getTask());
        });
        LOGGER.info("End Service: list Task by label_id");
        return taskList;
    }


    /**
     * Create new main task.
     *
     * @param taskCreateDTO - new task with label Id
     * @return created task without labelId
     */
    @Override
    public TaskCreateDTO createMainTask(TaskCreateDTO taskCreateDTO) {
        LOGGER.info("TaskService.createMainTask with TaskCreateDTO {} - {} - {}",
                taskCreateDTO.getName(),
                taskCreateDTO.getProjectId(),
                taskCreateDTO.getLabels());
        Task newTask;   // new Task
        TaskLabel newTaskLabel; // New taskLabel
        Label label;    // label of new Task
        // Create task
        newTask = Converter.taskCreateDTO2Task(taskCreateDTO);
        tasksRepository.save(newTask);
        // Create label
        for (Label i: taskCreateDTO.getLabels()) {
            label = labelRepository.findById(i.getId()).orElse(null);
            if (label != null) {
                newTaskLabel = new TaskLabel();
                newTaskLabel.setTask(newTask);
                newTaskLabel.setLabel(label);
                taskLabelRepository.save(newTaskLabel);
            } else {
                // If label of task not exist -> roll back
                tasksRepository.deleteById(newTask.getId());
                LOGGER.error("TaskService.createMainTask fail: label not found with TaskCreateDTO {} - {} - {} and labelId {}",
                        taskCreateDTO.getName(),
                        taskCreateDTO.getProjectId(),
                        taskCreateDTO.getLabels(),
                        i.getId());
                return null;
            }
        }
        return taskCreateDTO;
    }

    /**
     * Update Task detail.
     *
     * @param id   - Task Id.
     * @param task - Task with new information.
     * @return task after update.
     */
    @Override
    public TaskCreateDTO updateTask(final Long id, TaskCreateDTO task) {
        LOGGER.info("BEGIN taskService.updateTask with taskId: {} - task: {}", id, task.getName());
        // Get Task before update.
        Task updateTask = tasksRepository.findById(id).orElse(null);
        // update information of task.
        updateTask.setName(task.getName());
        updateTask.setDate(task.getDate());
        updateTask.setEnd(task.getEnd());

        Task savedTask = null;
        //check case main is null, sub is null
        if ((updateTask.getMain() == null || !updateTask.getMain())
                && updateTask.getSub() == null) {
            updateTask.setProgress(task.getProgress());
            savedTask = tasksRepository.save(updateTask);
        //check case main differ true, sub is true
        } else if ((updateTask.getMain() == null || !updateTask.getMain())
                && updateTask.getSub()) {
            updateTask.setProgress(task.getProgress());
            savedTask = tasksRepository.save(updateTask);
            Task taskParent = dependenciesRepository
                    .findBySubTaskId(updateTask.getId()).getTask();
            int progress = dependenciesList(taskParent);
            taskParent.setProgress(progress);
            savedTask = tasksRepository.save(taskParent);
        } else {
            savedTask = tasksRepository.save(updateTask);
        }
        // update labels
        updateTask.setId(id);
        List<TaskLabel> taskLabelUpdates = this.getLabelsUpdate(updateTask, task.getLabels());
        taskLabelRepository.saveAll(taskLabelUpdates);
        taskLabelUpdates.forEach(i -> {
            LOGGER.info("TaskService.updateTask add label with id: {} to task with id: {}", i.getLabel().getId(), i.getTask().getId());
        });
        LOGGER.info("END TaskService.updateTask with task id: {}", id);
        return task;
    }
    /**
     * Create subTask.
     * 1 task has 2 identical label - solution: using set to save list label of task.
     *
     * @param id - main task id.
     * @return Task after create.
     */
    @Override
    public TaskCreateDTO createSubTask(final Long id, TaskCreateDTO taskCreateDTO) {
        LOGGER.info("TaskService.createSubTask with TaskCreateDTO {} - {} - {}",
                taskCreateDTO.getName(), taskCreateDTO.getProjectId(), taskCreateDTO.getLabels());
        int progress; // progress of parent task
        Task newSubTask;    // this task will be create
        Set<Label> labels = new HashSet<>();    // save all label of new Tasks
        Label newLabelOfTask;   // label of Task
        TaskLabel newTaskLabel; // new TaskLabel of Task
        // Get Task before create.
        Task parentTask = tasksRepository.findById(id).orElse(null);
        if (parentTask != null) {
            // Check new label of new sub task
            for (Label i: taskCreateDTO.getLabels()) {
                // Get Label of new Task
                newLabelOfTask = labelRepository.findById(i.getId()).orElse(null);
                if (newLabelOfTask == null) {
                    LOGGER.error("TaskService.createSubTask fail: label not found with TaskCreateDTO {} - {} - {} and labelId {}",
                            taskCreateDTO.getName(), taskCreateDTO.getProjectId(), taskCreateDTO.getLabels(), i.getId());
                    return null;
                }
                labels.add(newLabelOfTask);
            }
            // create new sub task
            newSubTask = Converter.taskCreateDTO2Task(taskCreateDTO);
            newSubTask.setMain(null);
            newSubTask.setSub(true);
            tasksRepository.save(newSubTask);
            // create new relationship between parent task and subTask (new dependency)
            Dependency dependency = new Dependency(newSubTask.getId(), parentTask);
            dependenciesRepository.save(dependency);
            // update label for subtask
            labels.addAll(taskLabelRepository.findAllByTaskId(id));
            for (Label label: labels) {
                newTaskLabel = new TaskLabel();
                newTaskLabel.setLabel(label);
                newTaskLabel.setTask(newSubTask);
                taskLabelRepository.save(newTaskLabel);
            }
            // update parentTask
            parentTask.setMain(true);
            progress = dependenciesList(parentTask);
            parentTask.setProgress(progress);
            tasksRepository.save(parentTask);
            return taskCreateDTO;
        }
        LOGGER.error("TaskService.createMainTask fail: parent task not fount with parentTaskId {}",
                id);
        return null;
    }


    /**
     * Calculator progress of task.
     * @param task - task need to calculate progress.
     * @return progress of task.
     */
    public int dependenciesList(Task task) {
        List<Dependency> dependencies = dependenciesRepository.findAllByTask(task);
        int progress = 0;
        for (int i = 0; i < dependencies.size(); i++) {
            progress += tasksRepository.findById(dependencies.get(i).getSubTaskId()).get().getProgress();
        }
        return (int) Math.floor(progress / (dependencies.size()));

    }

    /**
     * This function to delete task, subTasks of task
     * and update progress of parent task.
     * @param id
     * @return true/false
     */
    @Override
    public boolean deleteTaskById(final long id) {
        try {
            Map<Long, Task> tasksMap = new HashMap<>(); // save all tasks by task_id
            List<Long> taskIdDelete = new ArrayList<>();    // save list of taskId will to be is delete
            Dependency dependency;  // this dependency has id input in sub_task_id column.
            // Generate tasksMap
            tasksRepository.findAll()
                    .forEach(tasks ->
                            tasksMap.put(tasks.getId(), tasks));

            if (tasksMap.get(id) != null) {
                taskIdDelete.add(id);
                // if this isn't a subtask
                if (tasksMap.get(id).getSub() == null
                        || tasksMap.get(id).getSub() == false) {
                    this.getDeletedTaskList(id, tasksMap, taskIdDelete);
                    tasksRepository.deleteAllById(taskIdDelete);
                    return true;
                }
                // if this is a subtask
                dependency = dependenciesRepository.findBySubTaskId(id);
                // delete tasks and sub task
                dependenciesRepository.deleteById(dependency.getId());
                this.getDeletedTaskList(id, tasksMap, taskIdDelete);
                tasksRepository.deleteAllById(taskIdDelete);
                // update progress
                this.updateProgressForTaskAndParentTask(dependency.getTask().getId(), tasksMap);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}