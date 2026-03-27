package com.workshop.patterns;

import com.workshop.patterns.circuitbreaker.ExternalServiceException;
import com.workshop.patterns.circuitbreaker.OrderService;
import com.workshop.patterns.circuitbreaker.ShippingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CircuitBreakerTest {

    @Mock
    private ShippingService shippingService;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void testFallbackIsCalledOnFailure() {
        // Act & Assert
        try {
            // Directly call the fallback as if circuit breaker activated it (Unit testing)
            // Note: Full circuit breaker integration test requires a Spring Boot test context
            String result = orderService.shippingFallback("ORD-123", new ExternalServiceException("Timeout"));
            assertTrue(result.contains("Envío pendiente de calcular"));
        } catch (Exception e) {
            assertTrue(false, "No exception should be thrown");
        }
    }
}