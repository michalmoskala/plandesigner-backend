package com.example.month.service;

import com.example.block.repository.BlockEntity;
import com.example.block.repository.BlockRepository;
import com.example.holiday.repository.HolidayEntity;
import com.example.holiday.repository.HolidayRepository;
import com.example.holiday.service.HolidayAlgoDTO;
import com.example.holiday.service.HolidayDTO;
import com.example.holiday.service.HolidayNameDTO;
import com.example.month.repository.MonthEntity;
import com.example.month.repository.MonthRepository;
import com.example.offset.repository.OffsetEntity;
import com.example.offset.repository.OffsetRepository;
import com.example.shift.repository.ShiftEntity;
import com.example.shift.repository.ShiftRepository;
import com.example.shift.service.ShiftDTO;
import com.example.specialday.repository.SpecialDayEntity;
import com.example.specialday.repository.SpecialDayRepository;
import com.example.specialday.service.SpecialDayService;
import com.example.worker.repository.WorkerEntity;
import com.example.worker.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    @Autowired
    private SpecialDayService specialDayService;

    @Autowired
    private BlockRepository blockRepository;


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

        List<HolidayEntity> holidayEntities = filterHolidaysForMonth(holidayRepository.findAll(),monthEntity.getId());


        ArrayList<BlockEntity> blockEntities = filterBlocksForMonth(blockRepository.findAll(),monthEntity.getId());

        for (int i=1;i<=length;i++)
        {
            DayEntity dayEntity = new DayEntity(i);
            dayEntity.setSpecial(isSpecial(i,specialDays));
            dayEntity.setWorkersOnHoliday(getHolidaysByDay(i,holidayEntities));
            dayEntity.setShifts(getShiftsForDay(i, shiftEntities,workerEntities,blockEntities));
            dayEntity.setWeekday(((monthEntity.getStartingDay()+i-2)%7)+1);
            dayentities.add(dayEntity);
        }
        return dayentities;

    }

    private List<Integer> getHolidaysByDay(int i, List<HolidayEntity> holidayEntities) {

        ArrayList<Integer> workersOnHoliday = new ArrayList<>();
        for (HolidayEntity holidayEntity:holidayEntities){
            if (holidayEntity.checkIfInRange(i))
                workersOnHoliday.add(Integer.valueOf(workerRepository.findById(holidayEntity.getWorkerId()).get().getShortname()));
        }
        workersOnHoliday.sort(Comparator.naturalOrder());
        return workersOnHoliday;
    }

    private ArrayList<DayEntity> fillDayEntitiesForAlgo(MonthEntity monthEntity){
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
            dayEntity.setShifts(getShiftsForDayForAlgo(i, shiftEntities,workerEntities));
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

    private ArrayList<BlockEntity> filterBlocksForMonth(List<BlockEntity> blocks, long id) {
        ArrayList<BlockEntity> blockEntities = new ArrayList<>();
        for(BlockEntity blockEntity:blocks)
            if(blockEntity.getMonthId()==id)
                blockEntities.add(blockEntity);
        return blockEntities;

    }

    private ArrayList<SpecialDayEntity> filterSpecialDaysForMonth(List<SpecialDayEntity> specialDays, long id) {
        ArrayList<SpecialDayEntity> specialDayEntities = new ArrayList<>();
        for(SpecialDayEntity specialDayEntity: specialDays)
            if(specialDayEntity.getMonthId()==id)
                specialDayEntities.add(specialDayEntity);
        return specialDayEntities;

    }

    private ArrayList<HolidayEntity> filterHolidaysForMonth(List<HolidayEntity> holidays, long id) {
        ArrayList<HolidayEntity> holidayEntities = new ArrayList<>();
        for(HolidayEntity holidayEntity: holidays)
            if(holidayEntity.getMonthId()==id)
                holidayEntities.add(holidayEntity);
        return holidayEntities;

    }

    private boolean isSpecial(int day, List<SpecialDayEntity> specialDays){
        for (SpecialDayEntity specialDay: specialDays)
            if (specialDay.getDay() == day)
                return true;
        return false;
    }

    private HashMap<Integer,ShiftDTO> getShiftsForDay(int day, ArrayList<ShiftEntity> shiftEntities, List<WorkerEntity> workers, ArrayList<BlockEntity> blockEntities)
    {
        HashMap<Integer, ShiftDTO> shifts = new HashMap<>();
        for (ShiftEntity shiftEntity : shiftEntities)
        {
            if(shiftEntity.getDay()==day)
                shifts.put(shiftEntity.getWhichTime(),new ShiftDTO(shiftEntity,getWorkerShortName(shiftEntity.getWorkerId(),workers)));


        }
        for (BlockEntity blockEntity : blockEntities)
        {
            if(blockEntity.getDay()==day)
                shifts.put(blockEntity.getWhichTime(),new ShiftDTO(blockEntity));


        }

        return shifts;

    }

    private HashMap<Integer,ShiftDTO> getShiftsForDayForAlgo(int day, ArrayList<ShiftEntity> shiftEntities, List<WorkerEntity> workers)
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
        MonthEntity monthEntity1 = monthRepository.save(monthEntity);
        for(int i=1;i<monthEntity1.getDays();i++)
            if ((((monthEntity.getStartingDay()+i-2)%7)+1) == 7  || (((monthEntity.getStartingDay()+i-2)%7)+1) == 6)
                specialDayService.put(monthEntity1.getId(),new SpecialDayEntity(monthEntity1.getId(),i));
        return monthEntity1;
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

    private HolidayDTO findDaysOnHolidayForWorker(WorkerEntity worker, List<HolidayEntity> holidayEntities, long monthId)
    {
        for (HolidayEntity holidayEntity:holidayEntities)
        {
            if(holidayEntity.getMonthId() == monthId && holidayEntity.getWorkerId() == worker.getId())
                return new HolidayDTO(holidayEntity.getDays()*455,holidayEntity.getFirstDay(),holidayEntity.getLastDay());
        }
        return new HolidayDTO(0,32,0);
    }

    private Boolean raiseWorker(Map.Entry<Shift, Long> mapEntry, List<WorkerEntity> workerEntities,long firstWorker)
    {
        Boolean prev=false;
        for (WorkerEntity workerEntity:workerEntities){
            if(prev)
            {
                mapEntry.setValue(workerEntity.getId());
                return true;
            }
            if(workerEntity.getId()==mapEntry.getValue())
            {
                prev=true;
            }
        }

        mapEntry.setValue(firstWorker);
        return false;

    }

    private Boolean allAreFinal(LinkedHashMap<Shift, Long> mutable, long lastWorker) {
        for (Map.Entry<Shift, Long> mapEntry : mutable.entrySet()) {
            if (mapEntry.getValue()!=lastWorker)
                return false;
        }
        return true;
    }

    public int getSumOfPenaltiesByBruteForce(long monthId) {
        ArrayList<Shift> immutableEmpties = new ArrayList<>();

        List<BlockEntity> blockEntities = filterBlocksForMonth(blockRepository.findAll(), monthId);
        for (BlockEntity blockEntity : blockEntities) {
            immutableEmpties.add(new Shift(blockEntity.getDay(), blockEntity.getWhichTime()));
        }

        List<WorkerEntity> workerEntities = workerRepository.findAll();
        MapTrio mapTrio = createMonthMap(monthId, immutableEmpties, workerEntities);

        MapTrio bestSoFar = null;
        int lowestPenSoFar = Integer.MAX_VALUE;


        long firstWorker = 0, lastWorker = 0;
        //find lowest element
        for(WorkerEntity workerEntity: workerEntities)
        {
            firstWorker = workerEntity.getId();
            break;
        }

        //find highest element
        for(WorkerEntity workerEntity: workerEntities)
        {
            lastWorker = workerEntity.getId();
        }

        //set lowest element to all
        for(Map.Entry<Shift, Long> mapEntry:mapTrio.getMutable().entrySet()) {
            mapEntry.setValue(firstWorker);
        }

        //bruteforce
        Boolean end=false;
        while(!end){
            int pen=getPenalty(mapTrio);
            if(lowestPenSoFar>pen)
            {
                lowestPenSoFar=pen;
                bestSoFar=new MapTrio(mapTrio);
            }
            for(Map.Entry<Shift, Long> mapEntry:mapTrio.getMutable().entrySet()) {
                if (raiseWorker(mapEntry,workerEntities,firstWorker))
                    break;
            }

            if (allAreFinal(mapTrio.getMutable(),lastWorker))
                end=true;
        }


            //db
//        int i=0;
//        for(Map.Entry<Shift, Long> mapEntry:mapTrio.getMutable().entrySet()) {
//            i++;
//            if(i%20==0)
//                System.out.println(i);
//            postShift(mapEntry,mapTrio.getMinutes().get(mapEntry.getKey()),monthId);
//        }


        return lowestPenSoFar;

    }

        public int getSumOfPenalties(long monthId)
    {
        ArrayList<Shift> immutableEmpties = new ArrayList<>();

        List<BlockEntity> blockEntities = filterBlocksForMonth(blockRepository.findAll(), monthId);
        for(BlockEntity blockEntity: blockEntities) {
            immutableEmpties.add(new Shift(blockEntity.getDay(),blockEntity.getWhichTime()));
        }

        List<WorkerEntity> workerEntities = workerRepository.findAll();
        MapTrio mapTrio = createMonthMap(monthId, immutableEmpties, workerEntities);
        int penalty;
        HashMap<Long,Integer> workerToPenalty = new HashMap<>();
        Map.Entry<Long, Integer> min = null;
        int iter = 0;
        HashMap<Integer,Shift> numberToMutable = new HashMap<>();
        MapTrio bestSoFar = null;
        int bestPenSoFar;



//        for(Map.Entry<Shift, Long> mapEntry:mapTrio.getMutable().entrySet()) {
//            for (WorkerEntity workerEntity : workerEntities) {
//                mapTrio.getMutable().put(mapEntry.getKey(), workerEntity.getId());
//                workerToPenalty.put(workerEntity.getId(), getPenalty(mapTrio));
//            }
//            for(Map.Entry<Long, Integer> entry:workerToPenalty.entrySet()) {
//                if (min == null || min.getValue() > entry.getValue()) {
//                    min = entry;
//                }
//            }
//            mapTrio.getMutable().put(mapEntry.getKey(), min.getKey());
//        }


        //greedy+
        for(Map.Entry<Shift, Long> mapEntry:mapTrio.getMutable().entrySet()) {
            numberToMutable.put(iter,mapEntry.getKey());
            iter++;

        }

        for(int i = 0; i < iter/4; i++) {
            bestPenSoFar=Integer.MAX_VALUE;

            for (WorkerEntity workerEntity1 : workerEntities) {
                for (WorkerEntity workerEntity2 : workerEntities) {
                    for (WorkerEntity workerEntity3 : workerEntities) {
                        for (WorkerEntity workerEntity4 : workerEntities) {
                            mapTrio.getMutable().put(numberToMutable.get(i*4),workerEntity1.getId());
                            mapTrio.getMutable().put(numberToMutable.get((i*4)+1),workerEntity2.getId());
                            mapTrio.getMutable().put(numberToMutable.get((i*4)+2),workerEntity3.getId());
                            mapTrio.getMutable().put(numberToMutable.get((i*4)+3),workerEntity4.getId());
                            if(getPenalty(mapTrio) < bestPenSoFar) {
                                bestSoFar = new MapTrio(mapTrio);
                                bestPenSoFar=getPenalty(mapTrio);
                            }
                        }
                    }
                }
            }
            mapTrio = new MapTrio(bestSoFar);
            System.out.println(bestPenSoFar);
        }


        //greedy
        for(Map.Entry<Shift, Long> mapEntry:mapTrio.getMutable().entrySet()) {
            if(mapEntry.getValue() == null) {
                for (WorkerEntity workerEntity : workerEntities) {
                    mapTrio.getMutable().put(mapEntry.getKey(), workerEntity.getId());
                    workerToPenalty.put(workerEntity.getId(), getPenalty(mapTrio));
                }
                for (Map.Entry<Long, Integer> entry : workerToPenalty.entrySet()) {
                    if (min == null || min.getValue() > entry.getValue()) {
                        min = entry;
                    }
                }
            mapTrio.getMutable().put(mapEntry.getKey(), min.getKey());
            }
        }

         System.out.println(getPenalty(mapTrio));

        //poprawianie 2opt
        int prev=Integer.MAX_VALUE;
        LinkedHashMap<Shift, Long> newMutable;
        for (int i=0;i<100000;i++){
            for(Map.Entry<Shift, Long> mapEntry:mapTrio.getMutable().entrySet()) {
                 for (WorkerEntity workerEntity : workerEntities){
                     newMutable = new LinkedHashMap<>(mapTrio.getMutable());
                     newMutable.put(mapEntry.getKey(),workerEntity.getId());
                     if(getPenalty(new MapTrio(mapTrio,newMutable))<getPenalty(mapTrio))
                         mapTrio.setMutable(newMutable);
                 }
            }
            System.out.println(getPenalty(mapTrio));
            if (getPenalty(mapTrio)<prev)
                prev=getPenalty(mapTrio);
            else
                break;
        }

        penalty = getPenalty(mapTrio);


//        for(int i = 0; i < iter/4; i++) {
//            bestPenSoFar=Integer.MAX_VALUE;
//
//            for (WorkerEntity workerEntity1 : workerEntities) {
//                for (WorkerEntity workerEntity2 : workerEntities) {
//                    for (WorkerEntity workerEntity3 : workerEntities) {
//                        for (WorkerEntity workerEntity4 : workerEntities) {
//                            mapTrio.getMutable().put(numberToMutable.get(i*4),workerEntity1.getId());
//                            mapTrio.getMutable().put(numberToMutable.get((i*4)+1),workerEntity2.getId());
//                            mapTrio.getMutable().put(numberToMutable.get((i*4)+2),workerEntity3.getId());
//                            mapTrio.getMutable().put(numberToMutable.get((i*4)+3),workerEntity4.getId());
//                            if(getPenalty(mapTrio) < bestPenSoFar) {
//                                bestSoFar = new MapTrio(mapTrio);
//                                bestPenSoFar=getPenalty(mapTrio);
//                            }
//                        }
//                    }
//                }
//            }
//            mapTrio = new MapTrio(bestSoFar);
//            System.out.println(bestPenSoFar);
//        }
//        penalty = getPenalty(mapTrio);

//       db
        int i=0;
        for(Map.Entry<Shift, Long> mapEntry:mapTrio.getMutable().entrySet()) {
            i++;
            if(i%20==0)
            System.out.println(i);
            postShift(mapEntry,mapTrio.getMinutes().get(mapEntry.getKey()),monthId);
        }

        return getPenalty(mapTrio);
    }

    public ShiftDTO postShift(Map.Entry<Shift, Long> workerEntry, Integer minutes, long monthId)
    {
        ShiftEntity shiftEntity = new ShiftEntity();
        shiftEntity.setMinutes(minutes);
        shiftEntity.setDay(workerEntry.getKey().getDay());
        shiftEntity.setWorkerId(workerEntry.getValue());
        shiftEntity.setWhichTime(workerEntry.getKey().getTime());
        shiftEntity.setMonthId(monthId);

        //todo jpql
        List<ShiftEntity> shiftEntityList = shiftRepository.findAll();
        for (ShiftEntity shiftEntity1: shiftEntityList){
            if (shiftEntity1.getMonthId()==shiftEntity.getMonthId()&&shiftEntity1.getDay()==shiftEntity.getDay()&&shiftEntity1.getWhichTime()==shiftEntity.getWhichTime())
            {
                shiftRepository.deleteById(shiftEntity1.getId());
                break;
            }
        }

        ShiftEntity shiftEntity1 = shiftRepository.save(shiftEntity);
        return new ShiftDTO(shiftEntity1,workerRepository.findById(shiftEntity1.getWorkerId()).get().getShortname());
    }

    private int getPenalty(MapTrio mapTrio){
        int penalty = 0;
        penalty += getMonthlyPenalty(mapTrio.getImmutable(), mapTrio.getMutable(), mapTrio.getMinutes(), mapTrio.getOffsets(), mapTrio.getHolidays());
        penalty += getSundaysPenalty(mapTrio.getImmutable(), mapTrio.getMutable(), mapTrio.getMonthEntity().getStartingDay());
        penalty += getWeeklyPenalty(mapTrio.getImmutable(), mapTrio.getMutable(), mapTrio.getMinutes());
        penalty += getNightsInARowAndDayAfterNightAndSameDayAnd12sInARowPenalty(mapTrio.getImmutable(), mapTrio.getMutable(), mapTrio.getMonthEntity().getDays());
        penalty += getHolidayPenalty(mapTrio.getImmutable(),mapTrio.getMutable(),createHolidayAlgoDTO(mapTrio.getHolidays()));
        penalty += getSpecialDayPenalty(mapTrio.getImmutable(),mapTrio.getMutable(),mapTrio.getMinutes(),mapTrio.getSpecialDayEntities());
        return penalty;
    }

    private ArrayList<HolidayAlgoDTO> createHolidayAlgoDTO(HashMap<Long,HolidayDTO> holidays) {
        ArrayList<HolidayAlgoDTO> holidayList = new ArrayList<>();
        for (Map.Entry<Long,HolidayDTO> holiday : holidays.entrySet())
            holidayList.add(new HolidayAlgoDTO(holiday.getValue(),holiday.getKey()));
        return holidayList;
    }

    private MapTrio createMonthMap(long monthId, ArrayList<Shift> listOfImmutableEmptyShifts, List<WorkerEntity> workerEntities) {
        HashMap<Shift, Long> immutable = new HashMap<>();
        LinkedHashMap<Shift, Long> mutable = new LinkedHashMap<>();
        HashMap<Shift, Integer> minutes = new HashMap<>();
        HashMap<Long, Integer> offsets = new HashMap<>();
        HashMap<Long, HolidayDTO> holidays = new HashMap<>();


        List<OffsetEntity> offsetEntities = offsetRepository.findAll();
        List<HolidayEntity> holidayEntities = holidayRepository.findAll();
        List<SpecialDayEntity> specialDayEntities = filterSpecialDaysForMonth(specialDayRepository.findAll(),monthId);

        for (WorkerEntity workerEntity: workerEntities){
            holidays.put(workerEntity.getId(), findDaysOnHolidayForWorker(workerEntity,holidayEntities,monthId));
            offsets.put(workerEntity.getId(), findOffsetMinutesForWorker(workerEntity,offsetEntities,monthId));
        }

        MonthEntity monthEntity = monthRepository.findById(monthId).get();
        ArrayList<DayEntity> dayEntities = fillDayEntitiesForAlgo(monthEntity);

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
        return new MapTrio(immutable,mutable,minutes,monthEntity,offsets,holidays,specialDayEntities);
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
//            //system.out.println("eni");
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
                penalty+=1000;
                else penalty+=50;
        }
        return penalty;
    }

    private int getSpecialDayPenalty(HashMap<Shift, Long> immutable, HashMap<Shift, Long> mutable, HashMap<Shift, Integer> minutes, List<SpecialDayEntity> specialDayEntities) {
        HashMap<Shift, Long> allShifts = new HashMap<>();
        allShifts.putAll(immutable);
        allShifts.putAll(mutable);

        HashMap<Long, Integer> specialDayMinutes = new HashMap<>();
//        for (WorkerEntity workerEntity : workerEntities) {
//            sundays.put(workerEntity.getId(), 0);
//        }

        for (Map.Entry<Shift, Long> mapEntry : allShifts.entrySet()) {
            if (mapEntry.getValue() != null)
                specialDayMinutes.put(mapEntry.getValue(),0);
//            //system.out.println("eni");
        }

        for(SpecialDayEntity specialDay: specialDayEntities)
        {
            if(allShifts.get(new Shift(specialDay.getDay(),1)) != null)
                specialDayMinutes.put(allShifts.get(new Shift(specialDay.getDay(),1)),specialDayMinutes.get(allShifts.get(new Shift(specialDay.getDay(),1))) + minutes.get(new Shift(specialDay.getDay(),1)));
            if(allShifts.get(new Shift(specialDay.getDay(),2)) != null)
                specialDayMinutes.put(allShifts.get(new Shift(specialDay.getDay(),2)),specialDayMinutes.get(allShifts.get(new Shift(specialDay.getDay(),2))) + minutes.get(new Shift(specialDay.getDay(),2)));
            if(allShifts.get(new Shift(specialDay.getDay(),3)) != null)
                specialDayMinutes.put(allShifts.get(new Shift(specialDay.getDay(),3)),specialDayMinutes.get(allShifts.get(new Shift(specialDay.getDay(),3))) + minutes.get(new Shift(specialDay.getDay(),3)));
            if(allShifts.get(new Shift(specialDay.getDay(),4)) != null)
                specialDayMinutes.put(allShifts.get(new Shift(specialDay.getDay(),4)),specialDayMinutes.get(allShifts.get(new Shift(specialDay.getDay(),4))) + minutes.get(new Shift(specialDay.getDay(),4))/3);

        }

        int most=0, least = 1000000;
        for (Map.Entry<Long, Integer> mapEntry : specialDayMinutes.entrySet()) {
            if (mapEntry.getValue()>most)
                most=mapEntry.getValue();
            if (mapEntry.getValue()<least)
                least=mapEntry.getValue();
        }

        return (most-least)/20;
    }

    private int getWeeklyPenalty(HashMap<Shift, Long> immutable, HashMap<Shift, Long> mutable, HashMap<Shift, Integer> minutesHashMap) {
        HashMap<Shift, Long> allShifts = new HashMap<>();
        allShifts.putAll(immutable);
        allShifts.putAll(mutable);
        int penalty = 0;

        HashMap<WorkerWeek, Integer> minutes = new HashMap<>();

        for (Map.Entry<Shift, Long> mapEntry : allShifts.entrySet()) {
            if (mapEntry.getValue() != null)
                for (int i=0;i<5;i++)
                    minutes.put(new WorkerWeek(mapEntry.getValue(),i),0);
        }

        for (Map.Entry<Shift, Long> mapEntry : allShifts.entrySet()) {
            if (mapEntry.getValue() != null) {
                minutes.put(new WorkerWeek(mapEntry.getValue(), mapEntry.getKey().getDay() % 7), minutes.get(new WorkerWeek(mapEntry.getValue(), mapEntry.getKey().getDay() / 7)) + minutesHashMap.get(mapEntry.getKey()));
            }
        }

        for (Map.Entry<WorkerWeek, Integer> mapEntry : minutes.entrySet()) {
            if (mapEntry.getValue()>49*60)
                penalty+=(mapEntry.getValue()-49*60)/60;
        }

        return penalty;
    }


    private int getMonthlyPenalty(HashMap<Shift, Long> immutable, HashMap<Shift, Long> mutable, HashMap<Shift, Integer> minutesHashMap, HashMap<Long, Integer> offsets, HashMap<Long, HolidayDTO> holidays) {
        HashMap<Shift, Long> allShifts = new HashMap<>();
        allShifts.putAll(immutable);
        allShifts.putAll(mutable);

        HashMap<Long, Integer> minutesForWorker = new HashMap<>();

        for (Map.Entry<Shift, Long> mapEntry : allShifts.entrySet()) {
            //system.out.println(mapEntry.getValue());
            if (mapEntry.getValue() != null)
                minutesForWorker.put(mapEntry.getValue(),offsets.get(mapEntry.getValue())+holidays.get(mapEntry.getValue()).getMinutesOnHolidays());
//            //system.out.println("eni");
        }

        for (Map.Entry<Shift, Long> mapEntry : allShifts.entrySet()) {
            if (mapEntry.getValue() != null){
                minutesForWorker.put(mapEntry.getValue(),minutesForWorker.get(mapEntry.getValue()) + minutesHashMap.get(mapEntry.getKey()));
            }
        }

        int most=0, least = 1000000;
        for (Map.Entry<Long, Integer> mapEntry : minutesForWorker.entrySet()) {
            if (mapEntry.getValue()>most)
                    most=mapEntry.getValue();
            if (mapEntry.getValue()<least)
                least=mapEntry.getValue();
        }

        return (most-least)/20;
    }

    private int getHolidayPenalty(HashMap<Shift, Long> immutable, HashMap<Shift, Long> mutable, ArrayList<HolidayAlgoDTO> holidays) {
        HashMap<Shift, Long> allShifts = new HashMap<>();
        allShifts.putAll(immutable);
        allShifts.putAll(mutable);
        int penalty = 0;

//        for(Map.Entry<Long,HolidayDTO> holiday:holidays.entrySet())
        for (HolidayAlgoDTO holiday : holidays)
        {
            for(int i=holiday.getFirstDay();i<=holiday.getLastDay();i++)
            {
                if(allShifts.get(new Shift(i,1))!=null && allShifts.get(new Shift(i,1))==holiday.getWorkerId()) penalty+=1000;
                if(allShifts.get(new Shift(i,2))!=null && allShifts.get(new Shift(i,2))==holiday.getWorkerId()) penalty+=1000;
                if(allShifts.get(new Shift(i,3))!=null && allShifts.get(new Shift(i,3))==holiday.getWorkerId()) penalty+=1000;
                if(allShifts.get(new Shift(i,4))!=null && allShifts.get(new Shift(i,4))==holiday.getWorkerId()) penalty+=1000;
            }
        }
        return penalty;

    }


    private int getNightsInARowAndDayAfterNightAndSameDayAnd12sInARowPenalty(HashMap<Shift, Long> immutable, HashMap<Shift, Long> mutable, int length) {
        HashMap<Shift, Long> allShifts = new HashMap<>();
        allShifts.putAll(immutable);
        allShifts.putAll(mutable);
        int penalty = 0;

        for(int i=1;i<=length-2;i++)
        {
            if(allShifts.get(new Shift(i,4))==allShifts.get(new Shift(i+1,4)))
                penalty+=5;//2 nights
            if(allShifts.get(new Shift(i,4))==allShifts.get(new Shift(i+1,4)) && allShifts.get(new Shift(i,4))==allShifts.get(new Shift(i+2,4)))
                penalty+=495;//3 nights
            if(allShifts.get(new Shift(i,4))==allShifts.get(new Shift(i+1,1)))
                penalty+=1000;//day after night
            if(     allShifts.get(new Shift(i,1))==allShifts.get(new Shift(i,2)) ||
                    allShifts.get(new Shift(i,1))==allShifts.get(new Shift(i,3)) ||
                    allShifts.get(new Shift(i,1))==allShifts.get(new Shift(i,4)) ||
                    allShifts.get(new Shift(i,2))==allShifts.get(new Shift(i,3)) ||
                    allShifts.get(new Shift(i,2))==allShifts.get(new Shift(i,4)) ||
                    allShifts.get(new Shift(i,3))==allShifts.get(new Shift(i,4)))
                penalty+=2000;//same day

            if(     allShifts.get(new Shift(i,2))==allShifts.get(new Shift(i+1,2)) ||
                    allShifts.get(new Shift(i,2))==allShifts.get(new Shift(i+1,3)) ||
                    allShifts.get(new Shift(i,3))==allShifts.get(new Shift(i+1,2)) ||
                    allShifts.get(new Shift(i,3))==allShifts.get(new Shift(i+1,3)))
                penalty+=10;//2 days second shift

            if(     (allShifts.get(new Shift(i,2))==allShifts.get(new Shift(i+1,2)) && allShifts.get(new Shift(i,2))==allShifts.get(new Shift(i+2,2))) ||
                    (allShifts.get(new Shift(i,2))==allShifts.get(new Shift(i+1,2)) && allShifts.get(new Shift(i,2))==allShifts.get(new Shift(i+2,3))) ||
                    (allShifts.get(new Shift(i,2))==allShifts.get(new Shift(i+1,3)) && allShifts.get(new Shift(i,2))==allShifts.get(new Shift(i+2,2))) ||
                    (allShifts.get(new Shift(i,2))==allShifts.get(new Shift(i+1,3)) && allShifts.get(new Shift(i,2))==allShifts.get(new Shift(i+2,3))) ||
                    (allShifts.get(new Shift(i,3))==allShifts.get(new Shift(i+1,2)) && allShifts.get(new Shift(i,3))==allShifts.get(new Shift(i+2,2))) ||
                    (allShifts.get(new Shift(i,3))==allShifts.get(new Shift(i+1,2)) && allShifts.get(new Shift(i,3))==allShifts.get(new Shift(i+2,3))) ||
                    (allShifts.get(new Shift(i,3))==allShifts.get(new Shift(i+1,3)) && allShifts.get(new Shift(i,3))==allShifts.get(new Shift(i+2,2))) ||
                    (allShifts.get(new Shift(i,3))==allShifts.get(new Shift(i+1,3)) && allShifts.get(new Shift(i,3))==allShifts.get(new Shift(i+2,3))))
                penalty+=1000;//2 days second shift


        }
        if(allShifts.get(new Shift(length-1,4))==allShifts.get(new Shift(length,4)))
            penalty+=5;//2nights in a row
        if(allShifts.get(new Shift(length-1,4))==allShifts.get(new Shift(length,1)))
            penalty+=1000;//day after night

        if(     allShifts.get(new Shift(length-1,1))==allShifts.get(new Shift(length-1,2)) ||
                allShifts.get(new Shift(length-1,1))==allShifts.get(new Shift(length-1,3)) ||
                allShifts.get(new Shift(length-1,1))==allShifts.get(new Shift(length-1,4)) ||
                allShifts.get(new Shift(length-1,2))==allShifts.get(new Shift(length-1,3)) ||
                allShifts.get(new Shift(length-1,2))==allShifts.get(new Shift(length-1,4)) ||
                allShifts.get(new Shift(length-1,3))==allShifts.get(new Shift(length-1,4)))
            penalty+=2000;//same day

        if(     allShifts.get(new Shift(length,1))==allShifts.get(new Shift(length,2)) ||
                allShifts.get(new Shift(length,1))==allShifts.get(new Shift(length,3)) ||
                allShifts.get(new Shift(length,1))==allShifts.get(new Shift(length,4)) ||
                allShifts.get(new Shift(length,2))==allShifts.get(new Shift(length,3)) ||
                allShifts.get(new Shift(length,2))==allShifts.get(new Shift(length,4)) ||
                allShifts.get(new Shift(length,3))==allShifts.get(new Shift(length,4)))
            penalty+=2000;//same day

        if(     allShifts.get(new Shift(length,2))==allShifts.get(new Shift(length-1,2)) ||
                allShifts.get(new Shift(length,2))==allShifts.get(new Shift(length-1,3)) ||
                allShifts.get(new Shift(length,3))==allShifts.get(new Shift(length-1,2)) ||
                allShifts.get(new Shift(length,3))==allShifts.get(new Shift(length-1,3)))
            penalty+=10;//2 days second shift



        return penalty;
    }


    public List<MonthHolidays> deprecatedgetAllHolidaysForMonth(long monthId) {


        List<HolidayEntity> holidayList = holidayRepository.findAll();
        List<WorkerEntity> workerEntityList = workerRepository.findAll();
        List<MonthHolidays> monthHolidaysList = new ArrayList<>();
        MonthHolidays monthHoliday;

        for (WorkerEntity workerEntity : workerEntityList)
        {
            monthHoliday = new MonthHolidays(workerEntity);
            for (HolidayEntity holidayEntity1 : holidayList) {
                if (monthId == holidayEntity1.getMonthId() &&
                        workerEntity.getId() == holidayEntity1.getWorkerId()) {

                    monthHoliday.addHoliday(holidayEntity1);
                }
            }
            monthHolidaysList.add(monthHoliday);
        }


        return monthHolidaysList;
    }

    public List<HolidayNameDTO> getAllHolidaysForMonth(long monthId) {

        List<HolidayEntity> holidayList = holidayRepository.findAll();
        List<HolidayNameDTO> holidayList1 = new ArrayList<>();
        List<WorkerEntity> workerEntityList = workerRepository.findAll();

        for (HolidayEntity holidayEntity1 : holidayList) {
            if (monthId == holidayEntity1.getMonthId()) {
                holidayList1.add(new HolidayNameDTO(holidayEntity1, getWorkerNameById(workerEntityList,holidayEntity1.getWorkerId())));
            }
        }

        Collections.sort(holidayList1);

        return holidayList1;
    }

    private String getWorkerNameById(List <WorkerEntity> workerEntities, long workerId) {
        for (WorkerEntity workerEntity : workerEntities) {
            if (workerEntity.getId() == workerId)
                return workerEntity.getName();
        }
        return "errorr";
    }

}
