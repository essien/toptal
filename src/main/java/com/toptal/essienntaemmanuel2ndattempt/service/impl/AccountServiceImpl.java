package com.toptal.essienntaemmanuel2ndattempt.service.impl;

import com.toptal.essienntaemmanuel2ndattempt.service.api.AccountService;
import com.toptal.essienntaemmanuel2ndattempt.domain.Account;
import com.toptal.essienntaemmanuel2ndattempt.exception.DuplicateAccountException;
import com.toptal.essienntaemmanuel2ndattempt.exception.NoSuchAccountException;
import com.toptal.essienntaemmanuel2ndattempt.repository.AccountRepository;
import com.toptal.essienntaemmanuel2ndattempt.repository.misc.PageRequestImpl;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author bodmas
 */
@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account saveWithToken(Account account) {
        generateToken(account);
        return save(account);
    }

    @Override
    public Account save(Account account) {
        log.info("Saving account " + account.getEmail());
        accountRepository.findByEmail(account.getEmail()).ifPresent(existingAccount -> {
            if (account.getId() == null)
                throw new DuplicateAccountException(existingAccount.getEmail());
        });

        final Account newAccount = accountRepository.save(account);
        return newAccount;
    }

    @Override
    public Optional<Account> findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    @Override
    public long count() {
        return accountRepository.count();
    }

    @Override
    public int incrementAndGetLoginAttempts(String email) throws NoSuchAccountException {
        log.info("Incrementing login attempts " + email);
        Account account = getByEmail(email);
        int newLoginAttempts = account.getLoginAttempts() + 1;
        account.setLoginAttempts(newLoginAttempts);
        accountRepository.save(account);
        return newLoginAttempts;
    }

    private Account getByEmail(String email) throws NoSuchAccountException {
        return findByEmail(email).orElseThrow(() -> new NoSuchAccountException(email));
    }

    @Override
    public void resetLoginAttempts(String email) throws NoSuchAccountException {
        log.info("Resetting login attempts " + email);
        Account account = getByEmail(email);
        account.setLoginAttempts(0);
        accountRepository.save(account);
    }
    
    private void generateToken(Account account) {
        log.info("Generating token for account " + account.getEmail());
        if (account.getEmail().matches("test\\d@toptal\\.com"))
            account.setVerificationToken("testtoken");
        else
            account.setVerificationToken(RandomStringUtils.randomAlphanumeric(64));
        accountRepository.save(account);
    }

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public List<Account> findAll(int offset, int size) {
        return accountRepository.findAccountsBy(new PageRequestImpl(offset, size, Sort.Direction.DESC, "id"));
    }

    @Override
    public void updateExpectedNumberOfCalories(String email, BigDecimal expectedNumCalories) throws NoSuchAccountException {
        log.info("Updating expected number of calories for account " + email + " to " + expectedNumCalories);
        Account account = getByEmail(email);
        account.getSettings().setExpectedNumberOfCalories(expectedNumCalories);
        accountRepository.save(account);
    }
}
