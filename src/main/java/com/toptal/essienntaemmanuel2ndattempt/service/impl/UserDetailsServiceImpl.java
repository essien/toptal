package com.toptal.essienntaemmanuel2ndattempt.service.impl;

import com.toptal.essienntaemmanuel2ndattempt.domain.Role;
import com.toptal.essienntaemmanuel2ndattempt.domain.Account;
import com.toptal.essienntaemmanuel2ndattempt.security.ToptalAccountPrincipal;
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
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final AccountServiceImpl accountService;

    public UserDetailsServiceImpl(AccountServiceImpl accountService, PasswordEncoder passwordEncoder) {
        this.accountService = accountService;
        createDefaultAccountsIfAbsent(accountService, passwordEncoder);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("email = " + email);
        Optional<Account> optAccount = accountService.findByEmail(email);
        final Account account = optAccount.orElseThrow(() -> new UsernameNotFoundException(email));
        return new ToptalAccountPrincipal(account);
    }

    private void createDefaultAccountsIfAbsent(AccountServiceImpl accountService, PasswordEncoder passwordEncoder) {
        if (accountService.count() == 0) {
            accountService.save(new Account("admin", passwordEncoder.encode("admin"), Role.ADMIN));
            accountService.save(new Account("manager", passwordEncoder.encode("manager"), Role.USER_MANAGER));
        }
    }
}
