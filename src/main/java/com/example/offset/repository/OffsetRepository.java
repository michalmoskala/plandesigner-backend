package com.example.offset.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OffsetRepository extends JpaRepository<OffsetEntity, Long> {



}
