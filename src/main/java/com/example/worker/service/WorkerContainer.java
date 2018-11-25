package com.example.worker.service;

import com.example.worker.repository.WorkerEntity;

public class WorkerContainer {

    WorkerEntity workerEntity;
    int minutes;
    int workedMinutes;
    int weekendMinutes;
    int offsetMinutes;
    int daysOnHoliday;

    public WorkerContainer(WorkerEntity workerEntity, int workedMinutes, int weekendMinutes) {
        this.workerEntity = workerEntity;
        this.workedMinutes = workedMinutes;
        this.weekendMinutes = weekendMinutes;

    }

    public WorkerEntity getWorkerEntity() {
        return workerEntity;
    }

    public void setWorkerEntity(WorkerEntity workerEntity) {
        this.workerEntity = workerEntity;
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
