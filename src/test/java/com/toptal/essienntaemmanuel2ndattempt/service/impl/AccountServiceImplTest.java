package com.toptal.essienntaemmanuel2ndattempt.service.impl;

import com.toptal.essienntaemmanuel2ndattempt.domain.Account;
import com.toptal.essienntaemmanuel2ndattempt.exception.DuplicateAccountException;
import com.toptal.essienntaemmanuel2ndattempt.repository.AccountRepository;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
import org.springframework.data.domain.Pageable;

/**
 *
 * @author bodmas
 */
@RunWith(MockitoJUnitRunner.class)
public class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    private AccountServiceImpl accountService;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    public AccountServiceImplTest() {
    }

    @Before
    public void setUp() {
        accountService = new AccountServiceImpl(accountRepository);
    }

    @Test
    public void saveWithTokenShouldGenerateNewTokenAndSave() {
        final String email = RandomStringUtils.randomAlphabetic(10);
        final Account account = new Account();
        account.setEmail(email);

        given(accountRepository.findByEmail(anyString())).willReturn(Optional.empty());
        given(accountRepository.save(any(Account.class))).will(AdditionalAnswers.returnsFirstArg());

        Account result = accountService.saveWithToken(account);
        assertThat(result.getEmail()).isEqualTo(account.getEmail());
        assertThat(result.getVerificationToken()).isNotNull();
    }

    @Test
    public void saveWhenAccountClashesWithExistingShouldThrowException() {
        final String email = RandomStringUtils.randomAlphabetic(10);
        final Account account = new Account();
        account.setEmail(email);

        given(accountRepository.findByEmail(anyString())).willReturn(Optional.of(account));

        thrown.expect(DuplicateAccountException.class);
        thrown.expectMessage("An account already exists with email " + email);

        accountService.save(account);
    }

    @Test
    public void saveWhenAccountNotFoundShouldSave() {
        final String email = RandomStringUtils.randomAlphabetic(10);
        final Account account = new Account();
        account.setEmail(email);

        given(accountRepository.findByEmail(anyString())).willReturn(Optional.empty());
        given(accountRepository.save(any(Account.class))).will(AdditionalAnswers.returnsFirstArg());

        Account result = accountService.save(account);
        assertThat(result.getEmail()).isEqualTo(account.getEmail());
    }

    @Test
    public void saveWhenAccountIsDetachedShouldUpdate() {
        final String email = RandomStringUtils.randomAlphabetic(10);
        final Account account = new Account();
        account.setId((long) (Math.random() * Long.MAX_VALUE));
        account.setEmail(email);

        given(accountRepository.findByEmail(anyString())).willReturn(Optional.of(account));
        given(accountRepository.save(any(Account.class))).will(AdditionalAnswers.returnsFirstArg());

        Account result = accountService.save(account);
        assertThat(result.getEmail()).isEqualTo(account.getEmail());
        assertThat(result.getId()).isEqualByComparingTo(account.getId());
    }

    @Test
    public void findByEmailShouldReturnOptionalAccount() {
        String email = RandomStringUtils.randomAlphabetic(10);
        final Account account = new Account();
        account.setEmail(email);

        given(accountRepository.findByEmail(anyString())).willReturn(Optional.of(account));

        Optional<Account> result = accountService.findByEmail(email);
        assertThat(result).isNotNull();
        assertThat(result.get().getEmail()).isEqualTo(account.getEmail());
    }

    @Test
    public void countShouldReturnNumberOfRegisteredAccounts() {
        long expResult = (long) (Math.random() * Long.MAX_VALUE);

        given(accountRepository.count()).willReturn(expResult);

        long result = accountService.count();
        assertThat(result).isEqualTo(expResult);
    }

    @Test
    public void incrementAndGetLoginAttemptsShouldReturnExpectedResult() throws Exception {

        String email = RandomStringUtils.randomAlphabetic(10);
        final int initialLoginCount = (int) (Math.random() * (Integer.MAX_VALUE - 1));
        final Account account = new Account();
        account.setEmail(email);
        account.setLoginAttempts(initialLoginCount);

        given(accountRepository.findByEmail(anyString())).willReturn(Optional.of(account));

        int result = accountService.incrementAndGetLoginAttempts(email);
        assertThat(result).isEqualTo(initialLoginCount + 1);
    }

    @Test
    public void resetLoginAttemptsShouldResetLoginAttempts() throws Exception {

        String email = RandomStringUtils.randomAlphabetic(10);
        final int initialLoginCount = (int) (Math.random() * (Integer.MAX_VALUE - 1));
        final Account account = new Account();
        account.setEmail(email);
        account.setLoginAttempts(initialLoginCount);

        given(accountRepository.findByEmail(anyString())).willReturn(Optional.of(account));

        accountService.resetLoginAttempts(email);
        assertThat(account.getLoginAttempts()).isEqualTo(0);
    }

    @Test
    public void findAllShouldReturnAllAccounts() {
        List<Account> expResult = Arrays.asList(new Account(), new Account(), new Account());

        given(accountRepository.findAll()).willReturn(expResult);

        List<Account> result = accountService.findAll();
        assertThat(result).containsExactlyElementsOf(expResult);
    }

    @Test
    public void findAllWithOffsetAndSizeShouldReturnMatchedAccounts() {
        List<Account> expResult = Arrays.asList(new Account(), new Account(), new Account());
        int offset = (int) (Math.random() * (Integer.MAX_VALUE - 1));
        int size = (int) (Math.random() * (Integer.MAX_VALUE - 1));

        given(accountRepository.findAccountsBy(any(Pageable.class))).willReturn(expResult);

        List<Account> result = accountService.findAll(offset, size);
        assertThat(result).containsExactlyElementsOf(expResult);
    }

    @Test
    public void testUpdateExpectedNumberOfCalories() throws Exception {
        Account account = new Account();
        String email = RandomStringUtils.randomAlphabetic(10);
        BigDecimal expectedNumCalories = BigDecimal.valueOf(Math.random() * Double.MAX_VALUE);

        given(accountRepository.findByEmail(anyString())).willReturn(Optional.of(account));

        accountService.updateExpectedNumberOfCalories(email, expectedNumCalories);
        assertThat(account.getSettings().getExpectedNumberOfCalories()).isEqualTo(expectedNumCalories);
    }
}
