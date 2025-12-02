package com.example.inventory.web;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

import com.example.inventory.model.BloodUnit;
import com.example.inventory.repo.BloodUnitRepository;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
	private final BloodUnitRepository repo;

	public InventoryController(BloodUnitRepository repo) {
		this.repo = repo;
	}

	@PostMapping
	public ResponseEntity<String> createFromDonation(@RequestBody java.util.Map<String, Object> body) {
		Long donationId = body.get("donationId") == null ? null : Long.valueOf(body.get("donationId").toString());
		Long donorId = body.get("donorId") == null ? null : Long.valueOf(body.get("donorId").toString());
		BloodUnit unit = new BloodUnit();
		unit.setDonationId(donationId);
		unit.setDonorId(donorId);
		unit.setStatus("AVAILABLE");
		unit.setCollectedDate(LocalDate.now());
		unit.setExpiryDate(LocalDate.now().plus(42, ChronoUnit.DAYS));
		if (body.get("bloodGroup") != null)
			unit.setBloodGroup(body.get("bloodGroup").toString());
		else
			unit.setBloodGroup("UNKNOWN");
		repo.save(unit);
		return ResponseEntity.ok("created");
	}

	@GetMapping
	public List<BloodUnit> list(@RequestParam(required = false) String bloodGroup) {
		if (bloodGroup == null)
			return repo.findAll();
		return repo.findByBloodGroupAndStatus(bloodGroup, "AVAILABLE");
	}

	@GetMapping("/{id}")
	public ResponseEntity<BloodUnit> get(@PathVariable("id") Long id) {
		return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/{id}/status")
	public ResponseEntity<BloodUnit> updateStatus(@PathVariable("id") Long id,
			@RequestBody java.util.Map<String, String> body) {
		return repo.findById(id).map(u -> {
			u.setStatus(body.get("status"));
			repo.save(u);
			return ResponseEntity.ok(u);
		}).orElse(ResponseEntity.notFound().build());
	}
}