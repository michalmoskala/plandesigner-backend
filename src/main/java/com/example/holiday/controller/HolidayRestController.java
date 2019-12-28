package com.example.holiday.controller;

import com.example.holiday.service.HolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/holidays")
public class HolidayRestController {

    private HolidayService holidayService;

    @Autowired
    HolidayRestController(HolidayService holidayService) { this.holidayService = holidayService; }


    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") long id){
        holidayService.deleteById(id);
    }
}
