package com.example.shift.service;

public class SimpleShiftDTO {

    private long monthId;
    private int day;
    private int whichTime;//1-4

    public SimpleShiftDTO(long monthId, int day, int whichTime) {
        this.monthId = monthId;
        this.day = day;
        this.whichTime = whichTime;
    }

    public SimpleShiftDTO() {
        super();
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
}
