package com.example.worker.service;

import com.example.worker.WorkerContainer;
import com.example.worker.repository.WorkerEntity;
import com.example.worker.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkerService {

    @Autowired
    private WorkerRepository workerRepository;

    public Optional<WorkerEntity> findById(long id){
        return workerRepository.findById(id);
    }

    public WorkerEntity postEntity(WorkerEntity workerEntity){
        return workerRepository.save(workerEntity);
    }

    public WorkerEntity putName(String name, long id)
    {
        WorkerEntity workerEntity = workerRepository.findById(id).get();
        workerEntity.setName(name);
        workerRepository.save(workerEntity);
        return workerEntity;
    }

    public WorkerEntity putShortname(String shortname, long id)
    {
        WorkerEntity workerEntity = workerRepository.findById(id).get();
        workerEntity.setShortname(shortname);
        workerRepository.save(workerEntity);
        return workerEntity;
    }

    public List<WorkerContainer> findAllByMonth(){
        //return workerRepository.findAll();
        return null;
    }

    public void deleteById(long id){
        workerRepository.deleteById(id);
    }

}
