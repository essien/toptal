package com.toptal.essienntaemmanuel2ndattempt.security;

import com.toptal.essienntaemmanuel2ndattempt.domain.Role;
import com.toptal.essienntaemmanuel2ndattempt.domain.Account;
import com.toptal.essienntaemmanuel2ndattempt.service.AccountService;
import java.util.List;
import java.util.stream.Collectors;
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
public class AccountAuthProvider extends DaoAuthenticationProvider {

    private static final Logger log = LoggerFactory.getLogger(AccountAuthProvider.class);
    private static final String TOO_MANY_ATTEMPTS = "Too many login attempts. Account blocked.";
    private static final String INVALID_CREDENTIALS = "Invalid email or password";
    private static final int MAX_ATTEMPTS = 3;

    private final AccountService accountService;

    public AccountAuthProvider(AccountService accountService, UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        this.accountService = accountService;
        setUserDetailsService(userDetailsService);
        setPasswordEncoder(passwordEncoder);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            // Account is already logged in.
            return auth;
        } else {
            // Not logged in. Attempt login.
            System.out.println("Not logged in");

            Account account = accountService.findByEmail(email).orElseThrow(() -> new AuthenticationException(INVALID_CREDENTIALS) {
            });

            List<String> roles = account.getRoles().stream().map(Role::getName).collect(Collectors.toList());
            if (roles.contains(Role.ADMIN) || roles.contains(Role.USER_MANAGER))
                return super.authenticate(authentication);

            if (account.getVerificationToken().isPresent())
                throw new AuthenticationException("You cannot login until your account verification is complete.") {
                };

            // Account. Check account blocking policy.
            int loginAttempts = accountService.incrementAndGetLoginAttempts(account);
            if (loginAttempts > MAX_ATTEMPTS) {
                // Account has 3 failed login attempts.
                throw new AuthenticationException(TOO_MANY_ATTEMPTS) {
                };
            }
            try {
                final Authentication result = super.authenticate(authentication);
                accountService.resetLoginAttempts(account);
                return result;
            } catch (AuthenticationException authenticationException) {
                if (loginAttempts >= MAX_ATTEMPTS)
                    throw new AuthenticationException(TOO_MANY_ATTEMPTS) {
                    };
                else
                    throw new AuthenticationException(INVALID_CREDENTIALS) {
                    };
            }
        }
    }
}
