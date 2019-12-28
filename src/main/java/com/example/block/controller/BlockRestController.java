package com.example.block.controller;

import com.example.block.repository.BlockEntity;
import com.example.block.service.BlockDTO;
import com.example.block.service.BlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blocks")
public class BlockRestController {

    private BlockService blockService;

    @Autowired
    BlockRestController(BlockService blockService) {
        this.blockService = blockService;
    }

    @PostMapping("")
    public BlockDTO postShift(@RequestBody BlockEntity blockEntity)
    {
        return blockService.postBlock(blockEntity);
    }


}
