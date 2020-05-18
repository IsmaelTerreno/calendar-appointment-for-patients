package com.appointments.calendar.controller;


import com.appointments.calendar.adapter.Slot;
import com.appointments.calendar.model.Appointment;
import com.appointments.calendar.model.Calendar;
import com.appointments.calendar.repository.AppointmentRepository;
import com.appointments.calendar.repository.CalendarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    public List<Slot> findFreeTimeSlots(
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

        return slots;
    }

    @PostMapping("/create")
    public Calendar create(@RequestBody Calendar calendar) {
        return calendarRepository.save(calendar);
    }

    @GetMapping("/find-calendar-name/{name}")
    public List<Calendar> findAll(@PathVariable(value = "name") String name) {
        return calendarRepository.findByNameIsContaining(name);
    }

    @PostMapping("{name}/appointment/create")
    public Calendar newAppointment(@PathVariable(value = "name") String name, @RequestBody Appointment appointment) {
        Calendar calendar = calendarRepository.findByName(name);
        appointment.setCalendar(calendar);
        calendar.getAppointments().add(appointment);
        return calendarRepository.save(calendar);
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
