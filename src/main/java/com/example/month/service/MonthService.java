package com.example.month.service;

import com.example.month.MonthContainer;
import com.example.month.repository.MonthEntity;
import com.example.month.repository.MonthRepository;
import com.example.shift.repository.ShiftEntity;
import com.example.shift.repository.ShiftRepository;
import com.example.shift.service.ShiftDTO;
import com.example.specialday.repository.SpecialDayEntity;
import com.example.specialday.repository.SpecialDayRepository;
import com.example.worker.WorkerContainer;
import com.example.worker.repository.WorkerEntity;
import com.example.worker.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.month.DayEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class MonthService {

    @Autowired
    private MonthRepository monthRepository;

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private SpecialDayRepository specialDayRepository;

    @Autowired
    private WorkerRepository workerRepository;

    public MonthContainer getMonthContainer(long monthId){
        MonthEntity monthEntity = monthRepository.findById(monthId).get();
        return new MonthContainer(monthEntity, fillDayEntities(monthEntity));
    }

    ArrayList<DayEntity> fillDayEntities(MonthEntity monthEntity){
        ArrayList<DayEntity> dayentities = new ArrayList<>();
        int length = monthEntity.getDays();

        //todo jpql
        ArrayList<ShiftEntity> shiftEntities = filterShiftsForMonth(shiftRepository.findAll(),monthEntity.getId());

        //todo jpql
        ArrayList<SpecialDayEntity> specialDays = filterSpecialDaysForMonth(specialDayRepository.findAll(),monthEntity.getId());

        //todo jpql
        List<WorkerEntity> workerEntities = workerRepository.findAll();

        for (int i=1;i<=length;i++)
        {
            DayEntity dayEntity = new DayEntity(i);
            dayEntity.setSpecial(isSpecial(i,specialDays));
            dayEntity.setShifts(getShiftsForDay(i, shiftEntities,workerEntities));
            dayentities.add(dayEntity);
        }
        return dayentities;

    }

    private ArrayList<ShiftEntity> filterShiftsForMonth(List<ShiftEntity> shifts, long id) {
        ArrayList<ShiftEntity> shiftEntities = new ArrayList<>();
        for(ShiftEntity shiftEntity:shifts)
            if(shiftEntity.getMonthId()==id)
                shiftEntities.add(shiftEntity);
        return shiftEntities;

    }

    private ArrayList<SpecialDayEntity> filterSpecialDaysForMonth(List<SpecialDayEntity> specialDays, long id) {
        ArrayList<SpecialDayEntity> specialDayEntities = new ArrayList<>();
        for(SpecialDayEntity specialDayEntity: specialDays)
            if(specialDayEntity.getMonthId()==id)
                specialDayEntities.add(specialDayEntity);
        return specialDayEntities;

    }

    private boolean isSpecial(int day, List<SpecialDayEntity> specialDays){
        for (SpecialDayEntity specialDay: specialDays)
            if (specialDay.getDay() == day)
                return true;
        return false;
    }

    private HashMap<Integer,ShiftDTO> getShiftsForDay(int day, ArrayList<ShiftEntity> shiftEntities, List<WorkerEntity> workers)
    {
        HashMap<Integer, ShiftDTO> shifts = new HashMap<>();
        for (ShiftEntity shiftEntity : shiftEntities)
        {
            if(shiftEntity.getDay()==day)
                shifts.put(shiftEntity.getWhichTime(),new ShiftDTO(shiftEntity,getWorkerShortName(shiftEntity.getWorkerId(),workers)));


        }
        return shifts;

    }

    private String getWorkerShortName(long workerId, List<WorkerEntity> workers) {
        for (WorkerEntity workerEntity:workers)
            if (workerEntity.getId()==workerId)
                return workerEntity.getShortname();
        return null;
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
