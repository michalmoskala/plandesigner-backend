package com.example.holiday.service;

public class HolidayAlgoDTO {
    private int minutesOnHolidays;
    private int firstDay;
    private int lastDay;
    private long workerId;

    public HolidayAlgoDTO(int minutesOnHolidays, int firstDay, int lastDay, long workerId) {
        this.minutesOnHolidays = minutesOnHolidays;
        this.firstDay = firstDay;
        this.lastDay = lastDay;
        this.workerId = workerId;
    }

    public HolidayAlgoDTO(HolidayDTO holidayDTO, long workerId) {
        this.minutesOnHolidays = holidayDTO.getMinutesOnHolidays();
        this.firstDay = holidayDTO.getFirstDay();
        this.lastDay = holidayDTO.getLastDay();
        this.workerId = workerId;
    }
    public int getMinutesOnHolidays() {
        return minutesOnHolidays;
    }

    public void setMinutesOnHolidays(int minutesOnHolidays) {
        this.minutesOnHolidays = minutesOnHolidays;
    }

    public int getFirstDay() {
        return firstDay;
    }

    public void setFirstDay(int firstDay) {
        this.firstDay = firstDay;
    }

    public int getLastDay() {
        return lastDay;
    }

    public void setLastDay(int lastDay) {
        this.lastDay = lastDay;
    }

    public long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(long workerId) {
        this.workerId = workerId;
    }
}
