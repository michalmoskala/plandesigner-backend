package com.example.month.service;

import java.util.Objects;

public class WorkerWeek {
    private Long workerId;
    private int week;

    WorkerWeek(Long workerId, int week) {
        this.workerId = workerId;
        this.week = week;
    }

    public Long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Long workerId) {
        this.workerId = workerId;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkerWeek that = (WorkerWeek) o;
        return week == that.week &&
                Objects.equals(workerId, that.workerId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(workerId, week);
    }
}
