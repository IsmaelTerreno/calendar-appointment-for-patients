
package com.appointments.calendar.service;

import com.appointments.calendar.model.Calendar;
import com.appointments.calendar.repository.CalendarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalendarService {
    private final CalendarRepository calendarRepository;

    public CalendarService( @Autowired CalendarRepository calendarRepository) {
        this.calendarRepository = calendarRepository;
    }

    public Calendar create(Calendar calendar){
       return calendarRepository.save(calendar);
    }

    public List<Calendar> findAllByName(String name){
       return calendarRepository.findByNameIsContaining(name);
    }

    public Calendar findByName (String name){
       return calendarRepository.findByName(name);
    }
}
