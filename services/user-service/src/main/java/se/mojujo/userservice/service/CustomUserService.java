package se.mojujo.userservice.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.mojujo.userservice.exception.EmailAlreadyExistsException;
import se.mojujo.userservice.exception.InvalidCredentialsException;
import se.mojujo.userservice.exception.UserNotFoundException;
import se.mojujo.userservice.exception.UsernameAlreadyExistsException;
import se.mojujo.userservice.repository.CustomUserRepository;
import se.mojujo.userservice.user.CustomUser;
import se.mojujo.userservice.user.authority.UserRole;
import se.mojujo.userservice.user.dto.CustomUserCreationDTO;
import se.mojujo.userservice.user.dto.CustomUserResponseDTO;
import se.mojujo.userservice.user.mapper.CustomUserMapper;
import se.mojujo.userservice.util.LogUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomUserService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserService.class);

    private final CustomUserRepository customUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserMapper customUserMapper;
    private final RabbitService rabbitService;

    @Autowired
    public CustomUserService(CustomUserRepository customUserRepository, PasswordEncoder passwordEncoder, CustomUserMapper customUserMapper, RabbitService rabbitService) {
        this.customUserRepository = customUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.customUserMapper = customUserMapper;
        this.rabbitService = rabbitService;
    }

    @Transactional
    public CustomUserResponseDTO createUser(CustomUserCreationDTO dto) {

        LogUtil.info(logger, "USER_CREATION_ATTEMPT", null, "username", dto.username());

        if (customUserRepository.existsByUsername(dto.username())) {
            LogUtil.warn(logger, "USERNAME_EXISTS", null, "username", dto.username());
            throw new UsernameAlreadyExistsException("Username already exists");
        }

        if (customUserRepository.existsByEmail(dto.email())) {
            LogUtil.warn(logger, "EMAIL_EXISTS", null, "email", dto.email());
            throw new EmailAlreadyExistsException("Email already exists");
        }

        CustomUser user = customUserMapper.toEntity(dto);

        user.setPassword(passwordEncoder.encode(dto.password()));

        CustomUser savedUser = customUserRepository.save(user);

        LogUtil.info(logger,
                "USER_CREATED",
                "User created successfully",
                "userId", savedUser.getId(), "username", savedUser.getUsername());

        Map<String, Object> auditData = new HashMap<>();
        auditData.put("userId", savedUser.getId());
        auditData.put("username", savedUser.getUsername());
        auditData.put("email", savedUser.getEmail());
        auditData.put("roles", savedUser.getRoles().stream().map(UserRole::getRoleName).collect(Collectors.toSet()));

        rabbitService.sendAuditEvent("USER_CREATED", auditData);

        return customUserMapper.toResponseDTO(savedUser);
    }

    @Transactional
    public void changeUsername(UUID userId, String newUsername) {
        CustomUser user = customUserRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (newUsername.equals(user.getUsername())) {
            return;
        }

        if (customUserRepository.existsByUsername(newUsername)) {
            throw new UsernameAlreadyExistsException("Username already taken");
        }

        user.setUsername(newUsername);
        customUserRepository.save(user);

        LogUtil.info(logger,
                "USERNAME_CHANGED",
                "Username changed successfully",
                "userId", user.getId(), "New Username", newUsername);

        // Publish events
        rabbitService.sendUsernameChangedEvent(userId, newUsername);
        rabbitService.sendAuditEvent(
                "USERNAME_CHANGED",
                Map.of("userId", user.getId(), "newUsername", newUsername)
        );
    }

    @Transactional
    public void changeEmail(UUID userId, String newEmail) {
        CustomUser user = customUserRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (newEmail.equals(user.getEmail())) {
            return;
        }

        if (customUserRepository.existsByEmail(newEmail)) {
            throw new EmailAlreadyExistsException("Email already taken");
        }

        user.setEmail(newEmail);
        customUserRepository.save(user);

        LogUtil.info(logger,
                "EMAIL_CHANGED",
                "Email changed successfully",
                "userId", user.getId(), "New Email", newEmail);

        rabbitService.sendAuditEvent(
                "EMAIL_CHANGED",
                Map.of("userId", user.getId(), "newEmail", newEmail)
        );
    }

    @Transactional
    public void changePassword(UUID userId, String oldPassword, String newPassword) {
        CustomUser user = customUserRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        customUserRepository.save(user);

        LogUtil.info(logger,
                "PASSWORD_CHANGED",
                "Password changed successfully",
                "userId", user.getId());

        rabbitService.sendAuditEvent("PASSWORD_CHANGED",
                Map.of("userId", user.getId()));
    }
}
