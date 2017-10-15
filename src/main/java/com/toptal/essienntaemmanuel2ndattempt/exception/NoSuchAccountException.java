package com.toptal.essienntaemmanuel2ndattempt.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bodmas
 */
public class NoSuchAccountException extends Exception {

    private static final Logger log = LoggerFactory.getLogger(NoSuchAccountException.class);
    private static final long serialVersionUID = 1L;

    private String email;

    public NoSuchAccountException(String email) {
        super("Unable to find account with email " + email);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
