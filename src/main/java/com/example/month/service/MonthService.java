package com.example.month.service;

import com.example.holiday.repository.HolidayEntity;
import com.example.holiday.repository.HolidayRepository;
import com.example.month.*;
import com.example.month.repository.MonthEntity;
import com.example.month.repository.MonthRepository;
import com.example.offset.repository.OffsetEntity;
import com.example.offset.repository.OffsetRepository;
import com.example.shift.repository.ShiftEntity;
import com.example.shift.repository.ShiftRepository;
import com.example.shift.service.ShiftDTO;
import com.example.specialday.repository.SpecialDayEntity;
import com.example.specialday.repository.SpecialDayRepository;
import com.example.worker.repository.WorkerEntity;
import com.example.worker.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private OffsetRepository offsetRepository;

    @Autowired
    private HolidayRepository holidayRepository;


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


    private int findOffsetMinutesForWorker(WorkerEntity worker, List<OffsetEntity> offsetEntities, long monthId)
    {
        for (OffsetEntity offsetEntity:offsetEntities)
        {
            if(offsetEntity.getMonthId() == monthId && offsetEntity.getWorkerId() == worker.getId())
                return offsetEntity.getMinutes();
        }
        return 0;
    }

    private int findDaysOnHolidayForWorker(WorkerEntity worker, List<HolidayEntity> holidayEntities, long monthId)
    {
        for (HolidayEntity holidayEntity:holidayEntities)
        {
            if(holidayEntity.getMonthId() == monthId && holidayEntity.getWorkerId() == worker.getId())
                return holidayEntity.getDays();
        }
        return 0;
    }

    public int getSumOfPenalties(long monthId)
    {
        MapTrio mapTrio = createMonthMap(monthId, new ArrayList<>());
        int penalty=0;

        penalty += getMonthlyPenalty(mapTrio.getImmutable(), mapTrio.getMutable(), mapTrio.getMinutes(), mapTrio.getOffsets(), mapTrio.getHolidays());
        penalty += getSundaysPenalty(mapTrio.getImmutable(), mapTrio.getMutable(), mapTrio.getMonthEntity().getStartingDay());
        penalty += getWeeklyPenalty(mapTrio.getImmutable(), mapTrio.getMutable(), mapTrio.getMinutes());
        penalty += getNightsInARowAndDayAfterNightAndSameDayWorkPenalty(mapTrio.getImmutable(), mapTrio.getMutable(), mapTrio.getMonthEntity().getDays());//todo length

        return penalty;
    }

    private MapTrio createMonthMap(long monthId, ArrayList<Shift> listOfImmutableEmptyShifts) {
        HashMap<Shift, Long> immutable = new HashMap<>();
        HashMap<Shift, Long> mutable = new HashMap<>();
        HashMap<Shift, Integer> minutes = new HashMap<>();
        HashMap<Long, Integer> offsets = new HashMap<>();
        HashMap<Long, Integer> holidays = new HashMap<>();

        List<OffsetEntity> offsetEntities = offsetRepository.findAll();
        List<HolidayEntity> holidayEntities = holidayRepository.findAll();
        List<WorkerEntity> workerEntities = workerRepository.findAll();
        for (WorkerEntity workerEntity: workerEntities){
            offsets.put(workerEntity.getId(), findDaysOnHolidayForWorker(workerEntity,holidayEntities,monthId)*455);
            holidays.put(workerEntity.getId(), findOffsetMinutesForWorker(workerEntity,offsetEntities,monthId));
        }

        MonthEntity monthEntity = monthRepository.findById(monthId).get();
        ArrayList<DayEntity> dayEntities = fillDayEntities(monthEntity);

        for (DayEntity dayEntity : dayEntities) {
            if (dayEntity.getShiftOne() != null) {
                immutable.put(new Shift(dayEntity.getShiftOne()), dayEntity.getShiftOne().getWorkerId());
                minutes.put(new Shift(dayEntity.getShiftOne()),dayEntity.getShiftOne().getMinutes());
            }
            else if (listOfImmutableEmptyShifts.contains(new Shift(dayEntity.getNumber(), 1))) {
                immutable.put(new Shift(dayEntity.getNumber(), 1), null);
                minutes.put(new Shift(dayEntity.getNumber(), 1),455);
            }
            else {
                mutable.put(new Shift(dayEntity.getNumber(), 1), null);
                minutes.put(new Shift(dayEntity.getNumber(), 1),455);
            }

            if (dayEntity.getShiftTwo() != null) {
                immutable.put(new Shift(dayEntity.getShiftTwo()), dayEntity.getShiftTwo().getWorkerId());
                minutes.put(new Shift(dayEntity.getShiftTwo()), dayEntity.getShiftTwo().getMinutes());
            }
            else if (listOfImmutableEmptyShifts.contains(new Shift(dayEntity.getNumber(), 2))) {
                immutable.put(new Shift(dayEntity.getNumber(), 2), null);
                minutes.put(new Shift(dayEntity.getNumber(), 2), 720);
            }
            else {
                mutable.put(new Shift(dayEntity.getNumber(), 2), null);
                minutes.put(new Shift(dayEntity.getNumber(), 2), 720);
            }

            if (dayEntity.getShiftThree() != null) {
                immutable.put(new Shift(dayEntity.getShiftThree()), dayEntity.getShiftThree().getWorkerId());
                minutes.put(new Shift(dayEntity.getShiftThree()),dayEntity.getShiftThree().getMinutes());
            }
            else if (listOfImmutableEmptyShifts.contains(new Shift(dayEntity.getNumber(), 3))) {
                immutable.put(new Shift(dayEntity.getNumber(), 3), null);
                minutes.put(new Shift(dayEntity.getNumber(), 3),720);
            }
            else {
                mutable.put(new Shift(dayEntity.getNumber(), 3), null);
                minutes.put(new Shift(dayEntity.getNumber(), 3),720);
            }

            if (dayEntity.getShiftFour() != null) {
                immutable.put(new Shift(dayEntity.getShiftFour()), dayEntity.getShiftFour().getWorkerId());
                minutes.put(new Shift(dayEntity.getShiftFour()), dayEntity.getShiftFour().getMinutes());
            }
            else if (listOfImmutableEmptyShifts.contains(new Shift(dayEntity.getNumber(), 4))) {
                immutable.put(new Shift(dayEntity.getNumber(), 4), null);
                minutes.put(new Shift(dayEntity.getNumber(), 4),720);
            }
            else{
                mutable.put(new Shift(dayEntity.getNumber(), 4), null);
                minutes.put(new Shift(dayEntity.getNumber(), 4),720);
            }
        }
        return new MapTrio(immutable,mutable,minutes,monthEntity,offsets,holidays);
    }

    private int getSundaysPenalty(HashMap<Shift, Long> immutable, HashMap<Shift, Long> mutable, int startingDay) {
        HashMap<Shift, Long> allShifts = new HashMap<>();
        allShifts.putAll(immutable);
        allShifts.putAll(mutable);
//        List<WorkerEntity> workerEntities = workerRepository.findAll();

        int penalty=0;
        HashMap<Long, Integer> sundays = new HashMap<>();
//        for (WorkerEntity workerEntity : workerEntities) {
//            sundays.put(workerEntity.getId(), 0);
//        }

        for (Map.Entry<Shift, Long> mapEntry : allShifts.entrySet()) {
            //system.out.println(mapEntry.getValue());
            if (mapEntry.getValue() != null)
                sundays.put(mapEntry.getValue(),0);
//            //system.out.println("penis");
        }

        for (Map.Entry<Shift,Long> mapEntry : allShifts.entrySet()) {
            if (mapEntry.getKey().getWeekday(startingDay) == 7) {
                if (mapEntry.getValue() != null) {
                    int value = (sundays.get(mapEntry.getValue()));
                    value++;
                    sundays.put(mapEntry.getValue(), value);
                }
            }
        }

        for (Map.Entry<Long, Integer> mapEntry : sundays.entrySet()) {
            if (mapEntry.getValue() > 3)
                if (mapEntry.getValue() >4)
                penalty+=50;
                else penalty+=5;
        }
        return penalty;
    }

    private int getWeeklyPenalty(HashMap<Shift, Long> immutable, HashMap<Shift, Long> mutable, HashMap<Shift, Integer> minutesHashMap) {
        HashMap<Shift, Long> allShifts = new HashMap<>();
        allShifts.putAll(immutable);
        allShifts.putAll(mutable);
        int penalty = 0;

        HashMap<WorkerWeek, Integer> minutes = new HashMap<>();

        for (Map.Entry<Shift, Long> mapEntry : allShifts.entrySet()) {
            //system.out.println(mapEntry.getValue());
            if (mapEntry.getValue() != null)
                for (int i=0;i<5;i++)
                    minutes.put(new WorkerWeek(mapEntry.getValue(),i),0);
//            //system.out.println("penis");
        }

        for (Map.Entry<Shift, Long> mapEntry : allShifts.entrySet()) {
            if (mapEntry.getValue() != null) {
                //system.out.println(mapEntry.getValue());//czl
                //system.out.println(mapEntry.getKey().getDay() / 7);//tydz
                //system.out.println(minutes.get(new WorkerWeek(mapEntry.getValue(), mapEntry.getKey().getDay() / 7)));
                minutes.put(new WorkerWeek(mapEntry.getValue(), mapEntry.getKey().getDay() % 7), minutes.get(new WorkerWeek(mapEntry.getValue(), mapEntry.getKey().getDay() / 7)) + minutesHashMap.get(mapEntry.getKey()));
            }

        }

        for (Map.Entry<WorkerWeek, Integer> mapEntry : minutes.entrySet()) {
            if (mapEntry.getValue()>48*60)
                penalty+=(mapEntry.getValue()-48*60)/60;
        }

        return penalty;
    }

    private int getMonthlyPenalty(HashMap<Shift, Long> immutable, HashMap<Shift, Long> mutable, HashMap<Shift, Integer> minutesHashMap, HashMap<Long, Integer> offsets, HashMap<Long, Integer> holidays) {
        HashMap<Shift, Long> allShifts = new HashMap<>();
        allShifts.putAll(immutable);
        allShifts.putAll(mutable);

        HashMap<Long, Integer> minutesForWorker = new HashMap<>();

        for (Map.Entry<Shift, Long> mapEntry : allShifts.entrySet()) {
            //system.out.println(mapEntry.getValue());
            if (mapEntry.getValue() != null)
                minutesForWorker.put(mapEntry.getValue(),offsets.get(mapEntry.getValue())+holidays.get(mapEntry.getValue()));
//            //system.out.println("penis");
        }

        for (Map.Entry<Shift, Long> mapEntry : allShifts.entrySet()) {
            if (mapEntry.getValue() != null){
                minutesForWorker.put(mapEntry.getValue(),minutesForWorker.get(mapEntry.getValue()) + minutesHashMap.get(mapEntry.getKey()));
            }
        }

        int most=0, least = 10000;
        for (Map.Entry<Long, Integer> mapEntry : minutesForWorker.entrySet()) {
            if (mapEntry.getValue()>most)
                    most=mapEntry.getValue();
            if (mapEntry.getValue()<least)
                least=mapEntry.getValue();
        }

        return (most-least)/60;
    }

    private int getNightsInARowAndDayAfterNightAndSameDayWorkPenalty(HashMap<Shift, Long> immutable, HashMap<Shift, Long> mutable, int length) {
        HashMap<Shift, Long> allShifts = new HashMap<>();
        allShifts.putAll(immutable);
        allShifts.putAll(mutable);
        int penalty = 0;

        for(int i=1;i<=length-2;i++)
        {
            if(allShifts.get(new Shift(i,4))==allShifts.get(new Shift(i+1,4)))
                penalty+=5;
            if(allShifts.get(new Shift(i,4))==allShifts.get(new Shift(i+1,4)) && allShifts.get(new Shift(i,4))==allShifts.get(new Shift(i+2,4)))
                penalty+=495;
            if(allShifts.get(new Shift(i,4))==allShifts.get(new Shift(i+1,1)))
                penalty+=1000;
            if(     allShifts.get(new Shift(i,1))==allShifts.get(new Shift(i,2)) ||
                    allShifts.get(new Shift(i,1))==allShifts.get(new Shift(i,3)) ||
                    allShifts.get(new Shift(i,1))==allShifts.get(new Shift(i,4)) ||
                    allShifts.get(new Shift(i,2))==allShifts.get(new Shift(i,3)) ||
                    allShifts.get(new Shift(i,2))==allShifts.get(new Shift(i,4)) ||
                    allShifts.get(new Shift(i,3))==allShifts.get(new Shift(i,4)))
                penalty+=2000;

        }
        if(allShifts.get(new Shift(length-1,4))==allShifts.get(new Shift(length,4)))
            penalty+=5;
        if(allShifts.get(new Shift(length-1,4))==allShifts.get(new Shift(length,1)))
            penalty+=1000;

        if(     allShifts.get(new Shift(length-1,1))==allShifts.get(new Shift(length-1,2)) ||
                allShifts.get(new Shift(length-1,1))==allShifts.get(new Shift(length-1,3)) ||
                allShifts.get(new Shift(length-1,1))==allShifts.get(new Shift(length-1,4)) ||
                allShifts.get(new Shift(length-1,2))==allShifts.get(new Shift(length-1,3)) ||
                allShifts.get(new Shift(length-1,2))==allShifts.get(new Shift(length-1,4)) ||
                allShifts.get(new Shift(length-1,3))==allShifts.get(new Shift(length-1,4)))
            penalty+=2000;

        if(     allShifts.get(new Shift(length,1))==allShifts.get(new Shift(length,2)) ||
                allShifts.get(new Shift(length,1))==allShifts.get(new Shift(length,3)) ||
                allShifts.get(new Shift(length,1))==allShifts.get(new Shift(length,4)) ||
                allShifts.get(new Shift(length,2))==allShifts.get(new Shift(length,3)) ||
                allShifts.get(new Shift(length,2))==allShifts.get(new Shift(length,4)) ||
                allShifts.get(new Shift(length,3))==allShifts.get(new Shift(length,4)))
            penalty+=2000;

        return penalty;
    }




}
