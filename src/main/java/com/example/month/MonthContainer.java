package com.example.month;

import com.example.month.repository.MonthEntity;
import com.example.shift.repository.ShiftEntity;
import com.example.specialday.repository.SpecialDayEntity;

import java.util.ArrayList;
import java.util.List;

public class MonthContainer {
    private MonthEntity monthEntity;
    private ArrayList<DayEntity> dayEntities;

    public MonthContainer(MonthEntity monthEntity, ArrayList<DayEntity> dayEntities) {
        this.monthEntity = monthEntity;
        this.dayEntities = dayEntities;
    }

    public MonthEntity getMonthEntity() {
        return monthEntity;
    }

    public void setMonthEntity(MonthEntity monthEntity) {
        this.monthEntity = monthEntity;
    }

    public List<DayEntity> getDayEntities() {
        return dayEntities;
    }

    public void setDayEntities(ArrayList<DayEntity> dayEntities) {
        this.dayEntities = dayEntities;
    }
}

