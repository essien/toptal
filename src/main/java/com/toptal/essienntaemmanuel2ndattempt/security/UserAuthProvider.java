package com.toptal.essienntaemmanuel2ndattempt.security;

import com.toptal.essienntaemmanuel2ndattempt.exception.NoSuchUserException;
import com.toptal.essienntaemmanuel2ndattempt.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author bodmas
 */
@Service
public class UserAuthProvider extends DaoAuthenticationProvider {

    private static final Logger log = LoggerFactory.getLogger(UserAuthProvider.class);
    private static final String TOO_MANY_ATTEMPTS = "Too many login attempts. Account blocked.";
    private static final String INVALID_CREDENTIALS = "Invalid username or password";
    private static final int MAX_ATTEMPTS = 3;

    private final UserService userService;

    public UserAuthProvider(UserService userService, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        setUserDetailsService(userDetailsService);
        setPasswordEncoder(passwordEncoder);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            // User is already logged in.
            return auth;
        } else {
            try {
                // Not logged in. Attempt login.
                System.out.println("Not logged in");
                int loginAttempts = userService.incrementAndGetLoginAttempts(username);
                if (loginAttempts > MAX_ATTEMPTS) {
                    // User has 3 failed login attempts.
                    throw new AuthenticationException(TOO_MANY_ATTEMPTS) {
                    };
                }
                try {
                    final Authentication result = super.authenticate(authentication);
                    userService.resetLoginAttempts(username);
                    return result;
                } catch (AuthenticationException authenticationException) {
                    if (loginAttempts >= MAX_ATTEMPTS)
                        throw new AuthenticationException(TOO_MANY_ATTEMPTS) {
                        };
                    else
                        throw new AuthenticationException(INVALID_CREDENTIALS) {
                        };
                }
            } catch (NoSuchUserException ex) {
                throw new AuthenticationException(INVALID_CREDENTIALS) {
                };
            }
        }
    }
}
