package com.toptal.essienntaemmanuel2ndattempt.service;

import com.toptal.essienntaemmanuel2ndattempt.domain.Account;
import com.toptal.essienntaemmanuel2ndattempt.exception.DuplicateAccountException;
import com.toptal.essienntaemmanuel2ndattempt.exception.NoSuchAccountException;
import com.toptal.essienntaemmanuel2ndattempt.repository.AccountRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author bodmas
 */
@Service
@Transactional
public class AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountService.class);

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account saveWithToken(Account account) {
        generateToken(account);
        return save(account);
    }

    public Account save(Account account) {
        accountRepository.findByEmail(account.getEmail()).ifPresent(existingAccount -> {
            if (account.getId() == null)
                throw new DuplicateAccountException(existingAccount.getEmail());
        });

        final Account newAccount = accountRepository.save(account);
        return newAccount;
    }

    public Optional<Account> findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    public long count() {
        return accountRepository.count();
    }

    public int incrementAndGetLoginAttempts(Account account) {
        int newLoginAttempts = account.getLoginAttempts() + 1;
        account.setLoginAttempts(newLoginAttempts);
        accountRepository.save(account);
        return newLoginAttempts;
    }

    private Account getByEmail(String email) throws NoSuchAccountException {
        return findByEmail(email).orElseThrow(() -> new NoSuchAccountException(email));
    }

    public void resetLoginAttempts(Account account) {
        account.setLoginAttempts(0);
        accountRepository.save(account);
    }

    public void generateToken(Account account) {
        account.setVerificationToken(RandomStringUtils.randomAlphanumeric(64));
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public void updateExpectedNumberOfCalories(String email, BigDecimal expectedNumCalories) throws NoSuchAccountException {
        Account account = getByEmail(email);
        account.getSettings().setExpectedNumberOfCalories(expectedNumCalories);
    }
}
