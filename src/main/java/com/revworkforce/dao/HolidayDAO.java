package com.revworkforce.dao;

import java.util.List;

import com.revworkforce.entity.Holiday;

public interface HolidayDAO {
    boolean addHoliday(Holiday holiday);

    List<Holiday> getAllHolidays();
}
