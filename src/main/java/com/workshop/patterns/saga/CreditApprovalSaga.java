package com.workshop.patterns.saga;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CreditApprovalSaga {

    private static final Logger log = LoggerFactory.getLogger(CreditApprovalSaga.class);

    private final AccountService accountService;
    private final CardService cardService;

    public CreditApprovalSaga(AccountService accountService, CardService cardService) {
        this.accountService = accountService;
        this.cardService = cardService;
    }

    // Orchestrator saga
    public String approveCredit(String userId, boolean simulateCardFailure) {
        log.info("Iniciando proceso de SAGA de aprobación de crédito para {}", userId);

        try {
            // Paso 1: Crear Cuenta (Transacción local 1)
            accountService.createAccount(userId);

            // Paso 2: Emitir Tarjeta (Transacción local 2)
            cardService.issueCard(userId, simulateCardFailure);

            log.info("SAGA Completada Exitosamente para {}", userId);
            return "Crédito Aprobado y Tarjeta Emitida";

        } catch (Exception e) {
            log.error("SAGA Falló. Ejecutando compensaciones...");

            // Transacción de Compensación
            accountService.compensateAccount(userId);

            log.warn("SAGA Abortada y Compensada para {}", userId);
            return "Crédito Denegado: Falló emisión de tarjeta. Cuenta anulada.";
        }
    }
}