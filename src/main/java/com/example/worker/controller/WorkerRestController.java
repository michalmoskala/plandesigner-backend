package com.example.worker.controller;

import com.example.worker.WorkerContainer;
import com.example.worker.repository.WorkerEntity;
import com.example.worker.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workers")
public class WorkerRestController {

    private WorkerService workerService;

    @Autowired
    WorkerRestController(WorkerService workerService) {
        this.workerService = workerService;
    }

    @GetMapping("/{id}")
    public WorkerEntity getworker(@PathVariable("id") long id){
        return workerService.findById(id).get();
    }

    //todo
    @GetMapping("")
    public List<WorkerContainer> getworkers(){
        return workerService.findAllByMonth();
    }

    @PutMapping("/{id}/shortname")
    public WorkerEntity putShortname(@PathVariable long id, @RequestBody String shortname){
        return workerService.putShortname(shortname,id);
    }

    @PutMapping("/{id}/name")
    public WorkerEntity putName(@PathVariable long id, @RequestBody String name){
        return workerService.putName(name,id);
    }


    @PostMapping("")
    public WorkerEntity postworker(@RequestBody WorkerEntity workerEntity){
        return workerService.postEntity(workerEntity);
    }

    @DeleteMapping("/{id}")
    public void deleteWorker(@PathVariable("id") long id){
        workerService.deleteById(id);
    }
}
