package com.toptal.essienntaemmanuel2ndattempt.rest;

import com.toptal.essienntaemmanuel2ndattempt.domain.Role;
import com.toptal.essienntaemmanuel2ndattempt.domain.User;
import com.toptal.essienntaemmanuel2ndattempt.dto.UserDto;
import com.toptal.essienntaemmanuel2ndattempt.exception.GenericException;
import com.toptal.essienntaemmanuel2ndattempt.service.UserService;
import com.toptal.essienntaemmanuel2ndattempt.service.MailSender2;
import com.toptal.essienntaemmanuel2ndattempt.util.AuthorityUtil;
import com.toptal.essienntaemmanuel2ndattempt.util.WebUtil;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import ma.glasnost.orika.MapperFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    private static final String ADMIN_OR_MANAGER_AUTHORITY = AuthorityUtil.HAS_ADMIN_AUTHORITY + " or "
            + AuthorityUtil.HAS_MANAGER_AUTHORITY;

    @Autowired
    private MapperFacade mapperFacade;

    private final UserService userService;
    private final MailSender2 mailSender;

    public AccountResource(UserService userService, MailSender2 mailSender) {
        this.userService = userService;
        this.mailSender = mailSender;
    }

    /**
     * Create new user.
     * @param userDto
     * @param fields
     * @return
     */
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody UserDto userDto, BindingResult fields) {
        log.info("Received request to create user account");
        log.debug("userDto = " + userDto);
        WebUtil.validate(fields);

        userDto.setRoles(Arrays.asList(Role.USER));
        User user = userService.saveWithToken(mapperFacade.map(userDto, User.class));

        user.getVerificationToken().ifPresent(verificationToken -> {
            String tokenLink = tokenLink(user.getEmail(), verificationToken);

            log.debug("tokenLink = " + tokenLink);

            // Send email to verify account.
            mailSender.sendVerificationMail(user.getEmail(), tokenLink);
        });

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{email}").buildAndExpand(user.getEmail()).toUri();
        return ResponseEntity.created(uri).build();
    }

    /**
     * Get all users
     * @param req
     * @return
     */
    @GetMapping
    @PreAuthorize(ADMIN_OR_MANAGER_AUTHORITY)
    public ResponseEntity<?> getAll(HttpServletRequest req) {
        final List<User> allUsers = userService.findAll();
        List<String> authorities = getAuthorities(req);
        // Managers can CRUD only users.
        if (authorities.contains(Role.USER_MANAGER))
            allUsers.removeIf(user -> !hasRole(user, Role.USER));
        return ResponseEntity.ok(mapperFacade.mapAsList(allUsers, UserDto.class));
    }

    private static boolean hasRole(User user, String role) {
        return user.getRoles().stream().map(Role::getName).filter(x -> x.equals(role)).count() > 0;
    }

    /**
     * Get user by email.
     * @param email
     * @param req
     * @return
     */
    @GetMapping("/{email:.+}")
    @PreAuthorize(ADMIN_OR_MANAGER_AUTHORITY)
    public ResponseEntity<?> getUser(@PathVariable String email, HttpServletRequest req) {
        Optional<User> optUser = userService.findByEmail(email);
        List<String> authorities = getAuthorities(req);
        // Managers can CRUD only users.
        if (optUser.isPresent() && (authorities.contains(Role.ADMIN) || hasRole(optUser.get(), Role.USER))) {
            return ResponseEntity.ok(mapperFacade.map(optUser.get(), UserDto.class));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private List<String> getAuthorities(HttpServletRequest req) {
        UsernamePasswordAuthenticationToken principal = (UsernamePasswordAuthenticationToken) req.getUserPrincipal();
        return principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    }

    /**
     * Get logged-in user information.
     * @param req
     * @return
     */
    @GetMapping("/me")
    public ResponseEntity<?> me(HttpServletRequest req) {
        String email = req.getUserPrincipal().getName();
        return ResponseEntity.ok(mapperFacade.map(userService.findByEmail(email).get(), UserDto.class));
    }

    /**
     * Resend verification token.
     * @param email
     * @return
     */
    @PostMapping("/{email:.+}/sendtoken")
    @PreAuthorize(ADMIN_OR_MANAGER_AUTHORITY)
    public ResponseEntity<?> sendNewToken(@PathVariable String email) {
        Optional<User> optUser = userService.findByEmail(email);
        if (optUser.isPresent()) {
            User user = optUser.get();
            userService.saveWithToken(user);
            mailSender.sendVerificationMail(user.getEmail(), tokenLink(user.getEmail(), user.getVerificationToken().get()));
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Unblock the user's account.
     * @param email
     * @return
     */
    @PostMapping("/{email:.+}/unblock")
    @PreAuthorize(ADMIN_OR_MANAGER_AUTHORITY)
    public ResponseEntity<?> unblock(@PathVariable String email) {
        Optional<User> optUser = userService.findByEmail(email);
        if (optUser.isPresent()) {
            User user = optUser.get();
            userService.resetLoginAttempts(user);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
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
        Optional<User> optUser = userService.findByEmail(email);
        if (optUser.isPresent()) {
            User user = optUser.get();
            if (user.getVerificationToken().isPresent()) {
                String verificationToken = user.getVerificationToken().get();
                if (verificationToken.equals(token)) {
                    user.setVerificationToken(null);
                    userService.save(user);
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
}
