package com.example.worker.service;

import com.example.holiday.repository.HolidayEntity;
import com.example.holiday.repository.HolidayRepository;
import com.example.offset.repository.OffsetEntity;
import com.example.offset.repository.OffsetRepository;
import com.example.shift.repository.ShiftEntity;
import com.example.shift.repository.ShiftRepository;
import com.example.specialday.repository.SpecialDayEntity;
import com.example.specialday.repository.SpecialDayRepository;
import com.example.worker.repository.WorkerEntity;
import com.example.worker.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
//todo ta klasa moglaby zostac uwydajniona bo jest taka se
@Service
public class WorkerService {

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private SpecialDayRepository specialDayRepository;

    @Autowired
    private OffsetRepository offsetRepository;

    @Autowired
    private HolidayRepository holidayRepository;

    public Optional<WorkerEntity> findById(long id){
        return workerRepository.findById(id);
    }

    public WorkerEntity postEntity(WorkerEntity workerEntity){
        return workerRepository.save(workerEntity);
    }


    public WorkerEntity putWorker(WorkerEntity workerEntity, long id)
    {
        workerEntity.setId(id);
        return workerRepository.save(workerEntity);

    }

    public List<WorkerDTO> findAllByMonth(long monthId){
        ArrayList<WorkerDTO> workerContainers = new ArrayList<>();
        
        List<WorkerEntity> workers = workerRepository.findAll();
        List<ShiftEntity> shiftEntities = shiftRepository.findAll();
        List<SpecialDayEntity> specialDayEntities = specialDayRepository.findAll();
        List<OffsetEntity> offsetEntities = offsetRepository.findAll();
        List<HolidayEntity> holidayEntities = holidayRepository.findAll();

        for (WorkerEntity worker: workers) {
            List<ShiftEntity> shiftsForWorker = getAllShiftsForWorkerForMonth(worker,shiftEntities,monthId);
            WorkerDTO workerContainer = prepareContainerForWorker(worker,shiftsForWorker,specialDayEntitiesOnlyForMonth(monthId,specialDayEntities));
            workerContainer.setOffsetMinutes(findOffsetMinutesForWorker(worker,offsetEntities,monthId));
            workerContainer.setDaysOnHoliday(findDaysOnHolidayForWorker(worker,holidayEntities,monthId));
            workerContainer.setMinutes();
            workerContainers.add(workerContainer);
        }
        
        return workerContainers;
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

    public void deleteById(long id){
        workerRepository.deleteById(id);
    }

    private List<SpecialDayEntity> specialDayEntitiesOnlyForMonth(long monthId, List<SpecialDayEntity> specialDayEntities)
    {
        List<SpecialDayEntity> filteredSpecialDayEntities = new ArrayList<>();
        for(SpecialDayEntity specialDayEntity: specialDayEntities)
            if (specialDayEntity.getMonthId() == monthId)
                filteredSpecialDayEntities.add(specialDayEntity);
        return filteredSpecialDayEntities;
    }

    private List<ShiftEntity> getAllShiftsForWorkerForMonth(WorkerEntity workerEntity,List<ShiftEntity> shiftEntities, long monthId){
        List<ShiftEntity> shiftEntities1 = new ArrayList<>();
        for (ShiftEntity shiftEntity: shiftEntities)
            if(shiftEntity.getWorkerId() == workerEntity.getId() && shiftEntity.getMonthId() == monthId)
                shiftEntities1.add(shiftEntity);
        return shiftEntities1;
    }

    private boolean isSpecial(ShiftEntity shiftEntity, List<SpecialDayEntity> specialDays)//kloiu tu oli no elo
    {
        for (SpecialDayEntity specialDay: specialDays)
            if (specialDay.getDay() == shiftEntity.getDay())
                return true;
        return false;
    }

    private WorkerDTO prepareContainerForWorker(WorkerEntity worker, List<ShiftEntity> workerShifts, List<SpecialDayEntity> specialDays)
    {
        int workedMinutes = 0;
        int weekendMinutes = 0;

        for (ShiftEntity shift:workerShifts)
        {
            workedMinutes+=shift.getMinutes();
            if(isSpecial(shift,specialDays))
                weekendMinutes+=shift.getMinutes();
        }
        return new WorkerDTO(worker,workedMinutes,weekendMinutes);


    }

    public List<WorkerEntity> getAllWorkers() {
        return workerRepository.findAll();
    }
}
