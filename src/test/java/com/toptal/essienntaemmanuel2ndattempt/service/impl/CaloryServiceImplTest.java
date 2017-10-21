package com.toptal.essienntaemmanuel2ndattempt.service.impl;

import com.toptal.essienntaemmanuel2ndattempt.domain.Account;
import com.toptal.essienntaemmanuel2ndattempt.domain.Calory;
import com.toptal.essienntaemmanuel2ndattempt.exception.NoSuchAccountException;
import com.toptal.essienntaemmanuel2ndattempt.repository.CaloryRepository;
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
public class CaloryServiceImplTest {

    @Mock
    private CaloryRepository caloryRepository;

    @Mock
    private AccountServiceImpl accountService;

    private CaloryServiceImpl caloryService;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    public CaloryServiceImplTest() {
    }

    @Before
    public void setUp() {
        caloryService = new CaloryServiceImpl(caloryRepository, accountService);
    }

    @Test
    public void saveWhenAccountNotFoundShouldThrowException() throws Exception {
        String email = RandomStringUtils.randomAlphabetic(10);
        Calory calory = null;

        given(accountService.findByEmail(anyString())).willReturn(Optional.empty());

        thrown.expect(NoSuchAccountException.class);
        thrown.expectMessage("Unable to find account with email " + email);

        caloryService.save(email, calory);
    }

    @Test
    public void saveShouldSetNecessaryPropertiesBeforePersisting() throws Exception {
        Account account = new Account();
        String email = RandomStringUtils.randomAlphabetic(10);
        Calory calory = new Calory();
        calory.setNumberOfCalories(BigDecimal.valueOf(12));

        given(accountService.findByEmail(anyString())).willReturn(Optional.of(account));
        given(caloryRepository.save(any(Calory.class))).will(AdditionalAnswers.returnsFirstArg());

        Calory result = caloryService.save(email, calory);
        assertThat(result.getAccount()).isEqualTo(account);
        assertThat(calory.isCaloriesLessThanExpected()).isFalse();

        calory.setNumberOfCalories(BigDecimal.valueOf(6));
        caloryService.save(email, calory);
        assertThat(calory.isCaloriesLessThanExpected()).isTrue();
    }

    @Test
    public void findByIdShouldReturnOptionalCalory() {
        Long caloryId = (long) (Math.random() * Long.MAX_VALUE);
        Optional<Calory> expResult = Optional.of(new Calory());

        given(caloryRepository.findCaloryById(anyLong())).willReturn(expResult);

        Optional<Calory> result = caloryService.findById(caloryId);
        assertThat(result).isEqualTo(expResult);
    }

    @Test
    public void findAllWhenAccountNotFoundShouldThrowException() throws Exception {
        String email = RandomStringUtils.randomAlphabetic(10);

        given(accountService.findByEmail(anyString())).willReturn(Optional.empty());

        thrown.expect(NoSuchAccountException.class);
        thrown.expectMessage("Unable to find account with email " + email);

        caloryService.findAll(email);
    }

    @Test
    public void findAllShouldReturnAllCalories() throws Exception {
        String email = RandomStringUtils.randomAlphabetic(10);
        List<Calory> expResult = Arrays.asList(new Calory(), new Calory(), new Calory());

        given(accountService.findByEmail(anyString())).willReturn(Optional.of(new Account()));
        given(caloryRepository.findAllByAccountEmail(anyString())).willReturn(expResult);

        List<Calory> result = caloryService.findAll(email);
        assertThat(result).containsExactlyElementsOf(expResult);
    }

    @Test
    public void findAllWithBoundsWhenAccountNotFoundShouldThrowException() throws Exception {
        String email = RandomStringUtils.randomAlphabetic(10);
        int offset = (int) (Math.random() * (Integer.MAX_VALUE - 1));
        int size = (int) (Math.random() * (Integer.MAX_VALUE - 1));

        given(accountService.findByEmail(anyString())).willReturn(Optional.empty());

        thrown.expect(NoSuchAccountException.class);
        thrown.expectMessage("Unable to find account with email " + email);

        caloryService.findAll(email, offset, size);
    }

    @Test
    public void findAllWithBoundsShouldReturnAllCaloriesWithinBounds() throws Exception {
        String email = RandomStringUtils.randomAlphabetic(10);
        List<Calory> expResult = Arrays.asList(new Calory(), new Calory(), new Calory());
        int offset = (int) (Math.random() * (Integer.MAX_VALUE - 1));
        int size = (int) (Math.random() * (Integer.MAX_VALUE - 1));

        given(accountService.findByEmail(anyString())).willReturn(Optional.of(new Account()));
        given(caloryRepository.findAllByAccountEmail(anyString(), any(Pageable.class))).willReturn(expResult);

        List<Calory> result = caloryService.findAll(email, offset, size);
        assertThat(result).containsExactlyElementsOf(expResult);
    }

    @Test
    public void findAllByQueryWhenAccountNotFoundShouldThrowException() throws Exception {
        String email = RandomStringUtils.randomAlphabetic(10);
        String query = RandomStringUtils.randomAlphabetic(10);

        given(accountService.findByEmail(anyString())).willReturn(Optional.empty());

        thrown.expect(NoSuchAccountException.class);
        thrown.expectMessage("Unable to find account with email " + email);

        caloryService.findAll(email, query);
    }

    @Test
    public void findAllByQueryShouldReturnAllMatchingCalories() throws Exception {
        String email = RandomStringUtils.randomAlphabetic(10);
        List<Calory> expResult = Arrays.asList(new Calory(), new Calory(), new Calory());
        String query = RandomStringUtils.randomAlphabetic(10);

        given(accountService.findByEmail(anyString())).willReturn(Optional.of(new Account()));
        given(caloryRepository.findByCustomQuery(anyString(), anyString())).willReturn(expResult);

        List<Calory> result = caloryService.findAll(email, query);
        assertThat(result).containsExactlyElementsOf(expResult);
    }

    @Test
    public void findAllByQueryAndBoundsWhenAccountNotFoundShouldThrowException() throws Exception {
        String email = RandomStringUtils.randomAlphabetic(10);
        String query = RandomStringUtils.randomAlphabetic(10);
        int offset = (int) (Math.random() * (Integer.MAX_VALUE - 1));
        int size = (int) (Math.random() * (Integer.MAX_VALUE - 1));

        given(accountService.findByEmail(anyString())).willReturn(Optional.empty());

        thrown.expect(NoSuchAccountException.class);
        thrown.expectMessage("Unable to find account with email " + email);

        caloryService.findAll(email, query, offset, size);
    }

    @Test
    public void findAllByQueryAndBounds() throws Exception {
        List<Calory> expResult = Arrays.asList(new Calory(), new Calory(), new Calory());
        String email = RandomStringUtils.randomAlphabetic(10);
        String query = RandomStringUtils.randomAlphabetic(10);
        int offset = (int) (Math.random() * (Integer.MAX_VALUE - 1));
        int size = (int) (Math.random() * (Integer.MAX_VALUE - 1));

        given(accountService.findByEmail(anyString())).willReturn(Optional.of(new Account()));
        given(caloryRepository.findByCustomQuery(anyString(), anyString(), anyInt(), anyInt())).willReturn(expResult);

        List<Calory> result = caloryService.findAll(email, query, offset, size);
        assertThat(result).containsExactlyElementsOf(expResult);
    }
}
