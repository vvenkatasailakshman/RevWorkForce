package com.revworkforce.entity;

import java.sql.Date;

public class Holiday {
    private int holidayId;
    private Date date;
    private String name;

    public Holiday() {
    }

    public Holiday(int holidayId, Date date, String name) {
        this.holidayId = holidayId;
        this.date = date;
        this.name = name;
    }

    public int getHolidayId() {
        return holidayId;
    }

    public void setHolidayId(int holidayId) {
        this.holidayId = holidayId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
