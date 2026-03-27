package com.workshop.patterns.outbox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final OutboxRepository outboxRepository;

    public UserService(UserRepository userRepository, OutboxRepository outboxRepository) {
        this.userRepository = userRepository;
        this.outboxRepository = outboxRepository;
    }

    @Transactional
    public UserEntity createUser(String username, String email) {
        log.info("Creando usuario: {}", username);

        // 1. Guardar la entidad de negocio
        UserEntity user = new UserEntity(username, email);
        user = userRepository.save(user);

        // 2. Guardar el evento en la tabla Outbox en la misma transacción
        String eventPayload = String.format("{\"userId\":%d, \"email\":\"%s\"}", user.getId(), user.getEmail());
        OutboxEventEntity event = new OutboxEventEntity("UsuarioCreado", eventPayload);
        outboxRepository.save(event);

        log.info("Usuario y Evento guardados atómicamente en base de datos. Event ID: {}", event.getId());

        return user;
    }
}