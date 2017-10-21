package com.toptal.essienntaemmanuel2ndattempt.dto;

import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bodmas
 */
public class ResponseDto implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(ResponseDto.class);
    private static final long serialVersionUID = 1L;

    public static final String ERROR = "error";
    public static final String SUCCESS = "success";
    public static final String FAIL = "fail";

    private String message;
    private String status;

    public String getMessage() {
        return message;
    }

    public ResponseDto setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public ResponseDto setStatus(String status) {
        this.status = status;
        return this;
    }
}
