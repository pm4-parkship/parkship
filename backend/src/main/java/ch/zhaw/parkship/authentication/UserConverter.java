package ch.zhaw.parkship.authentication;

import ch.zhaw.parkship.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * Class for converting a jwt object to a user authentication object.
 */
@RequiredArgsConstructor
public class UserConverter
        implements Converter<Jwt, UserAuthenticationToken> {

    private final UserRepository userRepository;


    @Override
    public UserAuthenticationToken convert(Jwt jwt) {
        Long id = Long.parseLong(jwt.getSubject());
        ParkshipUserDetails parkshipUserDetails = userRepository.getParkshipUserDetailsById(id);

        return new UserAuthenticationToken(jwt, parkshipUserDetails, parkshipUserDetails.getAuthorities());
    }
}
