package com.workshop.patterns.saga;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountService.class);

    // Simulating database storage
    private final Map<String, String> accountDatabase = new HashMap<>();

    public void createAccount(String userId) {
        log.info("SAGA Paso 1: Creando cuenta bancaria para {}", userId);
        accountDatabase.put(userId, "ACTIVA");
    }

    public void compensateAccount(String userId) {
        log.warn("SAGA Compensación: Anulando cuenta bancaria para {}", userId);
        accountDatabase.put(userId, "ANULADA");
    }

    public String getAccountStatus(String userId) {
        return accountDatabase.get(userId);
    }
}