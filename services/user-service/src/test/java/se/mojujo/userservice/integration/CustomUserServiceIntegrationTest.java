package se.mojujo.userservice.integration;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import se.mojujo.userservice.repository.CustomUserRepository;
import se.mojujo.userservice.security.JwtUtils;
import se.mojujo.userservice.service.AuditService;
import se.mojujo.userservice.service.CustomUserService;
import se.mojujo.userservice.user.authority.UserRole;
import se.mojujo.userservice.user.dto.CustomUserCreationDTO;
import se.mojujo.userservice.user.dto.CustomUserResponseDTO;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Transactional
@Testcontainers
@ActiveProfiles("test")
public class CustomUserServiceIntegrationTest {

    @Container
    @SuppressWarnings("all")
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    }

    @Autowired
    private CustomUserService customUserService;

    @Autowired
    private CustomUserRepository customUserRepository;

    @MockitoBean
    private AuditService auditService;

    @MockitoBean
    private JwtUtils jwtUtils;

    @Test
    void createUserIntegration() {
        CustomUserCreationDTO dto = new CustomUserCreationDTO(
                "oscar_integration",
                "oscar@integration.com",
                "Password1!",
                Set.of(UserRole.USER)
        );

        CustomUserResponseDTO response = customUserService.createUser(dto);

        assertNotNull(response.id());
        assertEquals("oscar_integration", response.username());
        assertEquals("oscar@integration.com", response.email());
        assertTrue(response.roles().contains("ROLE_USER"));
        assertEquals(1,customUserRepository.count());
        verify(auditService).sendAuditEvent(eq("USER_CREATED"), anyMap());
    }

    @Test
    void createUserDefaultBooleansTrue() {
        CustomUserCreationDTO dto = new CustomUserCreationDTO(
                "integration_user2",
                "integration2@example.com",
                "Password1!",
                Set.of(UserRole.USER)
        );

        customUserService.createUser(dto);
        var userEntity = customUserRepository.findUserByUsername("integration_user2").orElseThrow();

        assertTrue(userEntity.isAccountNonExpired());
        assertTrue(userEntity.isAccountNonLocked());
        assertTrue(userEntity.isCredentialsNonExpired());
        assertTrue(userEntity.isEnabled());
    }
}
