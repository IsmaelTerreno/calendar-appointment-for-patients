package com.appointments.calendar;

import com.appointments.calendar.configuration.WorkingHoursFrom;
import com.appointments.calendar.configuration.WorkingHoursTo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.TimeZone;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties({WorkingHoursTo.class, WorkingHoursFrom.class})
@Configuration
public class CalendarApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalendarApplication.class, args);
	}

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperCustomization() {
	    return jacksonObjectMapperBuilder ->
	        jacksonObjectMapperBuilder.timeZone(TimeZone.getDefault());
	}
}
