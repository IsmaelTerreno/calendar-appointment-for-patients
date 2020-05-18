
package com.appointments.calendar.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("app.configuration.working-hours-to")
public class WorkingHoursTo {
    private String toInHour;

    public String getToInHour() {
        return toInHour;
    }

    public void setToInHour(String toInHour) {
        this.toInHour = toInHour;
    }
}
