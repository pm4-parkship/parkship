package ch.zhaw.parkship.user;


import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class ParkshipUserDetails implements UserDetails {
    private final Long id;
    private final String username;
    private final String name;
    private final String surname;
    private final String password;
    private final UserRole userRole;
    private final UserState userState;

    // Need explicit Constructor cause of Repository -> No lombok
    public ParkshipUserDetails(Long id, String email, String name, String surname, String password, UserRole userRole, UserState userState) {
        this.id = id;
        this.username = email;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.userRole = userRole;
        this.userState = userState;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(getUserRole().name()));
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

    public String getEmail(){
        return getUsername();
    }
}
