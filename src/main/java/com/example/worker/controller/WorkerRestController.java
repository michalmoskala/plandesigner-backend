package com.example.worker.controller;

import com.example.worker.repository.WorkerEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/worker")
public class WorkerRestController {

    @GetMapping("/twojstary")
    public WorkerEntity twojstary(){
        WorkerEntity workerEntity = new WorkerEntity();
        workerEntity.setId(1);
        workerEntity.setName("Guantanamera");
        workerEntity.setShortname("gua");
        return workerEntity;
    }
}
