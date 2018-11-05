package com.example.month;

import com.example.month.repository.MonthEntity;

import java.util.HashMap;

public class MapTrio {
    HashMap<Shift, Long> immutable;
    HashMap<Shift, Long> mutable;
    HashMap<Shift, Integer> minutes;
    MonthEntity monthEntity;
    HashMap<Long, Integer> offsets;
    HashMap<Long, Integer> holidays;


    public MapTrio(HashMap<Shift, Long> immutable, HashMap<Shift, Long> mutable, HashMap<Shift, Integer> minutes, MonthEntity monthEntity, HashMap<Long, Integer> offsets, HashMap<Long, Integer> holidays) {
        this.immutable = immutable;
        this.mutable = mutable;
        this.minutes = minutes;
        this.monthEntity = monthEntity;
        this.offsets = offsets;
        this.holidays = holidays;
    }

    public HashMap<Shift, Long> getImmutable() {
        return immutable;
    }

    public void setImmutable(HashMap<Shift, Long> immutable) {
        this.immutable = immutable;
    }

    public HashMap<Shift, Long> getMutable() {
        return mutable;
    }

    public void setMutable(HashMap<Shift, Long> mutable) {
        this.mutable = mutable;
    }

    public HashMap<Shift, Integer> getMinutes() {
        return minutes;
    }

    public void setMinutes(HashMap<Shift, Integer> minutes) {
        this.minutes = minutes;
    }

    public MonthEntity getMonthEntity() {
        return monthEntity;
    }

    public void setMonthEntity(MonthEntity monthEntity) {
        this.monthEntity = monthEntity;
    }

    public HashMap<Long, Integer> getOffsets() {
        return offsets;
    }

    public void setOffsets(HashMap<Long, Integer> offsets) {
        this.offsets = offsets;
    }

    public HashMap<Long, Integer> getHolidays() {
        return holidays;
    }

    public void setHolidays(HashMap<Long, Integer> holidays) {
        this.holidays = holidays;
    }
}
