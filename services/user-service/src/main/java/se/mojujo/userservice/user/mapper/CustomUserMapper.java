package se.mojujo.userservice.user.mapper;

import org.springframework.stereotype.Component;
import se.mojujo.userservice.user.CustomUser;
import se.mojujo.userservice.user.authority.UserRole;
import se.mojujo.userservice.user.dto.CustomUserCreationDTO;
import se.mojujo.userservice.user.dto.CustomUserResponseDTO;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CustomUserMapper {

    public CustomUser toEntity(CustomUserCreationDTO dto) {

        return new CustomUser(
                dto.username(),
                dto.email(),
                dto.password(),
                dto.roles()
        );
    }

    public CustomUserResponseDTO toResponseDTO(CustomUser customUser) {

        Set<String> roles = customUser.getRoles().stream()
                .map(UserRole::getRoleName)
                .collect(Collectors.toSet());

        return new CustomUserResponseDTO(
                customUser.getId(),
                customUser.getUsername(),
                customUser.getEmail(),
                roles
        );
    }
}
