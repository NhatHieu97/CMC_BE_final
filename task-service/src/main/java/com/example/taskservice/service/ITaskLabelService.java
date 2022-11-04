package com.example.taskservice.service;

import com.example.taskservice.model.TaskLabel;

import java.util.List;

public interface ITaskLabelService {
    List<TaskLabel> findAllTaskLabel();
}
