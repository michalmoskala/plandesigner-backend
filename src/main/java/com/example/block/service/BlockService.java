package com.example.block.service;

import com.example.block.repository.BlockEntity;
import com.example.block.repository.BlockRepository;
import com.example.shift.repository.ShiftEntity;
import com.example.shift.repository.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlockService {

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private BlockRepository blockRepository;


    public BlockDTO postBlock(BlockEntity blockEntity1) {

        List<BlockEntity> blockEntityList = blockRepository.findAll();
        for (BlockEntity blockEntity : blockEntityList) {
            if (blockEntity.getMonthId() == blockEntity1.getMonthId() && blockEntity.getDay() == blockEntity1.getDay() && blockEntity.getWhichTime() == blockEntity1.getWhichTime()) {
                blockRepository.deleteById(blockEntity.getId());
                break;
            }
        }

        //todo jpql
        List<ShiftEntity> shiftEntityList = shiftRepository.findAll();
        for (ShiftEntity shiftEntity1 : shiftEntityList) {
            if (shiftEntity1.getMonthId() == blockEntity1.getMonthId() && shiftEntity1.getDay() == blockEntity1.getDay() && shiftEntity1.getWhichTime() == blockEntity1.getWhichTime()) {
                shiftRepository.deleteById(shiftEntity1.getId());
                break;
            }
        }

        return new BlockDTO(blockRepository.save(blockEntity1),"X");
    }



}
