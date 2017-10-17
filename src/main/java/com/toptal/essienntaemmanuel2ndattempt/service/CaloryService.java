package com.toptal.essienntaemmanuel2ndattempt.service;

import com.toptal.essienntaemmanuel2ndattempt.domain.Account;
import com.toptal.essienntaemmanuel2ndattempt.domain.Calory;
import com.toptal.essienntaemmanuel2ndattempt.exception.NoSuchAccountException;
import com.toptal.essienntaemmanuel2ndattempt.repository.CaloryRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * @author bodmas
 */
@Service
@Transactional
public class CaloryService {

    private static final Logger log = LoggerFactory.getLogger(CaloryService.class);

    private final CaloryRepository caloryRepository;
    private final AccountService accountService;

    public CaloryService(CaloryRepository caloryRepository, AccountService accountService) {
        this.caloryRepository = caloryRepository;
        this.accountService = accountService;
    }

    public Calory save(String email, Calory calory) throws NoSuchAccountException {
        Account account = accountService.findByEmail(email).orElseThrow(() -> new NoSuchAccountException(email));
        calory.setAccount(account);
        calory.setCaloriesLessThanExpected(calory.getNumberOfCalories() < account.getSettings().getExpectedNumberOfCalories());
        return caloryRepository.save(calory);
    }

    public Optional<Calory> findById(Long caloryId) {
        return caloryRepository.findCaloryById(caloryId);
    }
}
