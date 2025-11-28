package se.mojujo.userservice.user.authority;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

import static se.mojujo.userservice.user.authority.UserPermission.*;

public enum UserRole {

    GUEST(
            UserRoleName.GUEST.getRoleName(),
            Set.of(
                    READ
            )
    ),

    USER(
            UserRoleName.USER.getRoleName(),
            Set.of(
                    READ,
                    WRITE
            )
    ),

    ADMIN(
            UserRoleName.ADMIN.getRoleName(),
            Set.of(
                    READ,
                    WRITE,
                    DELETE
            )
    );

    private final String roleName;
    private final Set<UserPermission> userPermissions;

    UserRole(String roleName, Set<UserPermission> userPermissions) {
        this.roleName = roleName;
        this.userPermissions = userPermissions;
    }

    public String getRoleName() {
        return roleName;
    }

    public Set<UserPermission> getUserPermissions() {
        return userPermissions;
    }

    // Get a LIST that Spring understands - containing both ROLE + PERMISSION
    public List<SimpleGrantedAuthority> getUserAuthorities() {

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        // this == the choice made after UserRole. (e.g: UserRole.ADMIN)
        authorityList.add(new SimpleGrantedAuthority(this.roleName));
        authorityList.addAll(
                this.userPermissions.stream().map(
                        userPermission -> new SimpleGrantedAuthority(userPermission.getUserPermission())
                ).toList()
        );

        return authorityList;
    }

}
