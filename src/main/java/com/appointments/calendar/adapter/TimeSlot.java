package com.appointments.calendar.adapter;

import com.appointments.calendar.model.Appointment;

import java.util.Date;
import java.util.List;

public class TimeSlot {
    private Date dateFrom;
    private Date dateTo;
    private List<Appointment> appointments;

    public TimeSlot(Date dateFrom, Date dateTo) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }
}
