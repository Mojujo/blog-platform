package se.mojujo.userservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import se.mojujo.userservice.exception.EmailAlreadyExistsException;
import se.mojujo.userservice.exception.UsernameAlreadyExistsException;
import se.mojujo.userservice.repository.CustomUserRepository;
import se.mojujo.userservice.user.CustomUser;
import se.mojujo.userservice.user.authority.UserRole;
import se.mojujo.userservice.user.dto.CustomUserCreationDTO;
import se.mojujo.userservice.user.dto.CustomUserResponseDTO;
import se.mojujo.userservice.user.mapper.CustomUserMapper;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomUserServiceTest {

    @Mock
    private CustomUserRepository customUserRepository;

    @Mock
    private CustomUserMapper customUserMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CustomUserService customUserService;

    @Test
    void createUserTest() {
        CustomUserCreationDTO dto = new CustomUserCreationDTO(
                "oscar",
                "oscar@test.com",
                "Password1!",
                Set.of(UserRole.USER)
        );

        CustomUser entity = new CustomUser(
                dto.username(),
                dto.email(),
                "encodedPassword",
                Set.of(UserRole.USER)
        );

        when(customUserRepository.existsByUsername(dto.username())).thenReturn(false);
        when(customUserRepository.existsByEmail(dto.email())).thenReturn(false);
        when(customUserMapper.toEntity(dto)).thenReturn(entity);
        when(passwordEncoder.encode(dto.password())).thenReturn("encodedPassword");
        when(customUserRepository.save(entity)).thenReturn(entity);
        when(customUserMapper.toResponseDTO(entity)).thenCallRealMethod();

        CustomUserResponseDTO response = customUserService.createUser(dto);

        assertEquals(dto.username(), response.username());
        assertEquals(dto.email(), response.email());
        assertTrue(response.roles().contains("ROLE_USER"));
        verify(customUserRepository, times(1)).save(entity);
    }

    @Test
    void createUserWithExistingUsernameTest() {
        CustomUserCreationDTO dto = new CustomUserCreationDTO(
                "oscar",
                "oscar@test.com",
                "Password1!",
                Set.of(UserRole.USER)
        );

        when(customUserRepository.existsByUsername(dto.username())).thenReturn(true);

        assertThrows(UsernameAlreadyExistsException.class, () -> customUserService.createUser(dto));
    }

    @Test
    void createUserWithExistingEmailTest() {
        CustomUserCreationDTO dto = new CustomUserCreationDTO(
                "oscar",
                "oscar@test.com",
                "Password1!",
                Set.of(UserRole.USER)
        );

        when(customUserRepository.existsByUsername(dto.username())).thenReturn(false);
        when(customUserRepository.existsByEmail(dto.email())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> customUserService.createUser(dto));
    }
}
