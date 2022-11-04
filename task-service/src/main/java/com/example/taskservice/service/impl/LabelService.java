package com.example.taskservice.service.impl;

import com.example.taskservice.model.Label;
import com.example.taskservice.repository.IDependenciesRepository;
import com.example.taskservice.repository.ILabelRepository;
import com.example.taskservice.service.ILabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This is label service class
 * implements task service interface (DI)
 *
 * @author Hieu. Ho Nhat - CMC Global CSC
 * @author Anh. Vu Tuan - CMC Global CSC
 * @author Minh. Ho Si Tuan - CMC Global CSC
 * @author Thach. Pham - CMC Global CSC
 * */
@Service
public class LabelService implements ILabelService {

    /**
     * inject labelRepository.
     */
    @Autowired
    private ILabelRepository labelRepository;


    /**
     * Retrieve List of all label.
     * @return listLabel
     */
    @Override
    public List<Label> findAll() {
        return labelRepository.findAll();
    }

    @Override
    public Label findById(long id) {
        return labelRepository.findById(id).orElse(null);
    }
}
