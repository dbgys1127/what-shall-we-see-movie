package shallwe.movie.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import shallwe.movie.security.service.UserDetailsServiceImpl;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Component
public class CustomUserAuthenticationProvider implements AuthenticationProvider {
    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public CustomUserAuthenticationProvider(UserDetailsServiceImpl userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) authentication;

        String username = authToken.getName();
        Optional.ofNullable(username).orElseThrow(() -> new UsernameNotFoundException("Invalid User name or User Password"));
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            String password = userDetails.getPassword();
            verifyCredentials(authToken.getCredentials(), password);
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            return UsernamePasswordAuthenticationToken.authenticated(username, password, authorities);
        } catch (Exception ex) {
            throw new UsernameNotFoundException(ex.getMessage()); // (1) AuthenticationException으로 다시 throw 한다.
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }

    private void verifyCredentials(Object credentials, String password) {
        if (!passwordEncoder.matches((String)credentials, password)) {
            throw new BadCredentialsException("Invalid User name or User Password");
        }
    }
}
