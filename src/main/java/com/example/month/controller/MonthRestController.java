package com.example.month.controller;

import com.example.holiday.repository.HolidayEntity;
import com.example.holiday.service.HolidayService;
import com.example.month.service.MonthContainer;
import com.example.month.repository.MonthEntity;
import com.example.month.service.MonthService;
import com.example.offset.repository.OffsetEntity;
import com.example.offset.service.OffsetService;
import com.example.specialday.repository.SpecialDayEntity;
import com.example.specialday.service.SpecialDayService;
import com.example.worker.service.WorkerDTO;
import com.example.worker.service.WorkerService;
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
    private WorkerService workerService;

    @Autowired
    MonthRestController(MonthService monthService, OffsetService offsetService, HolidayService holidayService, SpecialDayService specialDayService, WorkerService workerService) {
        this.monthService = monthService;
        this.offsetService = offsetService;
        this.holidayService = holidayService;
        this.specialDayService = specialDayService;
        this.workerService = workerService;
    }

    //todo
    @GetMapping("/{id}/workers")
    public List<WorkerDTO> getworkers(@PathVariable("id") long id){
        return workerService.findAllByMonth(id);
    }

    @GetMapping("/{id}")
    public MonthContainer getMonth(@PathVariable("id") long id){
        return monthService.getMonthContainer(id);
    }


    @GetMapping("/{id}/penalties")
    public Integer getVerification(@PathVariable("id") long id){
        return monthService.getSumOfPenalties(id);
    }

    @GetMapping("/{id}/bruteforce")
    public Integer getBFVerification(@PathVariable("id") long id){
        return monthService.getSumOfPenaltiesByBruteForce(id);
    }

    //ok
    @GetMapping("")
    public List<MonthEntity> findAllMonths(){
        return monthService.findAll();
    }

    //ok
    @PostMapping("")
    public MonthEntity postMonth(@RequestBody MonthEntity monthEntity){
        return monthService.postMonth(monthEntity);
    }


    //ok
    @PutMapping("/{id}")
    public MonthEntity putMonth(@PathVariable long id, @RequestBody MonthEntity monthEntity){
        return monthService.putMonth(monthEntity,id);
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

    @PutMapping("/{id}/special-days")
    public SpecialDayEntity switchDay(@PathVariable("id") long id, @RequestBody SpecialDayEntity day)
    {
        return specialDayService.put(id, day);
    }
}
