package com.example.shift.service;

import com.example.block.repository.BlockEntity;
import com.example.block.repository.BlockRepository;
import com.example.month.Shift;
import com.example.shift.repository.ShiftEntity;
import com.example.shift.repository.ShiftRepository;
import com.example.worker.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShiftService {

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private BlockRepository blockRepository;


    public ShiftDTO postShift(ShiftEntity shiftEntity)
    {

        List<BlockEntity> blockEntityList = blockRepository.findAll();
        for (BlockEntity blockEntity : blockEntityList)
        {
            if (blockEntity.getMonthId()==shiftEntity.getMonthId()&&blockEntity.getDay()==shiftEntity.getDay()&&blockEntity.getWhichTime()==shiftEntity.getWhichTime())
            {
                blockRepository.deleteById(blockEntity.getId());
                break;
            }
        }

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

    public String deleteByTime(SimpleShiftDTO received)
    {
        List<ShiftEntity> shiftEntities = shiftRepository.findAll();
        List<BlockEntity> blockEntities = blockRepository.findAll();

        for (ShiftEntity shiftEntity:shiftEntities){
            if (received.getMonthId()==shiftEntity.getMonthId()&&received.getDay()==shiftEntity.getDay()&&received.getWhichTime()==shiftEntity.getWhichTime()) {
                shiftRepository.delete(shiftEntity);
                return "OK";
            }
        }

        for (BlockEntity blockEntity:blockEntities){
            if (received.getMonthId()==blockEntity.getMonthId()&&received.getDay()==blockEntity.getDay()&&received.getWhichTime()==blockEntity.getWhichTime()) {
                blockRepository.delete(blockEntity);
                return "OK";
            }
        }
        throw new NullPointerException();
    }


}
