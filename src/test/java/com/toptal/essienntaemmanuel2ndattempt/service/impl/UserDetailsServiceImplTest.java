package com.toptal.essienntaemmanuel2ndattempt.service.impl;

import com.toptal.essienntaemmanuel2ndattempt.domain.Account;
import java.util.Optional;
import org.apache.commons.lang3.RandomStringUtils;
import static org.assertj.core.api.Assertions.*;
import org.junit.Before;
import org.junit.runner.RunWith;
import static org.mockito.BDDMockito.*;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author bodmas
 */
@RunWith(MockitoJUnitRunner.class)
public class UserDetailsServiceImplTest {

    @Mock
    private AccountServiceImpl accountService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserDetailsServiceImpl userDetailsService;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    public UserDetailsServiceImplTest() {
    }

    @Before
    public void setUp() {
        userDetailsService = new UserDetailsServiceImpl(accountService, passwordEncoder);
    }

    @Test
    public void loadUserByUsernameWhenAccountNotFoundShouldThrowException() {
        String email = RandomStringUtils.randomAlphabetic(10);

        given(accountService.findByEmail(anyString())).willReturn(Optional.empty());

        thrown.expect(UsernameNotFoundException.class);

        userDetailsService.loadUserByUsername(email);
    }

    @Test
    public void loadUserByUsernameShouldReturnUserDetails() {
        String email = RandomStringUtils.randomAlphabetic(10);
        final Account account = new Account();
        account.setEmail(email);

        given(accountService.findByEmail(anyString())).willReturn(Optional.of(account));

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        assertThat(userDetails.getUsername()).isEqualTo(email);
    }
}
