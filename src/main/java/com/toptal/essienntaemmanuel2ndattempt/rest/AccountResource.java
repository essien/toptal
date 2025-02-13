package com.toptal.essienntaemmanuel2ndattempt.rest;

import com.toptal.essienntaemmanuel2ndattempt.domain.Role;
import com.toptal.essienntaemmanuel2ndattempt.domain.Account;
import com.toptal.essienntaemmanuel2ndattempt.dto.AccountDto;
import com.toptal.essienntaemmanuel2ndattempt.exception.GenericException;
import com.toptal.essienntaemmanuel2ndattempt.exception.NoSuchAccountException;
import com.toptal.essienntaemmanuel2ndattempt.service.api.AccountService;
import com.toptal.essienntaemmanuel2ndattempt.service.api.MailSender;
import com.toptal.essienntaemmanuel2ndattempt.util.AuthorityUtil;
import com.toptal.essienntaemmanuel2ndattempt.util.WebUtil;
import java.math.BigDecimal;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import ma.glasnost.orika.MapperFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * @author bodmas
 */
@RestController
@RequestMapping(
        value = "/accounts",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class AccountResource {

    private static final Logger log = LoggerFactory.getLogger(AccountResource.class);

    @Autowired
    private MapperFacade mapperFacade;

    private final AccountService accountService;
    private final MailSender mailSender;

    public AccountResource(AccountService accountService, MailSender mailSender) {
        this.accountService = accountService;
        this.mailSender = mailSender;
    }

    /**
     * Create new account.
     * @param accountDto
     * @param fields
     * @return
     */
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody AccountDto accountDto, BindingResult fields) {
        log.info("Received request to create account account");
        log.debug("accountDto = " + accountDto);
        WebUtil.validate(fields);

        accountDto.setRoles(Arrays.asList(Role.USER));
        Account account = accountService.saveWithToken(mapperFacade.map(accountDto, Account.class));

        account.getVerificationToken().ifPresent(verificationToken -> {
            String tokenLink = tokenLink(account.getEmail(), verificationToken);

            log.debug("tokenLink = " + tokenLink);

            // Send email to verify account.
            mailSender.sendVerificationMail(account.getEmail(), tokenLink);
        });

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{email}").buildAndExpand(account.getEmail()).toUri();
        return ResponseEntity.created(uri).build();
    }

    /**
     * Get all accounts
     * @param req
     * @return
     */
    @GetMapping
    @PreAuthorize(AuthorityUtil.ADMIN_OR_MANAGER_AUTHORITY)
    public ResponseEntity<?> getAll(HttpServletRequest req) {
        final List<Account> allAccounts = accountService.findAll();
        List<String> authorities = WebUtil.getAuthorities(req);
        // Managers can CRUD only users.
        if (authorities.contains(Role.USER_MANAGER))
            allAccounts.removeIf(account -> !hasRole(account, Role.USER));
        return ResponseEntity.ok(mapperFacade.mapAsList(allAccounts, AccountDto.class));
    }

    /**
     * Get all accounts
     * @param page
     * @param size
     * @param req
     * @return
     */
    @GetMapping(params = {"page", "size"})
    @PreAuthorize(AuthorityUtil.ADMIN_OR_MANAGER_AUTHORITY)
    public ResponseEntity<?> getAll(@RequestParam int page, @RequestParam int size, HttpServletRequest req) {
        final List<Account> allAccounts = accountService.findAll(page, size);
        List<String> authorities = WebUtil.getAuthorities(req);
        // Managers can CRUD only users.
        if (authorities.contains(Role.USER_MANAGER))
            allAccounts.removeIf(account -> !hasRole(account, Role.USER));
        return ResponseEntity.ok(mapperFacade.mapAsList(allAccounts, AccountDto.class));
    }

    private static boolean hasRole(Account account, String role) {
        return account.getRoles().stream().map(Role::getName).filter(x -> x.equals(role)).count() > 0;
    }

    /**
     * Get account by email.
     * @param email
     * @param req
     * @return
     */
    @GetMapping("/{email:.+}")
    @PreAuthorize(AuthorityUtil.ADMIN_OR_MANAGER_AUTHORITY)
    public ResponseEntity<?> getAccount(@PathVariable String email, HttpServletRequest req) {
        Optional<Account> optAccount = accountService.findByEmail(email);
        List<String> authorities = WebUtil.getAuthorities(req);
        // Managers can CRUD only users.
        if (optAccount.isPresent() && (authorities.contains(Role.ADMIN) || hasRole(optAccount.get(), Role.USER))) {
            return ResponseEntity.ok(mapperFacade.map(optAccount.get(), AccountDto.class));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get logged-in account information.
     * @param req
     * @return
     */
    @GetMapping("/me")
    public ResponseEntity<?> me(HttpServletRequest req) {
        String email = req.getUserPrincipal().getName();
        return ResponseEntity.ok(mapperFacade.map(accountService.findByEmail(email).get(), AccountDto.class));
    }

    /**
     * Resend verification token.
     * @param email
     * @return
     */
    @PostMapping("/{email:.+}/sendtoken")
    @PreAuthorize(AuthorityUtil.ADMIN_OR_MANAGER_AUTHORITY)
    public ResponseEntity<?> sendNewToken(@PathVariable String email) {
        Optional<Account> optAccount = accountService.findByEmail(email);
        if (optAccount.isPresent()) {
            Account account = optAccount.get();
            accountService.saveWithToken(account);
            mailSender.sendVerificationMail(account.getEmail(), tokenLink(account.getEmail(), account.getVerificationToken().get()));
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Unblock the account's account.
     * @param email
     * @return
     * @throws com.toptal.essienntaemmanuel2ndattempt.exception.NoSuchAccountException
     */
    @PostMapping("/{email:.+}/unblock")
    @PreAuthorize(AuthorityUtil.ADMIN_OR_MANAGER_AUTHORITY)
    public ResponseEntity<?> unblock(@PathVariable String email) throws NoSuchAccountException {
        accountService.resetLoginAttempts(email);
        return ResponseEntity.ok().build();
    }

    /**
     * Verify verification token.
     * @param email
     * @param token
     * @return
     */
    @GetMapping("/{email:.+}/verify/{token}")
    public ResponseEntity<?> verify(@PathVariable String email, @PathVariable String token) {
        log.info("Verifying email");
        log.debug("email = " + email);
        log.debug("token = " + token);
        Optional<Account> optAccount = accountService.findByEmail(email);
        if (optAccount.isPresent()) {
            Account account = optAccount.get();
            if (account.getVerificationToken().isPresent()) {
                String verificationToken = account.getVerificationToken().get();
                if (verificationToken.equals(token)) {
                    account.setVerificationToken(null);
                    accountService.save(account);
                    return ResponseEntity.ok().build();
                } else
                    throw new GenericException("Invalid verification token");
            } else {
                throw new GenericException("Already verified email " + email);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private static String tokenLink(String email, String token) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{email}/verify/{token}")
                .buildAndExpand(email, token).toUriString();
    }

    /**
     * Set expected number of calories. Only accounts with user role can perform this action.
     * @param email
     * @param expectedNumCalories
     * @return
     * @throws NoSuchAccountException
     */
    @PostMapping("/{email:.+}/settings/calories/expect/{expectedNumCalories}")
    @PreAuthorize(AuthorityUtil.HAS_USER_AUTHORITY)
    public ResponseEntity<?> setExpectedCalories(@PathVariable String email, @PathVariable BigDecimal expectedNumCalories)
            throws NoSuchAccountException {
        accountService.updateExpectedNumberOfCalories(email, expectedNumCalories);
        return ResponseEntity.ok().build();
    }
}
