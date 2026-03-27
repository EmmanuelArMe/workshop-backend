package com.workshop.patterns;

import com.workshop.patterns.outbox.OutboxEventEntity;
import com.workshop.patterns.outbox.OutboxRepository;
import com.workshop.patterns.outbox.UserEntity;
import com.workshop.patterns.outbox.UserRepository;
import com.workshop.patterns.outbox.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class OutboxTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OutboxRepository outboxRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testCreateUserAndOutboxEventAtomically() {
        // Arrange
        UserEntity mockUser = new UserEntity("johndoe", "john@example.com");
        // Reflection or mock behavior to simulate save returning entity with ID
        when(userRepository.save(any(UserEntity.class))).thenReturn(mockUser);

        OutboxEventEntity mockEvent = new OutboxEventEntity("UsuarioCreado", "payload");
        when(outboxRepository.save(any(OutboxEventEntity.class))).thenReturn(mockEvent);

        // Act
        UserEntity result = userService.createUser("johndoe", "john@example.com");

        // Assert
        assertNotNull(result);
        verify(userRepository).save(any(UserEntity.class));
        verify(outboxRepository).save(any(OutboxEventEntity.class));
    }
}