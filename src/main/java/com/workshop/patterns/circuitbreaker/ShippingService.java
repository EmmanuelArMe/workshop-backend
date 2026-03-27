package com.workshop.patterns.circuitbreaker;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ShippingService {

    private final Random random = new Random();

    // Simulate external service that has a high chance of failure/slowness
    public String calculateShipping(String orderId) {
        int chance = random.nextInt(100);

        // 50% chance to fail
        if (chance < 50) {
            try {
                // Simulate slowness / thread exhaustion
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            throw new ExternalServiceException("Shipping service timeout for order: " + orderId);
        }

        return "Shipping calculated successfully for " + orderId + " (Cost: $15.00)";
    }
}