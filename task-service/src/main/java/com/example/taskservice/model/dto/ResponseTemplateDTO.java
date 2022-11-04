package com.example.taskservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseTemplateDTO {
    private TaskCreateDTO taskCreateDTO;
    private Project project;
}
