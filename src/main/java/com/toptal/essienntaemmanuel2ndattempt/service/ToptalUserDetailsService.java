package com.toptal.essienntaemmanuel2ndattempt.service;

import com.toptal.essienntaemmanuel2ndattempt.domain.Role;
import com.toptal.essienntaemmanuel2ndattempt.domain.User;
import com.toptal.essienntaemmanuel2ndattempt.security.ToptalUserPrincipal;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author bodmas
 */
@Service
public class ToptalUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(ToptalUserDetailsService.class);

    private final UserService userService;

    public ToptalUserDetailsService(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        createDefaultUsersIfAbsent(userService, passwordEncoder);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("username = " + username);
        Optional<User> optUser = userService.findByUsername(username);
        final User user = optUser.orElseThrow(() -> new UsernameNotFoundException(username));
        return new ToptalUserPrincipal(user);
    }

    private void createDefaultUsersIfAbsent(UserService userService, PasswordEncoder passwordEncoder) {
        if (userService.count() == 0) {
            userService.save(new User("admin", passwordEncoder.encode("admin"), Role.ADMIN));
            userService.save(new User("manager", passwordEncoder.encode("manager"), Role.USER_MANAGER));
        }
    }
}
