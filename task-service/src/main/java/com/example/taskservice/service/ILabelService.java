package com.example.taskservice.service;

import com.example.taskservice.model.Label;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This is label service interface.
 *
 * @author Hieu. Ho Nhat - CMC Global CSC
 * @author Anh. Vu Tuan
 * @author Minh. Ho Si Tuan - CMC Global CSC
 * @author Thach. Pham - CMC Global CSC
 * */
@Service
public interface ILabelService {
    /**
     * Retrieve all label.
     * @return List of all label.
     */
    List<Label> findAll();

    Label findById(long id);
}
