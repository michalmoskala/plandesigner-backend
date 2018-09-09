package com.example.specialday.service;

import com.example.specialday.repository.SpecialDayEntity;
import com.example.specialday.repository.SpecialDayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecialDayService {

    @Autowired
    private SpecialDayRepository specialDayRepository;

    public SpecialDayEntity put(long monthId, SpecialDayEntity specialDayEntity1)
    {

        //todo zamienic na sqlke?
        List<SpecialDayEntity> list = specialDayRepository.findAll();
        for (SpecialDayEntity specialDayEntity: list)
        {
            if (specialDayEntity.getDay()==specialDayEntity1.getDay() && specialDayEntity.getMonthId()==monthId)
            {
                specialDayRepository.deleteById(specialDayEntity.getId());
                return specialDayEntity;
            }

        }
        SpecialDayEntity specialDayEntity = new SpecialDayEntity();
        specialDayEntity.setDay(specialDayEntity1.getDay());
        specialDayEntity.setMonthId(monthId);
        return specialDayRepository.save(specialDayEntity);



    }
}
