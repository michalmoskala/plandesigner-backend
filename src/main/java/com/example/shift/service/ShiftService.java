package com.example.shift.service;

import com.example.shift.repository.ShiftEntity;
import com.example.shift.repository.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShiftService {

    @Autowired
    private ShiftRepository shiftRepository;


    public ShiftEntity postShift(ShiftEntity shiftEntity)
    {
        return shiftRepository.save(shiftEntity);
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

    public ShiftEntity putMinutes(int minutes, long id)
    {
        ShiftEntity shiftEntity1 = shiftRepository.findById(id).get();
        shiftEntity1.setMinutes(minutes);
        return shiftRepository.save(shiftEntity1);
    }

    public void deleteById(long id)
    {
        shiftRepository.deleteById(id);
    }


}
