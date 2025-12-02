package com.example.donor.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.donor.model.Donor;

public interface DonorRepository extends JpaRepository<Donor, Long> {
	List<Donor> findByBloodGroup(String bloodGroup);
}