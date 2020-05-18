
package com.appointments.calendar.service;

import com.appointments.calendar.adapter.Slot;
import com.appointments.calendar.model.Appointment;
import com.appointments.calendar.model.Calendar;
import com.appointments.calendar.repository.AppointmentRepository;
import com.appointments.calendar.repository.CalendarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final CalendarRepository calendarRepository;
    @Value("${spring.jackson.date-format}")
    private String dateFormatConfig;
    @Value("${app.configuration.working-hours-from}")
    private String workingHoursFrom;
    @Value("${app.configuration.working-hours-to}")
    private String workingHoursTo;

    public AppointmentService(
            @Autowired AppointmentRepository appointmentRepository,
            @Autowired CalendarRepository calendarRepository) {
        this.appointmentRepository = appointmentRepository;
        this.calendarRepository = calendarRepository;
    }

    public List<Slot> findFreeTimeSlots(
      String nameCalendar,
      String year,
      String month,
      String day
    ) throws ParseException {
        List<Slot> slots = new ArrayList<>();
        Date workingFrom = formatDateSearch(year, month, day, workingHoursFrom);
        Date workingTo = formatDateSearch(year, month, day, workingHoursTo);
        List<Appointment> appointments = appointmentRepository.findByCalendar_NameAndDateFromIsGreaterThanEqualAndDateToIsLessThanEqual(
            nameCalendar,
            workingFrom,
            workingTo
        );
        if(appointments.size() < 1){ return slots; }
        long limitWorkHours =  (workingTo.getTime() - workingFrom.getTime()) / (60 * 60 * 1000);
        List<LocalDateTime> timeRanges = generateDatesFromRange(workingFrom, limitWorkHours);
        slots = timeRanges.stream()
            .map(timeSlot-> new Slot(
                toDateDefault(timeSlot),
                toDateDefault(timeSlot.plusHours(1))
            ))
            .map(slot -> {
                List<Appointment> slotAppointments = appointments.stream().filter(appointment -> {
                    long dateFromDiff = ChronoUnit.MINUTES.between(toLocalDateTime(appointment.getDateFrom()), toLocalDateTime(slot.getDateFrom()));
                    long dateToDiff = ChronoUnit.MINUTES.between(toLocalDateTime(appointment.getDateTo()), toLocalDateTime(slot.getDateTo()));
                    return  (dateFromDiff < 1 && dateToDiff > 0);
                }).collect(Collectors.toList());
               slot.setAppointments(slotAppointments);
               return slot;
            })
            .collect(Collectors.toList());
        return slots;
    }

    public Appointment create(String nameCalendar,Appointment appointment){
        Calendar calendar = calendarRepository.findByName(nameCalendar);
        appointment.setCalendar(calendar);
        return appointmentRepository.save(appointment);
    }

    private List<LocalDateTime> generateDatesFromRange(Date dateFrom, long limit){
       return Stream.iterate(toLocalDateTime(dateFrom), date -> date.plusHours(1))
                   .limit(limit)
                   .collect(Collectors.toList());
    }
    private LocalDateTime toLocalDateTime(Date date){
        return Instant.ofEpochMilli(date.getTime())
                      .atZone(ZoneId.systemDefault())
                      .toLocalDateTime();
    }
    private Date toDateDefault(LocalDateTime date){
        return  Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
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
