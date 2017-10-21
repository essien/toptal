package com.toptal.essienntaemmanuel2ndattempt.security;

import com.toptal.essienntaemmanuel2ndattempt.domain.Account;
import com.toptal.essienntaemmanuel2ndattempt.domain.Role;
import com.toptal.essienntaemmanuel2ndattempt.repository.AccountRepository;
import com.toptal.essienntaemmanuel2ndattempt.service.impl.AccountServiceImpl;
import com.toptal.essienntaemmanuel2ndattempt.service.impl.UserDetailsServiceImpl;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.RandomStringUtils;
import static org.assertj.core.api.Assertions.*;
import org.junit.Before;
import org.junit.runner.RunWith;
import static org.mockito.BDDMockito.*;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author bodmas
 */
@RunWith(MockitoJUnitRunner.class)
public class AccountAuthProviderTest {

    @Mock
    private AccountRepository accountRepository;

    private AccountServiceImpl accountService;
    private PasswordEncoder passwordEncoder;
    private UserDetailsServiceImpl userDetailsService;
    private AccountAuthProvider accountAuthProvider;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    public AccountAuthProviderTest() {
    }

    @Before
    public void setUp() {
        given(accountRepository.findByEmail(anyString())).willReturn(Optional.empty());
        given(accountRepository.save(any(Account.class))).will(AdditionalAnswers.returnsFirstArg());

        accountService = new AccountServiceImpl(accountRepository);
        passwordEncoder = new BCryptPasswordEncoder();
        userDetailsService = new UserDetailsServiceImpl(accountService, passwordEncoder);
        accountAuthProvider = new AccountAuthProvider(accountService, userDetailsService, passwordEncoder);
    }

    @Test
    public void authenticateWhenAlreadyLoggedInShouldReturnExistingAuthentication() {
        String email = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomAlphabetic(10);
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Authentication result = accountAuthProvider.authenticate(authentication);
        assertThat(result).isEqualTo(authentication);
    }

    @Test
    public void authenticateWhenAccountNotFoundShouldThrowException() {
        String email = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomAlphabetic(10);
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);

        SecurityContextHolder.getContext().setAuthentication(null);
        given(accountRepository.findByEmail(anyString())).willReturn(Optional.empty());

        thrown.expect(AuthenticationException.class);

        accountAuthProvider.authenticate(authentication);
    }

    @Test
    public void authenticateWhenPrincipalIsAdminOrManagerShouldReturnAuthenticationImmediately() {
        String email = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomAlphabetic(10);
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        Account account = new Account();
        account.setEmail(email);
        account.setPassword(passwordEncoder.encode(password));
        account.setRoles(Arrays.asList("ADMIN").stream().map(Role::new).collect(Collectors.toList()));

        SecurityContextHolder.getContext().setAuthentication(null);
        given(accountRepository.findByEmail(anyString())).willReturn(Optional.of(account));

        Authentication result = accountAuthProvider.authenticate(authentication);
        assertThat(result.getName()).isEqualTo(authentication.getName());

        account.setRoles(Arrays.asList("USER-MANAGER").stream().map(Role::new).collect(Collectors.toList()));
        Authentication result2 = accountAuthProvider.authenticate(authentication);
        assertThat(result2.getName()).isEqualTo(authentication.getName());
    }

    @Test
    public void authenticateWhenUserAccountNotVerifiedShouldThrowException() {
        String email = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomAlphabetic(10);
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        Account account = new Account();
        account.setEmail(email);
        account.setPassword(passwordEncoder.encode(password));
        account.setRoles(Arrays.asList("USER").stream().map(Role::new).collect(Collectors.toList()));
        account.setVerificationToken(RandomStringUtils.randomAlphabetic(10));

        SecurityContextHolder.getContext().setAuthentication(null);
        given(accountRepository.findByEmail(anyString())).willReturn(Optional.of(account));

        thrown.expect(AuthenticationException.class);
        thrown.expectMessage("You cannot login until your account verification is complete.");

        accountAuthProvider.authenticate(authentication);
    }

    @Test
    public void authenticateWhenUserSigninIsAttemptedShouldCheckAccountBlockingPolicy() {
        String email = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomAlphabetic(10);
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        Account account = new Account();
        account.setEmail(email);
        account.setPassword(passwordEncoder.encode(password));
        account.setRoles(Arrays.asList("USER").stream().map(Role::new).collect(Collectors.toList()));
        account.setLoginAttempts(5);

        SecurityContextHolder.getContext().setAuthentication(null);
        given(accountRepository.findByEmail(anyString())).willReturn(Optional.of(account));

        thrown.expect(AuthenticationException.class);
        thrown.expectMessage("Too many login attempts. Account blocked.");

        accountAuthProvider.authenticate(authentication);
    }

    @Test
    public void authenticateWhenUserAuthenticationFailsAndFailedAttemptsReachedShouldThrowException() { // E.g. Bad credentials.
        String email = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomAlphabetic(10);
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        Account account = new Account();
        account.setEmail(email);
        // Password is omitted so that authentication would fail.
        account.setRoles(Arrays.asList("USER").stream().map(Role::new).collect(Collectors.toList()));
        account.setLoginAttempts(2); // Assuming 3 is the maximum number of failed attempts.

        SecurityContextHolder.getContext().setAuthentication(null);
        given(accountRepository.findByEmail(anyString())).willReturn(Optional.of(account));

        thrown.expect(AuthenticationException.class);
        thrown.expectMessage("Too many login attempts. Account blocked.");

        accountAuthProvider.authenticate(authentication);
    }

    @Test
    public void authenticateWhenUserCredentialsAreInvalidShouldThrowException() { // E.g. Bad credentials.
        String email = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomAlphabetic(10);
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        Account account = new Account();
        account.setEmail(email);
        // Password is omitted so that authentication would fail.
        account.setRoles(Arrays.asList("USER").stream().map(Role::new).collect(Collectors.toList()));
        account.setLoginAttempts(1); // Assuming 3 is the maximum number of failed attempts.

        SecurityContextHolder.getContext().setAuthentication(null);
        given(accountRepository.findByEmail(anyString())).willReturn(Optional.of(account));

        thrown.expect(AuthenticationException.class);
        thrown.expectMessage("Invalid email or password");

        accountAuthProvider.authenticate(authentication);
    }

    @Test
    public void authenticateShouldResetLoginAttemptsAndReturnAuthentication() {
        String email = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomAlphabetic(10);
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        Account account = new Account();
        account.setEmail(email);
        account.setPassword(passwordEncoder.encode(password));
        account.setRoles(Arrays.asList("USER").stream().map(Role::new).collect(Collectors.toList()));
        account.setLoginAttempts(2); // Assuming 3 is the maximum number of failed attempts.

        SecurityContextHolder.getContext().setAuthentication(null);
        given(accountRepository.findByEmail(anyString())).willReturn(Optional.of(account));

        Authentication result = accountAuthProvider.authenticate(authentication);
        assertThat(account.getLoginAttempts()).isEqualTo(0);
        assertThat(result).isInstanceOf(UsernamePasswordAuthenticationToken.class);
    }
}
