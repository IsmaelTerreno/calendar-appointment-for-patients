package com.appointments.calendar;

import com.appointments.calendar.configuration.WorkingHoursFrom;
import com.appointments.calendar.configuration.WorkingHoursTo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties({WorkingHoursTo.class, WorkingHoursFrom.class})
public class CalendarApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalendarApplication.class, args);
	}


}
