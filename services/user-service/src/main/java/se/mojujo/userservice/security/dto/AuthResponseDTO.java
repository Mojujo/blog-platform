package se.mojujo.userservice.security.dto;

import java.util.List;

public record AuthResponseDTO(String token, List<String> roles) {}
