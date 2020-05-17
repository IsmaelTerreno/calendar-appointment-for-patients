package com.appointments.calendar.repository;

import com.appointments.calendar.model.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, String> {
    Calendar findByName(String name);
    List<Calendar> findByNameIsContaining(String name);
}
