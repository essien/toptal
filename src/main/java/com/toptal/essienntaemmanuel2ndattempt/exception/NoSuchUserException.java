package com.toptal.essienntaemmanuel2ndattempt.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bodmas
 */
public class NoSuchUserException extends Exception {

    private static final Logger log = LoggerFactory.getLogger(NoSuchUserException.class);
    private static final long serialVersionUID = 1L;

    private String username;

    public NoSuchUserException(String username) {
        super("Unable to find user with username " + username);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
