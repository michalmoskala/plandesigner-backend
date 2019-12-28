package com.example.month.service;

import com.example.shift.service.ShiftDTO;

import java.util.HashMap;
import java.util.List;

public class DayEntity{
    int number;
    boolean isSpecial;
    ShiftDTO shiftOne;
    ShiftDTO shiftTwo;
    ShiftDTO shiftThree;
    ShiftDTO shiftFour;
    int weekday;
    String workersOnHoliday;

    public String getWorkersOnHoliday() {
        return workersOnHoliday;
    }

    public void setWorkersOnHoliday(List<Integer> workersOnHoliday) {
        if (workersOnHoliday.isEmpty())
        {
            this.workersOnHoliday = ".";
        }
        else {
            StringBuilder a = new StringBuilder();
            for (Integer worker : workersOnHoliday) {
                a.append(worker.toString());
                a.append(" ");
            }
            a.deleteCharAt(a.length()-1);
            this.workersOnHoliday = a.toString();
        }
    }

    public int getWeekday() {
        return weekday;
    }

    public void setWeekday(int weekday) {
        this.weekday = weekday;
    }

    public DayEntity(int i) {
        this.number=i;
    }

    public ShiftDTO getShiftOne() {
        return shiftOne;
    }

    public void setShiftOne(ShiftDTO shiftOne) {
        this.shiftOne = shiftOne;
    }

    public ShiftDTO getShiftTwo() {
        return shiftTwo;
    }

    public void setShiftTwo(ShiftDTO shiftTwo) {
        this.shiftTwo = shiftTwo;
    }

    public ShiftDTO getShiftThree() {
        return shiftThree;
    }

    public void setShiftThree(ShiftDTO shiftThree) {
        this.shiftThree = shiftThree;
    }

    public ShiftDTO getShiftFour() {
        return shiftFour;
    }

    public void setShiftFour(ShiftDTO shiftFour) {
        this.shiftFour = shiftFour;
    }

    public void setShifts(HashMap<Integer, ShiftDTO> shifts) {
        this.shiftOne = shifts.get(1);
        this.shiftTwo = shifts.get(2);
        this.shiftThree = shifts.get(3);
        this.shiftFour = shifts.get(4);

    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isSpecial() {
        return isSpecial;
    }

    public void setSpecial(boolean special) {
        isSpecial = special;
    }


}
