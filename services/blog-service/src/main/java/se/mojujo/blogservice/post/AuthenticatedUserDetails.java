package se.mojujo.blogservice.post;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class AuthenticatedUserDetails implements UserDetails {

    private final UUID userId;
    private final String username;
    private final Set<GrantedAuthority> authorities;

    public AuthenticatedUserDetails(UUID userId, String username, Set<GrantedAuthority> authorities) {
        this.userId = userId;
        this.username = username;
        this.authorities =  authorities;
    }

    public UUID getUserId() {
        return userId;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null; // Password not needed
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
