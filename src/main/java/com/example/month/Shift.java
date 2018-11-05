package com.example.month;

import com.example.shift.repository.ShiftEntity;
import com.example.shift.service.ShiftDTO;

import java.util.Objects;

public class Shift {
    int day;
    int time;

    public Shift(int day, int time) {
        this.day = day;
        this.time = time;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public Shift(ShiftDTO shiftDTO) {
        this.day=shiftDTO.getDay();
        this.time=shiftDTO.getWhichTime();
    }

    public int getWeekday(int startingDay){
        return (((startingDay+day-2)%7)+1);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shift shift = (Shift) o;
        return day == shift.day &&
                time == shift.time;
    }

    @Override
    public int hashCode() {

        return Objects.hash(day, time);
    }
}

