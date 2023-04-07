package ch.zhaw.parkship.authentication;

import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * Class for a JWT token that is associated with a user and his authorities.
 */
public class ApplicationUserAuthenticationToken extends AbstractAuthenticationToken {
    private final ApplicationUser user;
    private final Jwt jwt;

    public ApplicationUserAuthenticationToken(Jwt jwt, ApplicationUser user, Collection<? extends GrantedAuthority> authorities) {
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
