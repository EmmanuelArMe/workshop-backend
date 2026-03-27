package com.workshop.patterns.circuitbreaker;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final ShippingService shippingService;

    public OrderService(ShippingService shippingService) {
        this.shippingService = shippingService;
    }

    // Name should match the config in application.yml
    @CircuitBreaker(name = "shippingService", fallbackMethod = "shippingFallback")
    public String createOrder(String orderId) {
        log.info("Attempting to calculate shipping for order {}", orderId);
        String shippingResult = shippingService.calculateShipping(orderId);
        return "Order " + orderId + " created! " + shippingResult;
    }

    // Fallback method - must have same signature plus an Exception parameter
    public String shippingFallback(String orderId, Throwable t) {
        log.warn("Circuit Breaker OPEN or execution failed for order {}: {}", orderId, t.getMessage());
        return "Order " + orderId + " created! (Fallback: Envío pendiente de calcular, nos comunicaremos en breve)";
    }
}