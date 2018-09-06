package com.example.month.controller;

import com.example.holiday.repository.HolidayEntity;
import com.example.holiday.service.HolidayService;
import com.example.month.MonthContainer;
import com.example.month.repository.MonthEntity;
import com.example.month.service.MonthService;
import com.example.offset.repository.OffsetEntity;
import com.example.offset.service.OffsetService;
import com.example.specialday.repository.SpecialDayEntity;
import com.example.specialday.service.SpecialDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/months")
public class MonthRestController {

    private MonthService monthService;
    private OffsetService offsetService;
    private HolidayService holidayService;
    private SpecialDayService specialDayService;

    @Autowired
    MonthRestController(MonthService monthService, OffsetService offsetService, HolidayService holidayService, SpecialDayService specialDayService) {
        this.monthService = monthService;
        this.offsetService = offsetService;
        this.holidayService = holidayService;
        this.specialDayService = specialDayService;
    }

    //todo
    @GetMapping("/{id}")
    public MonthContainer getMonth(@PathVariable("id") long id){
        return monthService.findById(id);
    }

    @GetMapping("")
    public List<MonthEntity> findAllMonths(){
        return monthService.findAll();
    }

    @PostMapping("")
    public MonthEntity postMonth(@RequestBody MonthEntity monthEntity){
        return monthService.postMonth(monthEntity);
    }

    @PutMapping("/{id}")
    public MonthEntity putName(@PathVariable long id, @RequestBody String name){
        return monthService.putName(name,id);
    }


    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") long id){
        monthService.deleteById(id);
    }

    @PutMapping("/{id}/offsets")
    public OffsetEntity putOffset(@PathVariable("id") long id, @RequestBody OffsetEntity offsetEntity)
    {
        return offsetService.put(id, offsetEntity);
    }

    @PutMapping("/{id}/holidays")
    public HolidayEntity putHoliday(@PathVariable("id") long id, @RequestBody HolidayEntity holidayEntity)
    {
        return holidayService.put(id, holidayEntity);
    }

    @PutMapping("/{id}/special-day")
    public SpecialDayEntity switchDay(@PathVariable("id") long id, @RequestBody int day)
    {
        return specialDayService.put(id, day);
    }
}
