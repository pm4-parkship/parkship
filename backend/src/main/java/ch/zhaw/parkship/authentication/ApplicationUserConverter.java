package ch.zhaw.parkship.authentication;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class for converting a jwt object to a user authentication object.
 */
public class ApplicationUserConverter implements Converter<Jwt, ApplicationUserAuthenticationToken> {
    @Override
    public ApplicationUserAuthenticationToken convert(Jwt jwt) {
        int id =  Integer.parseInt(jwt.getSubject());
        String username = jwt.getClaimAsString("username");
        Set<Role> roles = jwt.getClaimAsStringList("roles").stream().map(Role::new).collect(Collectors.toSet());
        ApplicationUser user = new ApplicationUser();
        user.setId(id);
        user.setUsername(username);
        user.setRoles(roles);
        return new ApplicationUserAuthenticationToken(jwt, user, user.getAuthorities());
    }
}
