package com.example.donation.web;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.donation.model.Donation;
import com.example.donation.repo.DonationRepository;

@RestController
@RequestMapping("/api/donations")
public class DonationController {
	private final DonationRepository repo;
	private final RestTemplate restTemplate = new RestTemplate();
	@Value("${donor.service.url:http://localhost:8081/api/donors}")
	private String donorServiceUrl;
	@Value("${inventory.service.url:http://localhost:8083/api/inventory}")
	private String inventoryServiceUrl;

	public DonationController(DonationRepository repo) {
		this.repo = repo;
	}

	@PostMapping
	public ResponseEntity<Donation> create(@RequestBody Donation donation) {
		var donorResp = restTemplate.getForEntity(donorServiceUrl + "/" + donation.getDonorId(), String.class);
		if (!donorResp.getStatusCode().is2xxSuccessful())
			return ResponseEntity.badRequest().build();
		donation.setCollectedAt(LocalDateTime.now());
		donation.setStatus("COLLECTED");
		Donation saved = repo.save(donation);
		var unit = new java.util.HashMap<String, Object>();
		unit.put("donationId", saved.getId());
		unit.put("donorId", saved.getDonorId());
		restTemplate.postForEntity(inventoryServiceUrl, unit, String.class);
		return ResponseEntity.ok(saved);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Donation> get(@PathVariable Long id) {
		return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}
}