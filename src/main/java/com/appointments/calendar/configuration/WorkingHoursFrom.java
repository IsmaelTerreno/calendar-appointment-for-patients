
package com.appointments.calendar.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("app.configuration.working-hours-from")
public class WorkingHoursFrom {
    private String fromInHour;

    public String getFromInHour() {
        return fromInHour;
    }

    public void setFromInHour(String fromInHour) {
        this.fromInHour = fromInHour;
    }
}
