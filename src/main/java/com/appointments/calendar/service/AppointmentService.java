
package com.appointments.calendar.service;

import com.appointments.calendar.adapter.Slot;
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

    public List<Slot> findFreeTimeSlots(
      String nameCalendar,
      String year,
      String month,
      String day
    ) throws ParseException {
        List<Slot> slots = new ArrayList<>();
        Date workingFrom = calendarDateUtils.formatDateForCalendar(year, month, day, calendarDateUtils.getWorkingHoursFrom());
        Date workingTo = calendarDateUtils.formatDateForCalendar(year, month, day, calendarDateUtils.getWorkingHoursTo());
        List<Appointment> appointments = appointmentRepository.findByCalendar_NameAndDateFromIsGreaterThanEqualAndDateToIsLessThanEqual(
            nameCalendar,
            workingFrom,
            workingTo
        );
        if(appointments.size() < 1){ return slots; }
        long limitWorkHours =  (workingTo.getTime() - workingFrom.getTime()) / (60 * 60 * 1000);
        List<LocalDateTime> timeRanges = calendarDateUtils.generateDatesFromRange(workingFrom, limitWorkHours);
        slots = timeRanges.stream()
            .map(timeSlot-> new Slot(
                calendarDateUtils.toDateDefault(timeSlot),
                calendarDateUtils.toDateDefault(timeSlot.plusHours(1))
            ))
            .map(slot -> {
                List<Appointment> slotAppointments = appointments.stream().filter(appointment -> {
                    long dateFromDiff = ChronoUnit.MINUTES.between(
                            calendarDateUtils.toLocalDateTime(appointment.getDateFrom()),
                            calendarDateUtils.toLocalDateTime(slot.getDateFrom()));
                    long dateToDiff = ChronoUnit.MINUTES.between(
                            calendarDateUtils.toLocalDateTime(appointment.getDateTo()),
                            calendarDateUtils.toLocalDateTime(slot.getDateTo()));
                    return  (dateFromDiff <= 1 && dateToDiff >= 0);
                }).collect(Collectors.toList());
               slot.setAppointments(slotAppointments);
               return slot;
            })
            .collect(Collectors.toList());
        return slots;
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
