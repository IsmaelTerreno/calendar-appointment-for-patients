package com.appointments.calendar.controller;


import com.appointments.calendar.repository.CalendarRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Calendar {

    private final CalendarRepository repository;

    public Calendar(CalendarRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/free-time-slots")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        repository.findAll();
        return String.format("Hello %s!", name);
    }
}
