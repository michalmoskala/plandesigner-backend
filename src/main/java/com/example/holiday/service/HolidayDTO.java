package com.example.holiday.service;

public class HolidayDTO {
    private int minutesOnHolidays;
    private int firstDay;
    private int lastDay;

    public HolidayDTO(int minutesOnHolidays, int firstDay, int lastDay) {
        this.minutesOnHolidays = minutesOnHolidays;
        this.firstDay = firstDay;
        this.lastDay = lastDay;
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
}
