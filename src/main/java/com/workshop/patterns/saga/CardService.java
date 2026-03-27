package com.workshop.patterns.saga;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CardService {

    private static final Logger log = LoggerFactory.getLogger(CardService.class);

    private final Map<String, String> cardDatabase = new HashMap<>();

    public void issueCard(String userId, boolean shouldFail) {
        log.info("SAGA Paso 2: Emitiendo tarjeta para {}", userId);

        if (shouldFail) {
            log.error("Error SAGA: Falló emisión de tarjeta por error interno");
            throw new RuntimeException("Card Issuance System Down");
        }

        cardDatabase.put(userId, "EMITIDA");
    }

    public String getCardStatus(String userId) {
        return cardDatabase.get(userId);
    }
}