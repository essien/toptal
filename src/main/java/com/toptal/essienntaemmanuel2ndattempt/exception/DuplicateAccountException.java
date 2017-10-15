package com.toptal.essienntaemmanuel2ndattempt.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bodmas
 */
public class DuplicateAccountException extends GenericException {

    private static final Logger log = LoggerFactory.getLogger(DuplicateAccountException.class);
    private static final long serialVersionUID = 1L;

    private String email;

    public DuplicateAccountException(String email) {
        super("An account already exists with email " + email);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
