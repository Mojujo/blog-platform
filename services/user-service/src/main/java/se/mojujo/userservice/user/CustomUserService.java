package se.mojujo.userservice.user;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.mojujo.userservice.exception.EmailAlreadyExistsException;
import se.mojujo.userservice.exception.UsernameAlreadyExistsException;
import se.mojujo.userservice.repository.CustomUserRepository;
import se.mojujo.userservice.user.dto.CustomUserCreationDTO;
import se.mojujo.userservice.user.dto.CustomUserResponseDTO;
import se.mojujo.userservice.user.mapper.CustomUserMapper;

@Service
public class CustomUserService {

    private final CustomUserRepository customUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserMapper customUserMapper;

    @Autowired
    public CustomUserService(CustomUserRepository customUserRepository, PasswordEncoder passwordEncoder, CustomUserMapper customUserMapper) {
        this.customUserRepository = customUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.customUserMapper = customUserMapper;
    }

    @Transactional
    public CustomUserResponseDTO createUser(CustomUserCreationDTO dto) {
        if (customUserRepository.existsByUsername(dto.username())) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }

        if (customUserRepository.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        CustomUser user = customUserMapper.toEntity(dto);

        user.setPassword(passwordEncoder.encode(dto.password()));

        CustomUser savedUser = customUserRepository.save(user);

        return customUserMapper.toResponseDTO(savedUser);
    }
}
