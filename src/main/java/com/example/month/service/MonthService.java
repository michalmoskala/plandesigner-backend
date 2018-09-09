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

import java.util.ArrayList;
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
        List<ShiftEntity> shiftEntities1 = new ArrayList<>();
        for (ShiftEntity shiftEntity: shiftEntities)
            if(shiftEntity.getMonthId()==id)
                shiftEntities1.add(shiftEntity);

        List<SpecialDayEntity> specialDayEntities = specialDayRepository.findAll();
        List<SpecialDayEntity> specialDayEntities1 = new ArrayList<>();
        for (SpecialDayEntity specialDayEntity: specialDayEntities)
            if(specialDayEntity.getMonthId()==id)
                specialDayEntities1.add(specialDayEntity);

        return new MonthContainer(monthEntity,specialDayEntities1,shiftEntities1);
    }

    public List<MonthEntity> findAll(){
        return monthRepository.findAll();
    }

    public MonthEntity postMonth(MonthEntity monthEntity){
        return monthRepository.save(monthEntity);
    }

    public MonthEntity putMonth(MonthEntity monthEntity1, long id)
    {

        MonthEntity monthEntity = monthRepository.findById(id).get();
        monthEntity.setName(monthEntity1.getName());
        return monthRepository.save(monthEntity);
    }

    public void deleteById(long id)
    {
        monthRepository.deleteById(id);
    }






}
