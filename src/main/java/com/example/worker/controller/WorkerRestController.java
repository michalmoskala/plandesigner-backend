package com.example.worker.controller;

import com.example.worker.repository.WorkerEntity;
import com.example.worker.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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


    @PutMapping("/{id}")
    public WorkerEntity putWorker(@PathVariable long id, @RequestBody WorkerEntity workerEntity){
        return workerService.putWorker(workerEntity,id);
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
