
package com.appointments.calendar.model;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table
public class Calendar {
    @Id
    @GeneratedValue
    @org.hibernate.annotations.Type(type="org.hibernate.type.UUIDCharType")
    private UUID id;
    @Column
    private String name;
    @OneToMany
    List<Appointment> appointments;

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }
}
