package com.example.month;

import com.example.month.repository.MonthEntity;
import com.example.shift.repository.ShiftEntity;
import com.example.specialday.repository.SpecialDayEntity;

import java.util.ArrayList;
import java.util.List;

public class MonthContainer {
    private MonthEntity monthEntity;
    private List<SpecialDayEntity> specialDayEntities;
    private List<ShiftEntity> shiftEntities;

    public MonthContainer(MonthEntity monthEntity, List<SpecialDayEntity> specialDayEntities, List<ShiftEntity> shiftEntities) {
        this.monthEntity = monthEntity;
        this.specialDayEntities = specialDayEntities;
        this.shiftEntities = shiftEntities;
    }

    public MonthEntity getMonthEntity() {
        return monthEntity;
    }

    public void setMonthEntity(MonthEntity monthEntity) {
        this.monthEntity = monthEntity;
    }

    public List<SpecialDayEntity> getSpecialDayEntities() {
        return specialDayEntities;
    }

    public void setSpecialDayEntities(ArrayList<SpecialDayEntity> specialDayEntities) {
        this.specialDayEntities = specialDayEntities;
    }

    public List<ShiftEntity> getShiftEntities() {
        return shiftEntities;
    }

    public void setShiftEntities(ArrayList<ShiftEntity> shiftEntities) {
        this.shiftEntities = shiftEntities;
    }
}
