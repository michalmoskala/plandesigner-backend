package com.example.worker.service;

import com.example.worker.repository.WorkerEntity;

public class WorkerDTO {

    private long id;
    private String name;
    private String shortname;
    int minutes;
    int workedMinutes;
    int weekendMinutes;
    int offsetMinutes;
    int daysOnHoliday;

    public WorkerDTO(WorkerEntity workerEntity, int workedMinutes, int weekendMinutes) {
        this.id = workerEntity.getId();
        this.name = workerEntity.getName();
        this.shortname = workerEntity.getShortname();
        this.workedMinutes = workedMinutes;
        this.weekendMinutes = weekendMinutes;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }


    public int getMinutes() {
        return minutes;
    }

    public void setMinutes() {
        this.minutes = workedMinutes+offsetMinutes+daysOnHoliday*455;
    }

    public int getWorkedMinutes() {
        return workedMinutes;
    }

    public void setWorkedMinutes(int workedMinutes) {
        this.workedMinutes = workedMinutes;
    }

    public int getWeekendMinutes() {
        return weekendMinutes;
    }

    public void setWeekendMinutes(int weekendMinutes) {
        this.weekendMinutes = weekendMinutes;
    }

    public int getOffsetMinutes() {
        return offsetMinutes;
    }

    public void setOffsetMinutes(int offsetMinutes) {
        this.offsetMinutes = offsetMinutes;
    }

    public int getDaysOnHoliday() {
        return daysOnHoliday;
    }

    public void setDaysOnHoliday(int daysOnHoliday) {
        this.daysOnHoliday = daysOnHoliday;
    }
}
