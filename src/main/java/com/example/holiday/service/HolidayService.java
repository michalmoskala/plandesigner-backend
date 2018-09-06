package com.example.holiday.service;

import com.example.holiday.repository.HolidayEntity;
import com.example.holiday.repository.HolidayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HolidayService {

    @Autowired
    private HolidayRepository holidayRepository;

    public HolidayEntity put(long monthId, HolidayEntity holidayEntity)
    {

        //todo zamienic na sqlke
        List<HolidayEntity> list = holidayRepository.findAll();
        for (HolidayEntity holidayEntity1 : list)
        {
            if (monthId==holidayEntity1.getMonthId() &&
                    holidayEntity.getWorkerId()==holidayEntity1.getWorkerId())
            {
                holidayEntity1.setDays(holidayEntity.getDays());
                holidayRepository.save(holidayEntity1);
                return holidayEntity1;
            }
        }
        holidayEntity.setMonthId(monthId);
        holidayRepository.save(holidayEntity);
        return holidayEntity;



    }
}
