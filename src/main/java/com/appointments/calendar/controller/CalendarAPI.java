package com.appointments.calendar.controller;


import com.appointments.calendar.adapter.Slot;
import com.appointments.calendar.model.Appointment;
import com.appointments.calendar.model.Calendar;
import com.appointments.calendar.service.AppointmentService;
import com.appointments.calendar.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping(path ="/calendar", consumes = "application/json", produces = "application/json")
public class CalendarAPI {

    private final CalendarService calendarService;
    private final AppointmentService appointmentService;

    public CalendarAPI(
            @Autowired CalendarService calendarService,
            @Autowired AppointmentService appointmentService
    ) {
        this.calendarService = calendarService;
        this.appointmentService = appointmentService;
    }

    @GetMapping("{name}/free-time-slots/year/{year}/month/{month}/day/{day}")
    public ResponseEntity<List<Slot>> findFreeTimeSlots(
            @PathVariable(value = "name") String name,
            @PathVariable(value = "year") String year,
            @PathVariable(value = "month") String month,
            @PathVariable(value = "day") String day
    ) throws ParseException {
        List<Slot> slots = appointmentService.findFreeTimeSlots(name, year, month, day);
        return new ResponseEntity<>(slots, HttpStatus.FOUND);
    }

    @PostMapping("/create")
    public ResponseEntity<Calendar> create(@RequestBody Calendar calendar) {
        Calendar newCalendar = calendarService.create(calendar);
        return new ResponseEntity<>(newCalendar, HttpStatus.CREATED);
    }

    @GetMapping("/find-calendar-name/{name}")
    public ResponseEntity<List<Calendar>> findAll(@PathVariable(value = "name") String name) {
        List<Calendar> calendars = calendarService.findAllByName(name);
        return (calendars.size() > 0 ) ?
            new ResponseEntity<>(calendars, HttpStatus.FOUND):
            new ResponseEntity<>(calendars, HttpStatus.NOT_FOUND);
    }

    @PostMapping("{name}/appointment/create")
    public ResponseEntity<Appointment> newAppointment(@PathVariable(value = "name") String name, @RequestBody Appointment appointment) {
        appointmentService.create(name, appointment);
        return new ResponseEntity<>(appointment, HttpStatus.CREATED);
    }
}
