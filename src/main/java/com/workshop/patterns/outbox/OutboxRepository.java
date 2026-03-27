package com.workshop.patterns.outbox;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OutboxRepository extends JpaRepository<OutboxEventEntity, Long> {
    List<OutboxEventEntity> findByProcessedFalse();
}