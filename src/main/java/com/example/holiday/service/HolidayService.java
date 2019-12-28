package com.example.holiday.service;

import com.example.holiday.repository.HolidayEntity;
import com.example.holiday.repository.HolidayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
                holidayEntity1.setFirstDay(holidayEntity.getFirstDay());
                holidayEntity1.setLastDay(holidayEntity.getLastDay());
                holidayRepository.save(holidayEntity1);
                return holidayEntity1;
            }
        }
        holidayEntity.setMonthId(monthId);
        holidayRepository.save(holidayEntity);
        return holidayEntity;

    }



    public void delete(long monthId, HolidayEntity holidayEntity)
    {
        holidayRepository.deleteById(holidayEntity.getId());

    }


    public HolidayEntity post(long monthId, HolidayEntity newHolidayEntity)
    {

        List<HolidayEntity> list = holidayRepository.findAll();
        List<HolidayEntity> filteredList = new ArrayList<>();
        for (HolidayEntity holidayEntity1 : list)
        {
            if (monthId==holidayEntity1.getMonthId() &&
                    newHolidayEntity.getWorkerId()==holidayEntity1.getWorkerId())
            {

                filteredList.add(holidayEntity1);

            }
        }

        for (HolidayEntity existingHolidayEntity : filteredList)
        {
            if ((newHolidayEntity.getLastDay() >= existingHolidayEntity.getFirstDay() && newHolidayEntity.getLastDay() <= existingHolidayEntity.getLastDay())
                    || (newHolidayEntity.getFirstDay() >= existingHolidayEntity.getFirstDay() && newHolidayEntity.getFirstDay() <= existingHolidayEntity.getLastDay()))
            {
                return null;
            }
        }

        newHolidayEntity.setMonthId(monthId);
        holidayRepository.save(newHolidayEntity);
        return newHolidayEntity;

    }

    public void deleteById(long id)
    {
        holidayRepository.deleteById(id);
    }



}
