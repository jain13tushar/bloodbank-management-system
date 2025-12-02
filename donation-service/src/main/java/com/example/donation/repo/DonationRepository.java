package com.example.donation.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.donation.model.Donation;

public interface DonationRepository extends JpaRepository<Donation, Long> {
}