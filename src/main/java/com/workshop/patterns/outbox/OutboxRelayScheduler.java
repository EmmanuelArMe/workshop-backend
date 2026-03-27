package com.workshop.patterns.outbox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OutboxRelayScheduler {

    private static final Logger log = LoggerFactory.getLogger(OutboxRelayScheduler.class);

    private final OutboxRepository outboxRepository;

    public OutboxRelayScheduler(OutboxRepository outboxRepository) {
        this.outboxRepository = outboxRepository;
    }

    // Se ejecuta cada 5 segundos
    @Scheduled(fixedDelay = 5000)
    public void processOutboxEvents() {
        List<OutboxEventEntity> pendingEvents = outboxRepository.findByProcessedFalse();

        if (!pendingEvents.isEmpty()) {
            log.info("Outbox Relay: Encontrados {} eventos pendientes de envío a Kafka/Broker", pendingEvents.size());

            for (OutboxEventEntity event : pendingEvents) {
                try {
                    // Simular envío al Broker (Ej. Kafka)
                    log.info(">>> Enviando mensaje al Broker - Tipo: {}, Payload: {}", event.getEventType(), event.getPayload());

                    // Marcar como procesado si el envío fue exitoso
                    event.setProcessed(true);
                    outboxRepository.save(event);
                    log.info("Outbox Relay: Evento {} marcado como procesado.", event.getId());

                } catch (Exception e) {
                    log.error("Outbox Relay: Error enviando el evento {}. Se reintentará en la siguiente ejecución.", event.getId());
                    // No se marca como procesado, así se vuelve a intentar después
                }
            }
        }
    }
}