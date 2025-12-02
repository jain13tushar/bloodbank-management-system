package com.example.inventory.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.inventory.model.BloodUnit;

public interface BloodUnitRepository extends JpaRepository<BloodUnit, Long> {
	List<BloodUnit> findByBloodGroupAndStatus(String bloodGroup, String status);
}
