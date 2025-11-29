package se.mojujo.userservice.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.mojujo.userservice.exception.EmailAlreadyExistsException;
import se.mojujo.userservice.exception.UsernameAlreadyExistsException;
import se.mojujo.userservice.repository.CustomUserRepository;
import se.mojujo.userservice.user.CustomUser;
import se.mojujo.userservice.user.authority.UserRole;
import se.mojujo.userservice.user.dto.CustomUserCreationDTO;
import se.mojujo.userservice.user.dto.CustomUserResponseDTO;
import se.mojujo.userservice.user.mapper.CustomUserMapper;
import se.mojujo.userservice.util.LogUtil;

import java.util.Map;

@Service
public class CustomUserService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserService.class);

    private final CustomUserRepository customUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserMapper customUserMapper;
    private final AuditService auditService;

    @Autowired
    public CustomUserService(CustomUserRepository customUserRepository, PasswordEncoder passwordEncoder, CustomUserMapper customUserMapper, AuditService auditService) {
        this.customUserRepository = customUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.customUserMapper = customUserMapper;
        this.auditService = auditService;
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

        Map<String, Object> auditData = Map.of(
                "userId", savedUser.getId(),
                "username", savedUser.getUsername(),
                "email", savedUser.getEmail(),
                "roles", savedUser.getRoles().stream().map(UserRole::getRoleName).toList()
        );

        auditService.sendAuditEvent("USER_CREATED", auditData);

        return customUserMapper.toResponseDTO(savedUser);
    }
}
