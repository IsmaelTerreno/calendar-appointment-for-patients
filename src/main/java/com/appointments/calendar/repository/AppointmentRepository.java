package com.appointments.calendar.repository;

import com.appointments.calendar.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {
    List<Appointment> findByCalendar_NameAndDateFromIsGreaterThanEqualAndDateToIsLessThanEqual(
            String nameCalendar, Date dateFrom, Date dateTo);
}
