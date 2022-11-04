package com.example.taskservice.utils;

import com.example.taskservice.model.Task;
import com.example.taskservice.model.dto.TaskCreateDTO;

/**
 * This class has convert function
 * to convert from DTO object to entity object.
 *
 * @author Thach. Pham - CMC Global CSC
 */
public class Converter {
    /**
     * Convert from taskCreateDTO to Task entity.
     * @param taskCreateDTO - new Task with labelId.
     * @return task converted.
     */
    public static Task taskCreateDTO2Task(TaskCreateDTO taskCreateDTO) {
        Task task = new Task();
        task.setName(taskCreateDTO.getName());
        task.setMain(taskCreateDTO.getMain());
        task.setSub(null);
        task.setDate(taskCreateDTO.getDate());
        task.setEnd(taskCreateDTO.getEnd());
        task.setProjectId(taskCreateDTO.getProjectId());
        return task;
    }
}
