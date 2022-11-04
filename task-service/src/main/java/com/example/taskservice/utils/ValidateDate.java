package com.example.taskservice.utils;

import java.time.LocalDate;
import java.time.Period;

/**
 * this is a function validate date
 */
public class ValidateDate {
    /**
     * function validate date
     * compare start date and end date of main task, subtask
     * @param startDate of subtask
     * @param endDate of subtask
     * @return boolean
     */
    public static boolean checkDate(String startDate, String endDate){
        LocalDate localDateStart = LocalDate.parse(startDate);
        LocalDate localDateEnd = LocalDate.parse(endDate);
        Period midTime = Period.between(localDateStart,localDateEnd);
        if(midTime.isNegative()){
            return false;
        }
        return true;
    }


}
