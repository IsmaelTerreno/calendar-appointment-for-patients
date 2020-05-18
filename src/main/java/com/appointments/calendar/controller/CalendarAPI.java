package com.appointments.calendar.controller;


import com.appointments.calendar.adapter.Slot;
import com.appointments.calendar.model.Appointment;
import com.appointments.calendar.model.Calendar;
import com.appointments.calendar.repository.AppointmentRepository;
import com.appointments.calendar.repository.CalendarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path ="/calendar", consumes = "application/json", produces = "application/json")
public class CalendarAPI {

    private final CalendarRepository calendarRepository;
    private final AppointmentRepository appointmentRepository;
    @Value("${spring.jackson.date-format}")
    private String dateFormatConfig;
    @Value("${app.configuration.working-hours-from}")
    private String workingHoursFrom;
    @Value("${app.configuration.working-hours-to}")
    private String workingHoursTo;

    public CalendarAPI(
            @Autowired CalendarRepository calendarRepository,
            @Autowired AppointmentRepository appointmentRepository
    ) {
        this.calendarRepository = calendarRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @GetMapping("{name}/free-time-slots/year/{year}/month/{month}/day/{day}")
    public ResponseEntity<List<Slot>> findFreeTimeSlots(
            @PathVariable(value = "name") String name,
            @PathVariable(value = "year") String year,
            @PathVariable(value = "month") String month,
            @PathVariable(value = "day") String day
    ) throws ParseException {
        List<Appointment> appointments = appointmentRepository.findByCalendar_NameAndDateFromIsGreaterThanAndDateToIsLessThan(
                name,
                formatDateSearch(year, month, day, workingHoursFrom),
                formatDateSearch(year, month, day, workingHoursTo)
        );
        List<Slot> slots = new ArrayList<>();

        return new ResponseEntity<>(slots, HttpStatus.FOUND);
    }

    @PostMapping("/create")
    public ResponseEntity<Calendar> create(@RequestBody Calendar calendar) {
        Calendar newCalendar = calendarRepository.save(calendar);
        return new ResponseEntity<>(newCalendar, HttpStatus.CREATED);
    }

    @GetMapping("/find-calendar-name/{name}")
    public ResponseEntity<List<Calendar>> findAll(@PathVariable(value = "name") String name) {
        List<Calendar> calendars = calendarRepository.findByNameIsContaining(name);
        return (calendars.size() > 0 ) ?
            new ResponseEntity<>(calendars, HttpStatus.FOUND):
            new ResponseEntity<>(calendars, HttpStatus.NOT_FOUND);
    }

    @PostMapping("{name}/appointment/create")
    public ResponseEntity<Appointment> newAppointment(@PathVariable(value = "name") String name, @RequestBody Appointment appointment) {
        Calendar calendar = calendarRepository.findByName(name);
        appointment.setCalendar(calendar);
        appointmentRepository.save(appointment);
        return new ResponseEntity<>(appointment, HttpStatus.CREATED);
    }

    private String fillTwoZerosIn(String s){
        return String.format("%02d", Integer.parseInt(s));
    }

    private Date formatDateSearch(String year, String month, String day, String workingHours) throws ParseException {
        String dateFormat =
            year +
            "-" +
            fillTwoZerosIn(month) +
            "-" +
            fillTwoZerosIn(day) +
            " " +
            fillTwoZerosIn(workingHours) + ":00";
        return new SimpleDateFormat(dateFormatConfig).parse(dateFormat);
    }
}
