package com.toptal.essienntaemmanuel2ndattempt.translator;

import com.toptal.essienntaemmanuel2ndattempt.dto.ResponseDto;
import com.toptal.essienntaemmanuel2ndattempt.exception.GenericException;
import com.toptal.essienntaemmanuel2ndattempt.exception.MealNotFoundException;
import com.toptal.essienntaemmanuel2ndattempt.exception.NoSuchAccountException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author bodmas
 */
@ControllerAdvice
public class ErrorTranslator {

    private static final Logger log = LoggerFactory.getLogger(ErrorTranslator.class);

    @ExceptionHandler(GenericException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseDto processGenericException(GenericException e) {
        log.warn("", e);
        return new ResponseDto().setStatus(ResponseDto.FAIL)
                .setMessage(e.getClientMessage());
    }

    @ExceptionHandler(NonTransientDataAccessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseDto processNonTransientDataAccessException(NonTransientDataAccessException e) {
        log.warn("", e);
        return new ResponseDto().setStatus(ResponseDto.FAIL)
                .setMessage("Malformed query. Your request was not understood.");
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ResponseDto processAccessDeniedException(AccessDeniedException e) {
        log.warn("", e);
        return new ResponseDto().setStatus(ResponseDto.FAIL)
                .setMessage(e.getMessage());
    }

    @ExceptionHandler(NoSuchAccountException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseDto processNoSuchAccountException(NoSuchAccountException e) {
        log.warn("", e);
        return new ResponseDto().setStatus(ResponseDto.FAIL)
                .setMessage(e.getMessage());
    }

    @ExceptionHandler(MealNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseDto processMealNotFoundException(MealNotFoundException e) {
        log.warn("", e);
        return new ResponseDto().setStatus(ResponseDto.FAIL)
                .setMessage(e.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    public ResponseDto processHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.warn("", e);
        return new ResponseDto().setStatus(ResponseDto.FAIL)
                .setMessage(e.getMessage());
    }
}
