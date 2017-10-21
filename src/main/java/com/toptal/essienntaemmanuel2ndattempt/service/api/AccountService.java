package com.toptal.essienntaemmanuel2ndattempt.service.api;

import com.toptal.essienntaemmanuel2ndattempt.domain.Account;
import com.toptal.essienntaemmanuel2ndattempt.exception.NoSuchAccountException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * @author bodmas
 */
public interface AccountService {

    /**
     * @return the number of registered accounts
     */
    long count();

    /**
     * @return the list of all registered accounts
     */
    List<Account> findAll();

    /**
     * Finds {@code size} registered accounts starting from the given {@code offset}, sorted by id field.
     * @param offset indicates the number of accounts to skip
     * @param size   the number of accounts to return
     * @return the registered accounts that match the filter criteria
     */
    List<Account> findAll(int offset, int size);

    /**
     * @param email the email associated with the account
     * @return the optional account associated with the given email
     */
    Optional<Account> findByEmail(String email);

    /**
     * Increments the login attempts count for the account associated with the given email.
     * @param email the email associated with the account
     * @return the new login attempts count
     * @throws NoSuchAccountException if there's no account associated with the given email
     */
    int incrementAndGetLoginAttempts(String email) throws NoSuchAccountException;

    /**
     * Resets the login attempts for the account associated with the given email.
     * @param email the email associated with the account
     * @throws NoSuchAccountException if there's no account associated with the given email
     */
    void resetLoginAttempts(String email) throws NoSuchAccountException;

    /**
     * Saves the account.
     * @param account the account to save
     * @return the newly saved account
     */
    Account save(Account account);

    /**
     * Assigns a token to the account before saving it.
     * @param account the accont to save
     * @return the newly saved account
     */
    Account saveWithToken(Account account);

    /**
     * Updates the expected number of calories.
     * @param email               the email associated with the account whose expected number of calories is to be updated
     * @param expectedNumCalories the expected number of calories to set for the account
     * @throws NoSuchAccountException if there's no account associated with the given email
     */
    void updateExpectedNumberOfCalories(String email, BigDecimal expectedNumCalories) throws NoSuchAccountException;
}
