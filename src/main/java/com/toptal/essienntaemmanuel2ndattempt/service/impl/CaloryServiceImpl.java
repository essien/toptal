package com.toptal.essienntaemmanuel2ndattempt.service.impl;

import com.toptal.essienntaemmanuel2ndattempt.service.api.CaloryService;
import com.toptal.essienntaemmanuel2ndattempt.domain.Account;
import com.toptal.essienntaemmanuel2ndattempt.domain.Calory;
import com.toptal.essienntaemmanuel2ndattempt.exception.NoSuchAccountException;
import com.toptal.essienntaemmanuel2ndattempt.repository.CaloryRepository;
import com.toptal.essienntaemmanuel2ndattempt.repository.misc.PageRequestImpl;
import com.toptal.essienntaemmanuel2ndattempt.repository.misc.QueryAnalyzer;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * @author bodmas
 */
@Service
@Transactional
public class CaloryServiceImpl implements CaloryService {

    private static final Logger log = LoggerFactory.getLogger(CaloryServiceImpl.class);

    private final CaloryRepository caloryRepository;
    private final AccountServiceImpl accountService;

    public CaloryServiceImpl(CaloryRepository caloryRepository, AccountServiceImpl accountService) {
        this.caloryRepository = caloryRepository;
        this.accountService = accountService;
    }

    @Override
    public Calory save(String email, Calory calory) throws NoSuchAccountException {
        log.info("Saving calory associated with email " + email);
        log.debug("calory = " + calory);
        Account account = accountService.findByEmail(email).orElseThrow(() -> new NoSuchAccountException(email));
        calory.setAccount(account);
        final BigDecimal expectedNumberOfCalories = account.getSettings().getExpectedNumberOfCalories();
        calory.setCaloriesLessThanExpected(calory.getNumberOfCalories().compareTo(expectedNumberOfCalories) < 0);
        return caloryRepository.save(calory);
    }

    @Override
    public Optional<Calory> findById(Long caloryId) {
        return caloryRepository.findCaloryById(caloryId);
    }

    @Override
    public List<Calory> findAll(String email) throws NoSuchAccountException {
        accountService.findByEmail(email).orElseThrow(() -> new NoSuchAccountException(email));
        return caloryRepository.findAllByAccountEmail(email);
    }

    @Override
    public List<Calory> findAll(String email, int offset, int size) throws NoSuchAccountException {
        log.info("Finding all");
        log.debug("offset = " + offset);
        log.debug("size = " + size);
        accountService.findByEmail(email).orElseThrow(() -> new NoSuchAccountException(email));
        return caloryRepository.findAllByAccountEmail(email, new PageRequestImpl(offset, size, Sort.Direction.ASC, "id"));
    }

    @Override
    public List<Calory> findAll(String email, String query) throws NoSuchAccountException {
        query = QueryAnalyzer.clean(query);
        accountService.findByEmail(email).orElseThrow(() -> new NoSuchAccountException(email));
        return caloryRepository.findByCustomQuery(email, query);
    }

    @Override
    public List<Calory> findAll(String email, String query, int offset, int size) throws NoSuchAccountException {
        query = QueryAnalyzer.clean(query);
        accountService.findByEmail(email).orElseThrow(() -> new NoSuchAccountException(email));
        return caloryRepository.findByCustomQuery(email, query, offset, size);
    }
}
