package com.example.shift.controller;

import com.example.shift.repository.ShiftEntity;
import com.example.shift.service.ShiftDTO;
import com.example.shift.service.ShiftService;
import com.example.shift.service.SimpleShiftDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shifts")
public class ShiftRestController {

    private ShiftService shiftService;

    @Autowired
    ShiftRestController(ShiftService shiftService) {
        this.shiftService = shiftService;
    }



    @PostMapping("")
    public ShiftDTO postShift(@RequestBody ShiftEntity shiftEntity)
    {
        return shiftService.postShift(shiftEntity);
    }

    @PutMapping("/{id}")
    public ShiftEntity putTime(@PathVariable long id, @RequestBody ShiftEntity shiftEntity){
        return shiftService.putTime(shiftEntity,id);
    }


    @PutMapping("/minutes/{id}")
    public ShiftEntity putMinutes(@PathVariable long id) {
        return shiftService.putMinutes(id);
    }


    @DeleteMapping("")
    public String deleteByTime(@RequestBody SimpleShiftDTO shiftEntity){
        return shiftService.deleteByTime(shiftEntity);
    }

    //lepiej nie uzywac
    @PutMapping("/{id}/worker")
    public ShiftEntity putWorker(@PathVariable long id, @RequestBody long workerId){
        return shiftService.putWorker(workerId,id);
    }


}
