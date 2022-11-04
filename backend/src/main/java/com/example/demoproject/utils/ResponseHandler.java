package com.example.demoproject.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Config response class.
 * @author Anh. Vu Tuan
 * */
@Component
public class ResponseHandler {
    /**
     * function custom response.
     * @param status http status
     * @param message message
     * @param responseObj data object
     * @return JSON object with current date, status, message and data object
     */
    public static ResponseEntity<Object> generateResponse(
            HttpStatus status, String message, Object responseObj) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map.put("timestamp", new Date());
            map.put("status", status.value());
            map.put("message", message);
            map.put("data", responseObj);
            return new ResponseEntity<Object>(map, status);
        } catch (Exception e) {
            map.clear();
            map.put("timestamp", new Date());
            map.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            map.put("message", e.getMessage());
            map.put("data", null);
            return new ResponseEntity<Object>(map, status);
        }
    }
}
