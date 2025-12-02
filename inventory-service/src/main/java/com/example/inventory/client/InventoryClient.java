package com.example.inventory.client;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class InventoryClient {
	private final RestTemplate rt;
	private final String inventoryBase;

	public InventoryClient(RestTemplate rt,
			@Value("${inventory.service.url:http://localhost:8083/api/inventory}") String inventoryBase) {
		this.rt = rt;
		this.inventoryBase = inventoryBase;
	}

	public boolean createFromDonation(Map<String, Object> body) {
		try {
			rt.postForEntity(inventoryBase, body, String.class);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}