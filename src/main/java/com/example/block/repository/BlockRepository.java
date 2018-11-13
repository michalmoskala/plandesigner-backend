package com.example.block.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockRepository extends JpaRepository<BlockEntity,Long> {
}

