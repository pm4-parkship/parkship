package ch.zhaw.parkship.authentication;

import ch.zhaw.parkship.configuration.JwtConfiguration;
import ch.zhaw.parkship.user.ParkshipUserDetails;
import ch.zhaw.parkship.user.UserRole;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Service for generating and verifying JWTs.
 */
@Service
public class JwtService {
    private final JwtConfiguration jwtConfiguration;

    public JwtService(@Lazy JwtConfiguration jwtConfiguration) {
        this.jwtConfiguration = jwtConfiguration;
    }

    /**
     * Generates and configures a JWT for a specific user.
     *
     * @param parkshipUserDetails the JWT is generated for
     * @return a signed JWT
     */
    public String generateToken(ParkshipUserDetails parkshipUserDetails) {
        UserRole roles = UserRole.ADMIN;

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(parkshipUserDetails.getId() + "")
                .claim("username", parkshipUserDetails.getUsername())
                .claim("role", roles)
                .expirationTime(new Date(new Date().getTime() + jwtConfiguration.getExpiration()))
                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
        try {
            JWSSigner signer = new MACSigner(jwtConfiguration.getSecret());
            signedJWT.sign(signer);
            return signedJWT.serialize();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Verifies if a token is a valid JWT.
     *
     * @param token that to be verified
     * @return true, if the token is a  valid JWT
     */
    public SignedJWT verify(String token) {
        SignedJWT signedJWT = null;
        try {
            signedJWT = SignedJWT.parse(token);
            boolean isValid = signedJWT.verify(new MACVerifier(jwtConfiguration.getSecret()));
            return isValid ? signedJWT : null;
        } catch (Exception e) {
            return null;
        }
    }
}
