package com.example.taskservice.model.dto;

import com.example.taskservice.model.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseTemplate {
    private Task task;
    private Project project;
}
