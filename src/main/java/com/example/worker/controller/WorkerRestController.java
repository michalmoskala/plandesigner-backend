package com.example.worker.controller;

import com.example.worker.repository.WorkerEntity;
import com.example.worker.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/workers")
public class WorkerRestController {

    private WorkerService workerService;

    @Autowired
    WorkerRestController(WorkerService workerService) {
        this.workerService = workerService;
    }

    //ok
    @GetMapping("/{id}")
    public WorkerEntity getworker(@PathVariable("id") long id){
        return workerService.findById(id).get();
    }

    @GetMapping("")
    public List<WorkerEntity> getWorkers()
    {
        return workerService.getAllWorkers();
    }



    //ok
    @PostMapping("")
    public WorkerEntity postworker(@RequestBody WorkerEntity workerEntity){
        return workerService.postEntity(workerEntity);
    }

    @DeleteMapping("/{id}")
    public void deleteWorker(@PathVariable("id") long id){
        workerService.deleteById(id);
    }
}
