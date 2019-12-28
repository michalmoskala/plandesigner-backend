package com.example.holiday.service;

import com.example.holiday.repository.HolidayEntity;

import java.util.Objects;

public class HolidayNameDTO implements Comparable<HolidayNameDTO> {
    private long id;
    private long workerId;
    private long monthId;
    private int days;
    private int firstDay;
    private int lastDay;
    private String name;

    @Override
    public int compareTo(HolidayNameDTO o) {
        long diff = o.getWorkerId()-this.workerId;
        return (int)diff;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HolidayNameDTO that = (HolidayNameDTO) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    public HolidayNameDTO(HolidayEntity holidayEntity, String workerName) {
        this.id = holidayEntity.getId();
        this.workerId = holidayEntity.getWorkerId();
        this.monthId = holidayEntity.getMonthId();
        this.days = holidayEntity.getDays();
        this.firstDay = holidayEntity.getFirstDay();
        this.lastDay = holidayEntity.getLastDay();
        this.name = workerName + " " + this.firstDay + "-" + this.lastDay;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(long workerId) {
        this.workerId = workerId;
    }

    public long getMonthId() {
        return monthId;
    }

    public void setMonthId(long monthId) {
        this.monthId = monthId;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
