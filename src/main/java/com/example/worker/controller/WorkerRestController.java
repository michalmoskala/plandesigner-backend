package com.example.worker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/worker")
public class WorkerRestController {

    @GetMapping("/twojstary")
    public String twojstary(){
        return "twoj stary";
    }
}
