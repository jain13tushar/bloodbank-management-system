package com.example.donor.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.donor.model.Donor;
import com.example.donor.repo.DonorRepository;

@RestController
@RequestMapping("/api/donors")
public class DonorController {
	private final DonorRepository repo;

	public DonorController(DonorRepository repo) {
		this.repo = repo;
	}

	@PostMapping
	public Donor create(@RequestBody Donor d) {
		return repo.save(d);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Donor> get(@PathVariable Long id) {
		return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@GetMapping
	public List<Donor> list(@RequestParam(required = false) String bloodGroup) {
		if (bloodGroup == null)
			return repo.findAll();
		return repo.findByBloodGroup(bloodGroup);
	}

	@PutMapping("/{id}/eligibility")
	public ResponseEntity<Donor> updateEligibility(@PathVariable Long id, @RequestBody Donor partial) {
		return repo.findById(id).map(d -> {
			d.setEligibilityStatus(partial.getEligibilityStatus());
			d.setLastDonationDate(partial.getLastDonationDate());
			repo.save(d);
			return ResponseEntity.ok(d);
		}).orElse(ResponseEntity.notFound().build());
	}
}