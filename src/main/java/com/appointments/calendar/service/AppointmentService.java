
package com.appointments.calendar.service;

import com.appointments.calendar.adapter.Slot;
import com.appointments.calendar.model.Appointment;
import com.appointments.calendar.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    @Value("${spring.jackson.date-format}")
    private String dateFormatConfig;
    @Value("${app.configuration.working-hours-from}")
    private String workingHoursFrom;
    @Value("${app.configuration.working-hours-to}")
    private String workingHoursTo;

    public AppointmentService(@Autowired  AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<Slot> findFreeTimeSlots(
      String nameCalendar,
      String year,
      String month,
      String day
    ) throws ParseException {
      List<Appointment> appointments = appointmentRepository.findByCalendar_NameAndDateFromIsGreaterThanAndDateToIsLessThan(
              nameCalendar,
              formatDateSearch(year, month, day, workingHoursFrom),
              formatDateSearch(year, month, day, workingHoursTo)
      );
      List<Slot> slots = new ArrayList<>();

      return slots;
    }

    public Appointment create(Appointment appointment){
        return appointmentRepository.save(appointment);
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
