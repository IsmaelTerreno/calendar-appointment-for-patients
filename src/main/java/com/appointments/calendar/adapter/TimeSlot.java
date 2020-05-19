package com.appointments.calendar.adapter;

import java.util.Date;

public class TimeSlot {
    private Date dateFrom;
    private Date dateTo;
    private Boolean isFreeTimeSlot;
    private String appointmentTitle;

    public TimeSlot(Date dateFrom, Date dateTo) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public String getAppointmentTitle() {
        return appointmentTitle;
    }

    public void setAppointmentTitle(String appointmentTitle) {
        this.appointmentTitle = appointmentTitle;
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

    public Boolean getFreeTimeSlot() {
        return isFreeTimeSlot;
    }

    public void setFreeTimeSlot(Boolean freeTimeSlot) {
        isFreeTimeSlot = freeTimeSlot;
    }
}
