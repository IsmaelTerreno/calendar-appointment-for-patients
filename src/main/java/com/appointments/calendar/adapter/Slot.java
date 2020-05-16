package com.appointments.calendar.adapter;

import java.util.Date;

public class Slot {
    private Date date;
    private Boolean confirmed;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }
}
