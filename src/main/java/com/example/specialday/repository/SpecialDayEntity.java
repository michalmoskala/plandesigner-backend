package com.example.specialday.repository;

import javax.persistence.*;

@Entity
@Table(name = "special_days")
public class SpecialDayEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long monthId;
    private int day;

    public SpecialDayEntity()
    {
        super();
    }

    public SpecialDayEntity(long monthId, int day) {
        this.monthId = monthId;
        this.day = day;
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
}
