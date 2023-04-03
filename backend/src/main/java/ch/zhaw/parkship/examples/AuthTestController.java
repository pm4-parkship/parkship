package ch.zhaw.parkship.examples;

import ch.zhaw.parkship.authentication.ApplicationUser;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@SecurityRequirement(name = "Bearer Authentication")
public class AuthTestController {
    @GetMapping("/user")
    //@PreAuthorize("hasRole('ROLE_USER')") // doesn't work?
    //@PreAuthorize("hasRole('USER')") // doesn't work?
    //@PreAuthorize("authentication.principal.id == 1") // works
    //@PreAuthorize("hasAuthority('USER')") // works
    //@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')") // works
    @Secured("USER") // works
    //@Secured({ "USER", "ADMIN" }) // works
    public ApplicationUser allowUser(@AuthenticationPrincipal ApplicationUser user) {
        return user;
    }

    @GetMapping("/admin")
    @Secured("ADMIN")
    public ApplicationUser allowAdmin(@AuthenticationPrincipal ApplicationUser user) {
        return user;
    }
}
