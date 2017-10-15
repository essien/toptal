package com.toptal.essienntaemmanuel2ndattempt.service;

import com.toptal.essienntaemmanuel2ndattempt.domain.User;
import com.toptal.essienntaemmanuel2ndattempt.exception.DuplicateAccountException;
import com.toptal.essienntaemmanuel2ndattempt.exception.NoSuchUserException;
import com.toptal.essienntaemmanuel2ndattempt.repository.UserRepository;
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
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveWithToken(User user) {
        generateToken(user);
        return save(user);
    }

    public User save(User user) {
        userRepository.findByEmail(user.getEmail()).ifPresent(existingUser -> {
            if (user.getId() == null)
                throw new DuplicateAccountException(existingUser.getEmail());
        });

        final User newUser = userRepository.save(user);
        return newUser;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public long count() {
        return userRepository.count();
    }

    public int incrementAndGetLoginAttempts(String email) throws NoSuchUserException {
        User user = getByEmail(email);
        int newLoginAttempts = user.getLoginAttempts() + 1;
        user.setLoginAttempts(newLoginAttempts);
        return newLoginAttempts;
    }

    private User getByEmail(String email) throws NoSuchUserException {
        return findByEmail(email).orElseThrow(() -> new NoSuchUserException(email));
    }

    public void resetLoginAttempts(String email) throws NoSuchUserException {
        User user = getByEmail(email);
        user.setLoginAttempts(0);
    }

    public void generateToken(User user) {
        user.setVerificationToken(RandomStringUtils.randomAlphanumeric(64));
    }
}
