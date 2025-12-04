package se.mojujo.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import se.mojujo.userservice.repository.CustomUserRepository;
import se.mojujo.userservice.user.CustomUser;
import se.mojujo.userservice.user.CustomUserDetails;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomUserRepository customUserRepository;

    @Autowired
    public CustomUserDetailsService(CustomUserRepository customUserRepository) {
        this.customUserRepository = customUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        CustomUser customUser = customUserRepository.findUserByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("User not found with username: " + username)
                );

        // TODO Mapping instead of pushing to entity

        return new CustomUserDetails(customUser);
    }
}
