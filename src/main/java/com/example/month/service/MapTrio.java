package com.example.month.service;

import com.example.holiday.service.HolidayDTO;
import com.example.month.repository.MonthEntity;
import com.example.specialday.repository.SpecialDayEntity;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class MapTrio {
    private HashMap<Shift, Long> immutable;
    private LinkedHashMap<Shift, Long> mutable;
    private HashMap<Shift, Integer> minutes;
    private MonthEntity monthEntity;
    private HashMap<Long, Integer> offsets;
    private HashMap<Long, HolidayDTO> holidays;
    private List<SpecialDayEntity> specialDayEntities;

    MapTrio(HashMap<Shift, Long> immutable, LinkedHashMap<Shift, Long> mutable, HashMap<Shift, Integer> minutes, MonthEntity monthEntity, HashMap<Long, Integer> offsets, HashMap<Long, HolidayDTO> holidays, List<SpecialDayEntity> specialDayEntities) {
        this.immutable = immutable;
        this.mutable = mutable;
        this.minutes = minutes;
        this.monthEntity = monthEntity;
        this.offsets = offsets;
        this.holidays = holidays;
        this.specialDayEntities = specialDayEntities;
    }

    MapTrio(MapTrio another) {
        this.immutable= new HashMap<>();
        this.immutable.putAll(another.immutable);
        this.mutable = new LinkedHashMap<>();
        this.mutable.putAll(another.mutable);
        this.minutes = new LinkedHashMap<>();
        this.minutes.putAll(another.minutes);

        this.monthEntity = another.monthEntity;

        this.offsets = new LinkedHashMap<>();
        this.offsets.putAll(another.offsets);
        this.holidays = new LinkedHashMap<>();
        this.holidays.putAll(another.holidays);
        this.specialDayEntities = another.specialDayEntities;
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

    HashMap<Shift, Long> getImmutable() {
        return immutable;
    }

    public void setImmutable(HashMap<Shift, Long> immutable) {
        this.immutable = immutable;
    }

    LinkedHashMap<Shift, Long> getMutable() {
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

    MonthEntity getMonthEntity() {
        return monthEntity;
    }

    public void setMonthEntity(MonthEntity monthEntity) {
        this.monthEntity = monthEntity;
    }

    HashMap<Long, Integer> getOffsets() {
        return offsets;
    }

    public void setOffsets(HashMap<Long, Integer> offsets) {
        this.offsets = offsets;
    }

    HashMap<Long, HolidayDTO> getHolidays() {
        return holidays;
    }

    public void setHolidays(HashMap<Long, HolidayDTO> holidays) {
        this.holidays = holidays;
    }

    List<SpecialDayEntity> getSpecialDayEntities() {
        return specialDayEntities;
    }

    public void setSpecialDayEntities(List<SpecialDayEntity> specialDayEntities) {
        this.specialDayEntities = specialDayEntities;
    }
}
