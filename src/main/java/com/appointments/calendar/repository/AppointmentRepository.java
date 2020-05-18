package com.appointments.calendar.repository;

import com.appointments.calendar.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    List<Appointment> findByCalendar_NameAndDateFromIsGreaterThanEqualAndDateToIsLessThanEqual(
            String nameCalendar, Date dateFrom, Date dateTo);
    Integer countAllByCalendar_NameAndDateFromIsGreaterThanEqualAndDateToIsLessThanEqual(
                String nameCalendar, Date dateFrom, Date dateTo);
    Long deleteAllByCalendar_NameAndDateFromIsGreaterThanEqualAndDateToIsLessThanEqual(
            String nameCalendar, Date dateFrom, Date dateTo);
}
