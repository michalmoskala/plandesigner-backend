package com.example.block.repository;

import javax.persistence.*;

@Entity
@Table(name = "blocks")
public class BlockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long monthId;
    private int day;
    private int whichTime;//1-4

    public BlockEntity(long id, long monthId, int day, int whichTime) {
        this.id = id;
        this.monthId = monthId;
        this.day = day;
        this.whichTime = whichTime;
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
}

