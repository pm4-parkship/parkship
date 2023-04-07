package ch.zhaw.parkship.configuration;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import ch.zhaw.parkship.authentication.ApplicationUserConverter;
import ch.zhaw.parkship.authentication.ApplicationUserService;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class ApplicationConfiguration {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.algorithm}")
    private String algorithm;
    @Value("${app.allowed-urls}")
    private String[] allowedUrls;
    @Value("${app.auth-urls}")
    private String[] authUrls;

    private final UserDetailsService userDetailsService;

    @Autowired
    public ApplicationConfiguration(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authenticationProvider(authenticationProvider());
        http.oauth2ResourceServer().jwt(c -> {
            c.jwtAuthenticationConverter(new ApplicationUserConverter());
        });
        http.anonymous();
        http.cors().configurationSource(corsConfigurationSource());
        http.csrf().disable();
        http.exceptionHandling();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.headers().frameOptions().disable();
        http.authorizeHttpRequests()
                .requestMatchers(toH2Console()).permitAll()
                .requestMatchers(allowedUrls).permitAll()
                .requestMatchers(HttpMethod.POST, authUrls).permitAll()
                .anyRequest()
                .authenticated();
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(((ApplicationUserService)userDetailsService).getPasswordEncoder());
        return authProvider;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }


    @Bean
    public JwtDecoder customDecoder() {
        return NimbusJwtDecoder
                .withSecretKey(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), algorithm))
                .build();
    }
}
