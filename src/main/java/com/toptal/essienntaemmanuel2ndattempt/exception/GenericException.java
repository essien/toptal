package com.toptal.essienntaemmanuel2ndattempt.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bodmas
 */
public class GenericException extends RuntimeException {

    private static final Logger log = LoggerFactory.getLogger(GenericException.class);
    private static final long serialVersionUID = 1L;

    private final String clientMessage;

    public GenericException(String clientMessage) {
        this(clientMessage, clientMessage);
    }

    public GenericException(String detailedMessage, String clientMessage) {
        super(detailedMessage);
        this.clientMessage = clientMessage;
    }

    public String getClientMessage() {
        return clientMessage;
    }
}
