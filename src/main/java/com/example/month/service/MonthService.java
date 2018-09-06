package com.example.month.service;

import com.example.month.MonthContainer;
import com.example.month.repository.MonthEntity;
import com.example.month.repository.MonthRepository;
import com.example.shift.repository.ShiftEntity;
import com.example.shift.repository.ShiftRepository;
import com.example.specialday.repository.SpecialDayEntity;
import com.example.specialday.repository.SpecialDayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonthService {

    @Autowired
    private MonthRepository monthRepository;

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private SpecialDayRepository specialDayRepository;


    public MonthContainer findById(long id){
        MonthEntity monthEntity = monthRepository.findById(id).get();
        //todo concurrent?
        List<ShiftEntity> shiftEntities = shiftRepository.findAll();
        for (ShiftEntity shiftEntity: shiftEntities)
            if(shiftEntity.getMonthId()!=id)
                shiftEntities.remove(shiftEntity);

        List<SpecialDayEntity> specialDayEntities = specialDayRepository.findAll();
        for (SpecialDayEntity specialDayEntity: specialDayEntities)
            if(specialDayEntity.getMonthId()!=id)
                specialDayEntities.remove(specialDayEntity);

        return new MonthContainer(monthEntity,specialDayEntities,shiftEntities);
    }

    public List<MonthEntity> findAll(){
        return monthRepository.findAll();
    }

    public MonthEntity postMonth(MonthEntity monthEntity){
        return monthRepository.save(monthEntity);
    }

    public MonthEntity putName(String name, long id)
    {
        MonthEntity monthEntity = monthRepository.findById(id).get();
        monthEntity.setName(name);
        return monthRepository.save(monthEntity);
    }

    public void deleteById(long id)
    {
        monthRepository.deleteById(id);
    }






}
