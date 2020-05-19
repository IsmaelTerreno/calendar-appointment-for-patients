
package com.appointments.calendar.service;

import com.appointments.calendar.adapter.TimeSlot;
import com.appointments.calendar.model.Appointment;
import com.appointments.calendar.model.Calendar;
import com.appointments.calendar.repository.AppointmentRepository;
import com.appointments.calendar.repository.CalendarRepository;
import com.appointments.calendar.utils.CalendarDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
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
        Calendar calendarFound = calendarRepository.findByName(nameCalendar);
        if(calendarFound == null){ throw new ResponseStatusException( HttpStatus.NOT_ACCEPTABLE, "Calendar name no exist, use another unique name to show results.");}
        List<TimeSlot> timeSlots = new ArrayList<>();
        Date workingFrom = calendarDateUtils.formatDateForCalendar(year, month, day, calendarDateUtils.getWorkingHoursFrom());
        Date workingTo = calendarDateUtils.formatDateForCalendar(year, month, day, calendarDateUtils.getWorkingHoursTo());
        List<Appointment> appointments = appointmentRepository.findByCalendar_NameAndDateFromIsGreaterThanEqualAndDateToIsLessThanEqual(
            nameCalendar,
            workingFrom,
            workingTo
        );
        if(appointments.size() < 1){ return timeSlots; }
        final long limitWorkHours = calendarDateUtils.getDurationInMinutesFrom(workingFrom,workingTo);
        List<LocalDateTime> timeRanges = calendarDateUtils.generateDatesFromRangeInMinutes(workingFrom, limitWorkHours);
        timeSlots = timeRanges.stream()
            .map(timeSlot-> new TimeSlot(
                calendarDateUtils.toDateDefault(timeSlot),
                calendarDateUtils.toDateDefault(timeSlot)
            ))
            .peek(timeSlot -> {
                checkIfFreeTimeSlot(appointments, timeSlot);
            })
            .collect(Collectors.toList());
        return timeSlots;
    }

    private void checkIfFreeTimeSlot (List<Appointment> appointments, TimeSlot timeSlot){
        timeSlot.setAppointmentTitle("");
        timeSlot.setFreeTimeSlot(true);
        appointments
            .forEach((appointment) -> {
                LocalDateTime timeSlotDateTo = calendarDateUtils.toLocalDateTime(timeSlot.getDateTo());
                LocalDateTime timeSlotDateFrom = calendarDateUtils.toLocalDateTime(timeSlot.getDateFrom());
                LocalDateTime appointmentDateTo = calendarDateUtils.toLocalDateTime(appointment.getDateTo());
                LocalDateTime appointmentDateFrom = calendarDateUtils.toLocalDateTime(appointment.getDateFrom());
                boolean isBeforeTo = timeSlotDateTo.isBefore(appointmentDateTo);
                boolean isEqualTo = timeSlotDateTo.isEqual(appointmentDateTo);
                boolean isAfterFrom = timeSlotDateFrom.isAfter(appointmentDateFrom);
                boolean isEqualFrom = timeSlotDateFrom.isEqual(appointmentDateFrom);
                if((isBeforeTo || isEqualTo) && (isAfterFrom || isEqualFrom)){
                    timeSlot.setFreeTimeSlot(false);
                    timeSlot.setAppointmentTitle(appointment.getTitle() + " - " + appointment.getPatient().getName());
                }
            });
    }

    public Appointment create(String nameCalendar, Appointment appointment) throws ParseException {
        Calendar calendar = calendarRepository.findByName(nameCalendar);
        appointment.setCalendar(calendar);
        if(calendar == null) {throw new ResponseStatusException( HttpStatus.NOT_ACCEPTABLE, "Calendar name not exist, please use and existing calendar name to create the appointment.");}
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
    @Transactional
    public Boolean deleteFromTo(String nameCalendar, Date from, Date to){
        Long affectedRows = appointmentRepository.deleteAllByCalendar_NameAndDateFromIsGreaterThanEqualAndDateToIsLessThanEqual(
           nameCalendar, from, to
        );
       return (affectedRows >= 0);
    }
    @Transactional
    public Boolean deleteById(String id){
        appointmentRepository.deleteById(UUID.fromString(id));
        return Boolean.TRUE;
    }
}
