package com.example.shift.service;

import com.example.shift.repository.ShiftEntity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class ShiftDTO {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long workerId;
    private long monthId;
    private int day;
    private int whichTime;//1-4
    private int minutes;
    private String workerShortName;
    private Boolean specialColor;

    public ShiftDTO() {
        super();
    }

    public ShiftDTO(ShiftEntity shift, String workerShortName) {
        this.id = shift.getId();
        this.workerId = shift.getWorkerId();
        this.monthId = shift.getMonthId();
        this.day = shift.getDay();
        this.whichTime = shift.getWhichTime();
        this.minutes = shift.getMinutes();
        this.specialColor = this.minutes == 300;
        this.workerShortName=workerShortName;
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

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getWhichTime() {
        return whichTime;
    }

    public void setWhichTime(int whichTime) {
        this.whichTime = whichTime;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public String getWorkerShortName() {
        return workerShortName;
    }

    public void setWorkerShortName(String workerShortName) {
        this.workerShortName = workerShortName;
    }

    public Boolean getSpecialColor() {
        return specialColor;
    }

    public void setSpecialColor(Boolean specialColor) {
        this.specialColor = specialColor;
    }
}
