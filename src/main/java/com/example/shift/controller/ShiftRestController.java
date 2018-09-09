package com.example.shift.controller;

import com.example.shift.repository.ShiftEntity;
import com.example.shift.service.ShiftService;
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
    public ShiftEntity postShift(@RequestBody ShiftEntity shiftEntity)
    {
        return shiftService.postShift(shiftEntity);
    }

    @PutMapping("/{id}")
    public ShiftEntity putTime(@PathVariable long id, @RequestBody ShiftEntity shiftEntity){
        return shiftService.putTime(shiftEntity,id);
    }

    //hujowo lepiej nie uzywac
    @PutMapping("/{id}/worker")
    public ShiftEntity putWorker(@PathVariable long id, @RequestBody long workerId){
        return shiftService.putWorker(workerId,id);
    }

    //hujowo lepiej nie uzywac
    @PutMapping("/{id}/minutes")
    public ShiftEntity putMinutes(@PathVariable long id, @RequestBody int minutes){
        return shiftService.putMinutes(minutes,id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") long id){
        shiftService.deleteById(id);
    }



}
