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
            dayEntity.setWeekday(((monthEntity.getStartingDay()+i-2)%7)+1);
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


    public List<String> getMonthVerification(long monthId)
    {
        ArrayList<String> issues = new ArrayList<>();
        ArrayList<DayEntity> dayEntities = fillDayEntities(monthRepository.findById(monthId).get());
        List<WorkerEntity> workerEntities = workerRepository.findAll();
        HashMap<Long,Integer> sundays = new HashMap<>();
        for (WorkerEntity workerEntity: workerEntities) {
            sundays.put(workerEntity.getId(),0);
        }

        for(DayEntity dayEntity:dayEntities){
            if (dayEntity.getWeekday()==7) {
                if (dayEntity.getShiftOne()  != null) sundays.put(dayEntity.getShiftOne().getWorkerId(),   sundays.get(dayEntity.getShiftOne().getWorkerId())   + 1);
                if (dayEntity.getShiftTwo()  != null) sundays.put(dayEntity.getShiftTwo().getWorkerId(),   sundays.get(dayEntity.getShiftTwo().getWorkerId())   + 1);
                if (dayEntity.getShiftThree()!= null) sundays.put(dayEntity.getShiftThree().getWorkerId(), sundays.get(dayEntity.getShiftThree().getWorkerId()) + 1);
                if (dayEntity.getShiftFour() != null) sundays.put(dayEntity.getShiftFour().getWorkerId(),  sundays.get(dayEntity.getShiftFour().getWorkerId())  + 1);
            }
        }

        for (WorkerEntity workerEntity: workerEntities) {
            if (sundays.get(workerEntity.getId())>3)
                issues.add(workerEntity.getName().concat("+niedz"));
        }
        //
        HashMap<Long,Integer> minutesInAWeek = new HashMap<>();
        for (WorkerEntity workerEntity: workerEntities) {
            minutesInAWeek.put(workerEntity.getId(),0);
        }

        for(DayEntity dayEntity:dayEntities){
            if (dayEntity.getNumber()<=7) {
                if (dayEntity.getShiftOne() != null) {minutesInAWeek.put(dayEntity.getShiftOne().getWorkerId(),   minutesInAWeek.get(dayEntity.getShiftOne().getWorkerId())   + dayEntity.getShiftOne().getMinutes());}
                if (dayEntity.getShiftTwo() != null) minutesInAWeek.put(dayEntity.getShiftTwo().getWorkerId(),   minutesInAWeek.get(dayEntity.getShiftTwo().getWorkerId())   + dayEntity.getShiftTwo().getMinutes());
                if (dayEntity.getShiftThree()!= null) minutesInAWeek.put(dayEntity.getShiftThree().getWorkerId(), minutesInAWeek.get(dayEntity.getShiftThree().getWorkerId()) + dayEntity.getShiftThree().getMinutes());
                if (dayEntity.getShiftFour() != null) minutesInAWeek.put(dayEntity.getShiftFour().getWorkerId(),  minutesInAWeek.get(dayEntity.getShiftFour().getWorkerId())  + dayEntity.getShiftFour().getMinutes());
            }
        }
//
        for (WorkerEntity workerEntity: workerEntities) {
            if (minutesInAWeek.get(workerEntity.getId())>48*60)
                issues.add(workerEntity.getName().concat("+1week"));
        }

        for (WorkerEntity workerEntity: workerEntities) {
            minutesInAWeek.put(workerEntity.getId(),0);
        }

        for(DayEntity dayEntity:dayEntities){
            if (dayEntity.getNumber()>7&&dayEntity.getNumber()<=14) {
                if (dayEntity.getShiftOne()  != null) minutesInAWeek.put(dayEntity.getShiftOne().getWorkerId(),   minutesInAWeek.get(dayEntity.getShiftOne().getWorkerId())   + dayEntity.getShiftOne().getMinutes());
                if (dayEntity.getShiftTwo()  != null) minutesInAWeek.put(dayEntity.getShiftTwo().getWorkerId(),   minutesInAWeek.get(dayEntity.getShiftTwo().getWorkerId())   + dayEntity.getShiftTwo().getMinutes());
                if (dayEntity.getShiftThree()!= null) minutesInAWeek.put(dayEntity.getShiftThree().getWorkerId(), minutesInAWeek.get(dayEntity.getShiftThree().getWorkerId()) + dayEntity.getShiftThree().getMinutes());
                if (dayEntity.getShiftFour() != null) minutesInAWeek.put(dayEntity.getShiftFour().getWorkerId(),  minutesInAWeek.get(dayEntity.getShiftFour().getWorkerId())  + dayEntity.getShiftFour().getMinutes());
            }
        }

        for (WorkerEntity workerEntity: workerEntities) {
            if (minutesInAWeek.get(workerEntity.getId())>48*60)
                issues.add(workerEntity.getName().concat("+2week"));
        }

        for (WorkerEntity workerEntity: workerEntities) {
            minutesInAWeek.put(workerEntity.getId(),0);
        }

        for(DayEntity dayEntity:dayEntities){
            if (dayEntity.getNumber()>14&&dayEntity.getNumber()<=21) {
                if (dayEntity.getShiftOne() != null) {minutesInAWeek.put(dayEntity.getShiftOne().getWorkerId(),   minutesInAWeek.get(dayEntity.getShiftOne().getWorkerId())   + dayEntity.getShiftOne().getMinutes());}
                if (dayEntity.getShiftTwo() != null) minutesInAWeek.put(dayEntity.getShiftTwo().getWorkerId(),   minutesInAWeek.get(dayEntity.getShiftTwo().getWorkerId())   + dayEntity.getShiftTwo().getMinutes());
                if (dayEntity.getShiftThree()!= null) minutesInAWeek.put(dayEntity.getShiftThree().getWorkerId(), minutesInAWeek.get(dayEntity.getShiftThree().getWorkerId()) + dayEntity.getShiftThree().getMinutes());
                if (dayEntity.getShiftFour() != null) minutesInAWeek.put(dayEntity.getShiftFour().getWorkerId(),  minutesInAWeek.get(dayEntity.getShiftFour().getWorkerId())  + dayEntity.getShiftFour().getMinutes());
            }
        }
//
        for (WorkerEntity workerEntity: workerEntities) {
            if (minutesInAWeek.get(workerEntity.getId())>48*60)
                issues.add(workerEntity.getName().concat("+3week"));
        }

        for (WorkerEntity workerEntity: workerEntities) {
            minutesInAWeek.put(workerEntity.getId(),0);
        }

        for(DayEntity dayEntity:dayEntities){
            if (dayEntity.getNumber()>21&&dayEntity.getNumber()<=28) {
                if (dayEntity.getShiftOne()  != null) minutesInAWeek.put(dayEntity.getShiftOne().getWorkerId(),   minutesInAWeek.get(dayEntity.getShiftOne().getWorkerId())   + dayEntity.getShiftOne().getMinutes());
                if (dayEntity.getShiftTwo()  != null) minutesInAWeek.put(dayEntity.getShiftTwo().getWorkerId(),   minutesInAWeek.get(dayEntity.getShiftTwo().getWorkerId())   + dayEntity.getShiftTwo().getMinutes());
                if (dayEntity.getShiftThree()!= null) minutesInAWeek.put(dayEntity.getShiftThree().getWorkerId(), minutesInAWeek.get(dayEntity.getShiftThree().getWorkerId()) + dayEntity.getShiftThree().getMinutes());
                if (dayEntity.getShiftFour() != null) minutesInAWeek.put(dayEntity.getShiftFour().getWorkerId(),  minutesInAWeek.get(dayEntity.getShiftFour().getWorkerId())  + dayEntity.getShiftFour().getMinutes());
            }
        }

        for (WorkerEntity workerEntity: workerEntities) {
            if (minutesInAWeek.get(workerEntity.getId())>48*60)
                issues.add(workerEntity.getName().concat("+4week"));
        }

        return issues;



//        for (HashMap.Entry<String, String> entry : map.entrySet())

    }
}
