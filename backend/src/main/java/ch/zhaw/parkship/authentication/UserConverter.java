package ch.zhaw.parkship.authentication;

import ch.zhaw.parkship.role.RoleEntity;
import ch.zhaw.parkship.user.UserEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class for converting a jwt object to a user authentication object.
 */
public class UserConverter
        implements Converter<Jwt, UserAuthenticationToken> {
    @Override
    public UserAuthenticationToken convert(Jwt jwt) {
        Long id = Long.parseLong(jwt.getSubject());
        String username = jwt.getClaimAsString("username");
        Set<RoleEntity> roleEntities =
                jwt.getClaimAsStringList("roles").stream().map(RoleEntity::new).collect(Collectors.toSet());
        UserEntity user = new UserEntity();
        user.setId(id);
        user.setUsername(username);
        user.setRoleEntities(roleEntities);
        return new UserAuthenticationToken(jwt, user, user.getAuthorities());
    }
}
