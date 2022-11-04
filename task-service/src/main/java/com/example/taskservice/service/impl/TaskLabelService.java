package com.example.taskservice.service.impl;


import com.example.taskservice.model.TaskLabel;
import com.example.taskservice.repository.ITaskLableRepository;
import com.example.taskservice.service.ITaskLabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskLabelService implements ITaskLabelService {
    @Autowired
    private ITaskLableRepository taskLableRepository;
    @Override
    public List<TaskLabel> findAllTaskLabel() {
        return taskLableRepository.findAll();
    }
}
