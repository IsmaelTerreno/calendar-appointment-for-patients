
package com.appointments.calendar.service;

import com.appointments.calendar.adapter.TimeSlot;
import com.appointments.calendar.model.Appointment;
import com.appointments.calendar.model.Calendar;
import com.appointments.calendar.repository.AppointmentRepository;
import com.appointments.calendar.repository.CalendarRepository;
import com.appointments.calendar.utils.CalendarDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final CalendarRepository calendarRepository;
    private final CalendarDateUtils calendarDateUtils;


    public AppointmentService(
            @Autowired AppointmentRepository appointmentRepository,
            @Autowired CalendarRepository calendarRepository,
            @Autowired CalendarDateUtils calendarDateUtils) {
        this.appointmentRepository = appointmentRepository;
        this.calendarRepository = calendarRepository;
        this.calendarDateUtils = calendarDateUtils;
    }

    public List<TimeSlot> findFreeTimeSlots(
      String nameCalendar,
      String year,
      String month,
      String day
    ) throws ParseException {
        List<TimeSlot> timeSlots = new ArrayList<>();
        Date workingFrom = calendarDateUtils.formatDateForCalendar(year, month, day, calendarDateUtils.getWorkingHoursFrom());
        Date workingTo = calendarDateUtils.formatDateForCalendar(year, month, day, calendarDateUtils.getWorkingHoursTo());
        List<Appointment> appointments = appointmentRepository.findByCalendar_NameAndDateFromIsGreaterThanEqualAndDateToIsLessThanEqual(
            nameCalendar,
            workingFrom,
            workingTo
        );
        if(appointments.size() < 1){ return timeSlots; }
        long limitWorkHours =  (workingTo.getTime() - workingFrom.getTime()) / (60 * 60 * 1000);
        List<LocalDateTime> timeRanges = calendarDateUtils.generateDatesFromRange(workingFrom, limitWorkHours);
        timeSlots = timeRanges.stream()
            .map(timeSlot-> new TimeSlot(
                calendarDateUtils.toDateDefault(timeSlot),
                calendarDateUtils.toDateDefault(timeSlot.plusHours(1))
            ))
            .map(timeSlot -> {
                List<Appointment> slotAppointments = appointments.stream().filter(appointment -> {
                    long dateFromDiff = ChronoUnit.MINUTES.between(
                            calendarDateUtils.toLocalDateTime(appointment.getDateFrom()),
                            calendarDateUtils.toLocalDateTime(timeSlot.getDateFrom()));
                    long dateToDiff = ChronoUnit.MINUTES.between(
                            calendarDateUtils.toLocalDateTime(appointment.getDateTo()),
                            calendarDateUtils.toLocalDateTime(timeSlot.getDateTo()));
                    return  (dateFromDiff <= 0 && dateToDiff >= 0);
                }).collect(Collectors.toList());
               timeSlot.setAppointments(slotAppointments);
               return timeSlot;
            })
            .collect(Collectors.toList());
        return timeSlots;
    }

    public Appointment create(String nameCalendar, Appointment appointment) throws ParseException {
        Calendar calendar = calendarRepository.findByName(nameCalendar);
        appointment.setCalendar(calendar);
        if(calendar == null) {return null;}
        final long durationInMinutes = calendarDateUtils.getDurationInMinutesFrom(appointment.getDateFrom(),appointment.getDateTo());
        if(
                calendarDateUtils.isValidDurationInMinutes(durationInMinutes) &&
                calendarDateUtils.isDuringWorkingHours(appointment.getDateFrom(), appointment.getDateTo())
        ){
            Integer appointmentsFound = appointmentRepository.countAllByCalendar_NameAndDateFromIsGreaterThanEqualAndDateToIsLessThanEqual(
                nameCalendar,
                appointment.getDateFrom(),
                appointment.getDateTo()
            );
            if(appointmentsFound < 1){
                return appointmentRepository.save(appointment);
            }
        }
        return null;
    }

}
