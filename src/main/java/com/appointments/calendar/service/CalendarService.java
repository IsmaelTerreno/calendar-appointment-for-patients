
package com.appointments.calendar.service;

import com.appointments.calendar.model.Calendar;
import com.appointments.calendar.repository.CalendarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CalendarService {
    private final CalendarRepository calendarRepository;

    public CalendarService( @Autowired CalendarRepository calendarRepository) {
        this.calendarRepository = calendarRepository;
    }

    public Calendar create(Calendar calendar){
        Calendar calendarFound = calendarRepository.findByName(calendar.getName());
        if(calendarFound != null){ throw new ResponseStatusException( HttpStatus.CONFLICT, "Calendar name already exist, use another unique name.");}
        calendar = calendarRepository.save(calendar);
       return calendar;
    }

    public List<Calendar> findAllByName(String name){
       return calendarRepository.findByNameIsContaining(name);
    }

    public Calendar findByName (String name){
       return calendarRepository.findByName(name);
    }
}
