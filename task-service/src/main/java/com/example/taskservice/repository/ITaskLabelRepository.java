package com.example.taskservice.repository;

import com.example.taskservice.model.Label;
import com.example.taskservice.model.Task;
import com.example.taskservice.model.TaskLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This is tasklabel repository interface - extends JPA Repository
 * to support mysql query by jpa template.
 * @author Hieu. Ho Nhat - CMC Global CSC
 * @author Anh. Vu Tuan
 * @author Thach. Pham - CMC Global CSC
 * */
@Repository
public interface ITaskLabelRepository extends JpaRepository<TaskLabel, Long> {
    /**
     * Find all Label by TaskId
     * List<Label> of task.
     *
     * @param taskId - task id.
     * @return List<Label> of task has task id input.
     */
    @Query(value = "FROM Label l, TaskLabel tl  " +
            "where tl.task.id = ?1 " +
            "and l.id = tl.label.id")
    List<Label> findAllByTaskId(Long taskId);
    /**
     * Find all Task by TaskId
     * List<Task> of label.
     *
     * @param labelId - label id.
     * @return List<Task> of label has label id input.
     */
    @Query(value = "FROM Task t, TaskLabel tl  " +
            "where tl.label.id = ?1 " +
            "and t.id = tl.task.id")
    List<Task> findAllByLabelId(Long labelId);

    /**
     * Find all TaskLabel by Task.
     *
     * @param task - Task object
     * @return List<TaskLabel> of Task
     */
    List<TaskLabel> findAllByTask(Task task);
    /**
     * Find all TaskLabel by label.
     *
     * @param label - label object
     * @return List<TaskLabel> of label
     */
    List<TaskLabel> findAllByLabel(Label label);
}
