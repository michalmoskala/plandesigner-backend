package com.example.month.service;

import com.example.holiday.repository.HolidayEntity;
import com.example.month.repository.MonthEntity;
import com.example.worker.repository.WorkerEntity;

import java.util.ArrayList;
import java.util.List;

public class MonthHolidays {

    private WorkerEntity workerEntity;
    private List<HolidayEntity> holidayEntityList;

    public WorkerEntity getWorkerEntity() {
        return workerEntity;
    }

    public void setWorkerEntity(WorkerEntity workerEntity) {
        this.workerEntity = workerEntity;
    }

    public List<HolidayEntity> getHolidayEntityList() {
        return holidayEntityList;
    }

    public void setHolidayEntityList(List<HolidayEntity> holidayEntityList) {
        this.holidayEntityList = holidayEntityList;
    }

    public void addHoliday(HolidayEntity holidayEntity)
    {
        this.holidayEntityList.add(holidayEntity);
    }

    public MonthHolidays(WorkerEntity workerEntity) {
        this.workerEntity = workerEntity;
        this.holidayEntityList = new ArrayList<>();
    }



}
