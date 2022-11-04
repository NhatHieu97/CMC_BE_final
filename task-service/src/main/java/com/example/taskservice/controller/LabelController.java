package com.example.taskservice.controller;

import com.example.taskservice.model.Label;
import com.example.taskservice.model.Task;
import com.example.taskservice.model.TaskLabel;
import com.example.taskservice.service.ILabelService;
import com.example.taskservice.service.ITaskLabelService;
import com.example.taskservice.utils.Messages;
import com.example.taskservice.utils.ResponseHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This is controller class for task object.
 * @author Hieu. Ho Nhat - CMC Global CSC
 * @author Anh. Vu Tuan
 * @author Minh. Ho Si Tuan - CMC Global CSC
 * @author Thach. Pham - CMC Global CSC
 * */
@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/label")
public class LabelController {

    /**
     * inject labelService.
     */
    @Autowired
    private ILabelService labelService;

    /**
     * inject responseHandler.
     */
    private ResponseHandler responseHandler;

    @Autowired
    private ITaskLabelService taskLabelService;

    /**
     * inject logging service.
     */
    private static final Logger LOGGER =
            LogManager.getLogger(LabelController.class);
    /**
     * {@code GET  /api/v1/label} : get list of label.
     *
     * @return list Label.
     */
    @GetMapping("")
    public ResponseEntity<List<Label>> findAll() {
        LOGGER.info("Retrieve all Label");
        return new ResponseEntity<>(labelService.findAll(), HttpStatus.OK);
    }
    @GetMapping("{id}")
    private ResponseEntity<Object> getDetailLabel(@PathVariable final long id) {
        LOGGER.info("BEGIN Get detail task (id={})", id);
        Label label = labelService.findById(id);
        if (label == null) {
            return responseHandler.generateResponse(
                    HttpStatus.BAD_REQUEST, Messages.DATA_NOT_FOUND, null);
        }
        return responseHandler.generateResponse(
                HttpStatus.OK, Messages.DATA_LOAD_SUCCESS, label);
    }

    @GetMapping("idLabel/{id}")
    public ResponseEntity<List<Label>> getListIdLabel(@PathVariable Long id){
        List<TaskLabel> taskLabels = taskLabelService.findAllTaskLabel();
        List<Label> labelList = new ArrayList<>();
        for (int i = 0; i < taskLabels.size(); i++){
            if(taskLabels.get(i).getTask().getId() == id){
                labelList.add(taskLabels.get(i).getLabel());
            }

        }

        if(labelList.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(labelList,HttpStatus.OK);
    }

}
