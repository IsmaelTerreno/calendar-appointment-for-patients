
package com.appointments.calendar.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class CalendarDateUtils {

    @Value("${spring.jackson.date-format}")
    private String dateFormatConfig;
    @Value("${app.configuration.working-hours-from}")
    private String workingHoursFrom;
    @Value("${app.configuration.working-hours-to}")
    private String workingHoursTo;

    public String getDateFormatConfig() {
        return dateFormatConfig;
    }

    public String getWorkingHoursFrom() {
        return workingHoursFrom;
    }

    public String getWorkingHoursTo() {
        return workingHoursTo;
    }

    public List<LocalDateTime> generateDatesFromRangeInMinutes(Date dateFrom, long limit){
       return Stream.iterate(toLocalDateTime(dateFrom), date -> date.plusMinutes(1))
                   .limit(limit)
                   .collect(Collectors.toList());
    }
    public LocalDateTime toLocalDateTime(Date date){
        return Instant.ofEpochMilli(date.getTime())
                      .atZone(ZoneId.systemDefault())
                      .toLocalDateTime();
    }
    public Date toDateDefault(LocalDateTime date){
        return  Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
    }
    private String fillTwoZerosIn(String s){
            return String.format("%02d", Integer.parseInt(s));
        }

    public Date formatDateForCalendar(String year, String month, String day, String workingHours) throws ParseException {
        return dateFormatParse(dateFormat(year, month, day, workingHours));
    }

    public Date dateFormatParse(String dateFormat) throws ParseException {
        return new SimpleDateFormat(dateFormatConfig).parse(dateFormat);
    }
    public String dateFormat(String year, String month, String day, String workingHours){
        return String.format("%s-%s-%s %s:00", year, fillTwoZerosIn(month), fillTwoZerosIn(day), fillTwoZerosIn(workingHours));
    }
    public String[] getYearMonthDayFromDate(Date date){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.systemDefault()));
        cal.setTime(date);
        String year = String.valueOf(cal.get(Calendar.YEAR));
        String month = String.valueOf(cal.get(Calendar.MONTH) + 1);
        String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        return new String[]{year, month, day};
    }

    public Boolean isDuringWorkingHours (Date from, Date to) throws ParseException {
        String[] yearMonthDay = getYearMonthDayFromDate(from);
        final Date fromWorking = dateFormatParse(dateFormat(yearMonthDay[0], yearMonthDay[1],yearMonthDay[2], workingHoursFrom));
        final Date toWorking = dateFormatParse(dateFormat(yearMonthDay[0], yearMonthDay[1] ,yearMonthDay[2], workingHoursTo));
        long durationFrom = getDurationInMinutesFrom(fromWorking, from);
        long durationTo = getDurationInMinutesFrom(to, toWorking);
        Boolean isFromValid = durationFrom >= 0;
        Boolean isToValid = durationTo >= 0;
        return isFromValid && isToValid;
    }

    public long getDurationInMinutesFrom(Date from, Date to){
        return ChronoUnit.MINUTES.between(
                        toLocalDateTime(from),
                        toLocalDateTime(to));
    }

    public Boolean isValidDurationInMinutes(long durationInMinutes) {
        return (durationInMinutes == 15 || durationInMinutes == 60);
    }
}
