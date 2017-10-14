package com.toptal.essienntaemmanuel2ndattempt.service;

import com.toptal.essienntaemmanuel2ndattempt.domain.User;
import com.toptal.essienntaemmanuel2ndattempt.exception.NoSuchUserException;
import com.toptal.essienntaemmanuel2ndattempt.repository.UserRepository;
import java.util.Optional;
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

    public User save(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public long count() {
        return userRepository.count();
    }

    public int incrementAndGetLoginAttempts(String username) throws NoSuchUserException {
        User user = getByUsername(username);
        int newLoginAttempts = user.getLoginAttempts() + 1;
        user.setLoginAttempts(newLoginAttempts);
        return newLoginAttempts;
    }

    private User getByUsername(String username) throws NoSuchUserException {
        return findByUsername(username).orElseThrow(() -> new NoSuchUserException(username));
    }

    public void resetLoginAttempts(String username) throws NoSuchUserException {
        User user = getByUsername(username);
        user.setLoginAttempts(0);
    }
}
