package ch.zhaw.parkship.util;

import ch.zhaw.parkship.authentication.UserAuthenticationToken;
import ch.zhaw.parkship.user.ParkshipUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class ParkshipUserDetailsContext {

    private ParkshipUserDetailsContext() {
    }
    public static UserAuthenticationToken getCurrentUserAuthenticationToken() {
        return (UserAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    }
    public static ParkshipUserDetails getCurrentParkshipUserDetails() {
        return (ParkshipUserDetails) getCurrentUserAuthenticationToken().getPrincipal();
    }


}
