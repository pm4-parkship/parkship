package ch.zhaw.parkship.authentication;

import ch.zhaw.parkship.user.UserEntity;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;

/**
 * Class for a JWT token that is associated with a user and his authorities.
 */
public class UserAuthenticationToken extends AbstractAuthenticationToken {
    private final UserEntity user;
    private final Jwt jwt;

    public UserAuthenticationToken(Jwt jwt, UserEntity user, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.user = user;
        this.jwt = jwt;
    }

    @Override
    public Object getCredentials() {
        return jwt;
    }

    @Override
    public Object getPrincipal() {
        return user;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }
}
