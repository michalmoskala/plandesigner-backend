package com.example.block.service;

import com.example.block.repository.BlockEntity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class BlockDTO {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long monthId;
    private int day;
    private int whichTime;//1-4
    private String workerShortName;
    private Boolean specialColor;

    public BlockDTO() {
        super();
    }

    public BlockDTO(BlockEntity block, String workerShortName) {
        this.id = block.getId();
        this.monthId = block.getMonthId();
        this.day = block.getDay();
        this.whichTime = block.getWhichTime();
        this.workerShortName = workerShortName;
        this.specialColor = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
