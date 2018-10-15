package com.example.shift.service;

import com.example.shift.repository.ShiftEntity;
import com.example.shift.repository.ShiftRepository;
import com.example.worker.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShiftService {

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private WorkerRepository workerRepository;


    public ShiftDTO postShift(ShiftEntity shiftEntity)
    {
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

    public ShiftEntity putTime(ShiftEntity shiftEntity, long id)
    {
        ShiftEntity shiftEntity1 = shiftRepository.findById(id).get();
        shiftEntity1.setDay(shiftEntity.getDay());
        shiftEntity1.setWhichTime(shiftEntity.getWhichTime());
        return shiftRepository.save(shiftEntity1);

    }

    public ShiftEntity putWorker(long workerId, long id)
    {
        ShiftEntity shiftEntity1 = shiftRepository.findById(id).get();
        shiftEntity1.setWorkerId(workerId);
        return shiftRepository.save(shiftEntity1);

    }

    public ShiftEntity putMinutes(long id)
    {
        ShiftEntity shiftEntity = shiftRepository.findById(id).get();
        if (shiftEntity.getMinutes()==300)
            shiftEntity.setMinutes(720);
        else
            shiftEntity.setMinutes(300);
        return shiftRepository.save(shiftEntity);
    }

    public void deleteById(long id)
    {
        shiftRepository.deleteById(id);
    }


}
