package se.mojujo.userservice.integration;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import se.mojujo.userservice.repository.CustomUserRepository;
import se.mojujo.userservice.service.CustomUserService;
import se.mojujo.userservice.user.authority.UserRole;
import se.mojujo.userservice.user.dto.CustomUserCreationDTO;
import se.mojujo.userservice.user.dto.CustomUserResponseDTO;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Testcontainers
public class CustomUserServiceIntegrationTest {

    @Container
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
    }

    @Test
    void createUser_defaultBooleans_true() {
        CustomUserCreationDTO dto = new CustomUserCreationDTO(
                "integration_user2",
                "integration2@example.com",
                "Password1!",
                Set.of(UserRole.USER)
        );

        var savedUser = customUserService.createUser(dto);
        var userEntity = customUserRepository.findUserByUsername("integration_user2").orElseThrow();

        assertTrue(userEntity.isAccountNonExpired());
        assertTrue(userEntity.isAccountNonLocked());
        assertTrue(userEntity.isCredentialsNonExpired());
        assertTrue(userEntity.isEnabled());
    }
}
