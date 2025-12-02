package com.example.donation.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DonorClient {
	private final RestTemplate rt;
	private final String donorBase;

	public DonorClient(RestTemplate rt,
			@Value("${donor.service.url:http://localhost:8081/api/donors}") String donorBase) {
		this.rt = rt;
		this.donorBase = donorBase;
	}

	public boolean exists(Long donorId) {
		try {
			var resp = rt.getForEntity(donorBase + "/" + donorId, String.class);
			return resp.getStatusCode().is2xxSuccessful();
		} catch (Exception e) {
			return false;
		}
	}
}