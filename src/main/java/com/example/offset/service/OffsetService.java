package com.example.offset.service;

import com.example.offset.repository.OffsetEntity;
import com.example.offset.repository.OffsetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OffsetService {

    @Autowired
    private OffsetRepository offsetRepository;

    public OffsetEntity put(long monthId, OffsetEntity offsetEntity)
    {

        //todo zamienic na sqlke
        List<OffsetEntity> list = offsetRepository.findAll();
        for (OffsetEntity offsetEntity1 : list)
        {
            if (monthId==offsetEntity1.getMonthId() &&
                    offsetEntity.getWorkerId()==offsetEntity1.getWorkerId())
            {
                    offsetEntity1.setMinutes(offsetEntity.getMinutes());
                    offsetRepository.save(offsetEntity1);
                    return offsetEntity1;
            }
        }
        offsetEntity.setMonthId(monthId);
        offsetRepository.save(offsetEntity);
        return offsetEntity;



    }
}
