package com.appointments.calendar.controller;


import com.appointments.calendar.adapter.Slot;
import com.appointments.calendar.model.Appointment;
import com.appointments.calendar.model.Calendar;
import com.appointments.calendar.repository.CalendarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path ="/calendar", consumes = "application/json", produces = "application/json")
public class CalendarAPI {

    private final CalendarRepository repository;

    public CalendarAPI(@Autowired CalendarRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/free-time-slots/{name}")
    public List<Slot> findFreeTimeSlots(@PathVariable(value = "name") String name) {
        Calendar calendar = repository.findByName(name);
        List<Slot> slots = new ArrayList<>();
        if(calendar != null){
            List<Appointment> appointments = calendar.getAppointments();

        }
        return slots;
    }

    @PostMapping("/create")
    public Calendar create(@RequestBody Calendar calendar) {
        return repository.save(calendar);
    }

    @GetMapping("/find-calendar-name/{name}")
    public List<Calendar> findAll(@PathVariable(value = "name") String name) {
        return repository.findByNameIsContaining(name);
    }

    @PostMapping("{name}/appointment/create")
    public Calendar newAppointment(@PathVariable(value = "name") String name, @RequestBody Appointment appointment) {
        Calendar calendar = repository.findByName(name);
        calendar.getAppointments().add(appointment);
        return repository.save(calendar);
    }
}
