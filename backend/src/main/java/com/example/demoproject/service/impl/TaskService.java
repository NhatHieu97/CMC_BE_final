package com.example.demoproject.service.impl;

import com.example.demoproject.model.Dependency;
import com.example.demoproject.model.Task;
import com.example.demoproject.repository.IDependenciesRepository;
import com.example.demoproject.repository.ITasksRepository;
import com.example.demoproject.service.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is task service class
 * implements task service interface (DI)
 *
 * @author Hieu. Ho Nhat - CMC Global CSC
 * @author Anh. Vu Tuan
 * @author Minh. Ho Si Tuan - CMC Global CSC
 * @author Thach. Pham - CMC Global CSC
 * */
@Service
public class TaskService implements ITaskService {

    /**
     * inject dependenciesRepository.
     */
    @Autowired
    private IDependenciesRepository dependenciesRepository;


    /**
     * inject tasksRepository.
     */
    @Autowired
    private ITasksRepository tasksRepository;

    /**
     * Retrieve Task by task id.
     * @param id - task id
     * @return Task
     */
    public Task findById(final Long id) {
        return tasksRepository.findById(id).orElse(null);
    }

    /**
     * Retrieve List of all tasks.
     * @return listTasks
     */
    public List<Task> findAll() {
        return  tasksRepository.findAll();
    }

    /**
     * List all main task and not a subtask.
     * @return List of main tasks.
     */
    @Override
    public List<Task> listMain() {
        List<Task> taskPage = tasksRepository.findAll();
        List<Task> taskList = new ArrayList<>();

        for (int i = 0; i < taskPage.size(); i++){
            if (taskPage.get(i).getSub() == null){
                taskList.add(taskPage.get(i));
            }
        }
        return taskList;
    }

    /**
     * Retrieve list subtask of mainTask.
     * @param id - mainTask id.
     * @return list of subtask.
     */
    @Override
    public List<Task> listSub(Long id) {
        // Find main task by Id
        Task task = tasksRepository.findById(id).orElse(null);
        // Find add dependencies of main task
        List<Dependency> dependencyList = dependenciesRepository.findAllByTask(task);
        List<Task> taskList = new ArrayList<>();

        for (int i = 0; i < dependencyList.size(); i++){
            // Add subtask to return list.
            taskList.add(tasksRepository.findById(dependencyList.get(i).getSubTaskId()).orElse(null));
        }
        return taskList;
    }
    /**
     * Create new main task.
     * @param task
     * @return new Task
     */
    @Override
    public Task createMainTask (Task task)
        {
           return tasksRepository.save(task);
        }

    /**
     * Update Task detail.
     * @param id - Task Id.
     * @param task - Task with new information.
     * @return task after update.
     */
    @Override
    public Task updateTask(Long id, Task task) {
        // Get Task before update.
        Task updateTask = tasksRepository.findById(id).orElse(null);
        // update information of task.
        updateTask.setName(task.getName());
        updateTask.setDate(task.getDate());
        updateTask.setEnd(task.getEnd());

        Task savedTask = null;
        //check case main is null, sub is null
        if ((updateTask.getMain() == null || !updateTask.getMain()) && updateTask.getSub() == null) {
            updateTask.setProgress(task.getProgress());
            savedTask = tasksRepository.save(updateTask);
        //check case main differ true, sub is true
        } else if ((updateTask.getMain() == null || !updateTask.getMain()) && updateTask.getSub()) {
            updateTask.setProgress(task.getProgress());
            savedTask = tasksRepository.save(updateTask);
            Task taskParent = dependenciesRepository.findBySubTaskId(updateTask.getId()).getTask();
            int progress = dependenciesList(taskParent);
            taskParent.setProgress(progress);
            savedTask = tasksRepository.save(taskParent);
        }else {
            savedTask = tasksRepository.save(updateTask);
        }
        return savedTask;
    }
    /**
     * Create subTask.
     * @param id - main task id.
     * @return Task after create.
     */
    @Override
    public Task createSubTask(Long id, Task task) {
        // Get Task before create.
        Task tasks = tasksRepository.findById(id).orElse(null);

        //set main true to main task
        tasks.setMain(true);
        task.setSub(true);
        tasksRepository.save(task);
        Dependency dependency = new Dependency(task.getId(),tasks);
        dependenciesRepository.save(dependency);
        Task mainTask = tasksRepository.findById(id).orElse(null);
        // update progress task
        int progress = dependenciesList(mainTask);
        mainTask.setProgress(progress);
        Task taskResponse = tasksRepository.save(mainTask);
        return taskResponse;
    }


    /**
     * Calculator progress of task.
     * @param task - task need to calculate progress.
     * @return progress of task.
     */
    public int dependenciesList (Task task){
        List<Dependency> dependencies = dependenciesRepository.findAllByTask(task);
        int progress = 0;
        for (int i = 0; i < dependencies.size(); i++){
            progress += tasksRepository.findById(dependencies.get(i).getSubTaskId()).get().getProgress();
        }
        return (int) Math.floor(progress / (dependencies.size()));

    }

    /**
     * Calculate value of progress.
     * @param id - id of task need to calculate progress.
     * @param taskMap - map of all task.
     * @return int - progress value of task.
     */
    public int progressCalculator(final long id,
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
     * Using recursive to get all subtask of task id (which will be delete).
     * @param id - id of task want to get subtask.
     * @param taskMap - List of all task
     * @param taskIdDelete - List of task will be delete
     */
    public void getDeletedTaskList(final long id, Map<Long, Task> taskMap,
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
     * @param taskMap - the Map of task has key is taskId and value is Task Object.
     */
    public void updateProgressForTaskAndParentTask(
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
     * This function to delete task, subTasks of task
     * and update progress of parent task.
     * @param id
     * @return true/false
     */
    @Override
    public boolean deleteTaskById(final long id) {
        try {
            Map<Long, Task> tasksMap = new HashMap<>(); // save all tasks by task_id
            List<Long> taskIdDelete = new ArrayList<>();    // save list of taskId will be delete
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