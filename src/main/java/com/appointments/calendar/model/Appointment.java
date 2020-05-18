
package com.appointments.calendar.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Entity
@Table
public class Appointment {
    @Id
    @GeneratedValue
    @org.hibernate.annotations.Type(type="org.hibernate.type.UUIDCharType")
    private UUID id;
    @Column
    @NotNull
    private Date dateFrom;
    @Column
    @NotNull
    private Date dateTo;
    @Column
    @NotEmpty
    private String title;
    @Column
    @NotEmpty
    private String detail;
    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE })
    private Patient patient;
    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE })
    private Calendar calendar;

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public UUID getId() {
        return id;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
