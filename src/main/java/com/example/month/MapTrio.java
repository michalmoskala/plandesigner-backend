package com.example.month;

import com.example.holiday.service.HolidayDTO;
import com.example.month.repository.MonthEntity;
import com.example.specialday.repository.SpecialDayEntity;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class MapTrio {
    HashMap<Shift, Long> immutable;
    LinkedHashMap<Shift, Long> mutable;
    HashMap<Shift, Integer> minutes;
    MonthEntity monthEntity;
    HashMap<Long, Integer> offsets;
    HashMap<Long, HolidayDTO> holidays;
    List<SpecialDayEntity> specialDayEntities;

    public MapTrio(HashMap<Shift, Long> immutable, LinkedHashMap<Shift, Long> mutable, HashMap<Shift, Integer> minutes, MonthEntity monthEntity, HashMap<Long, Integer> offsets, HashMap<Long, HolidayDTO> holidays, List<SpecialDayEntity> specialDayEntities) {
        this.immutable = immutable;
        this.mutable = mutable;
        this.minutes = minutes;
        this.monthEntity = monthEntity;
        this.offsets = offsets;
        this.holidays = holidays;
        this.specialDayEntities = specialDayEntities;
    }

    public MapTrio(MapTrio mapTrio, LinkedHashMap<Shift, Long> mutable)
    {
        this.immutable = mapTrio.immutable;
        this.mutable = mutable;
        this.minutes = mapTrio.minutes;
        this.monthEntity = mapTrio.monthEntity;
        this.offsets = mapTrio.offsets;
        this.holidays = mapTrio.holidays;
        this.specialDayEntities = mapTrio.specialDayEntities;

    }

    public HashMap<Shift, Long> getImmutable() {
        return immutable;
    }

    public void setImmutable(HashMap<Shift, Long> immutable) {
        this.immutable = immutable;
    }

    public LinkedHashMap<Shift, Long> getMutable() {
        return mutable;
    }

    public void setMutable(LinkedHashMap<Shift, Long> mutable) {
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

    public HashMap<Long, HolidayDTO> getHolidays() {
        return holidays;
    }

    public void setHolidays(HashMap<Long, HolidayDTO> holidays) {
        this.holidays = holidays;
    }

    public List<SpecialDayEntity> getSpecialDayEntities() {
        return specialDayEntities;
    }

    public void setSpecialDayEntities(List<SpecialDayEntity> specialDayEntities) {
        this.specialDayEntities = specialDayEntities;
    }
}
