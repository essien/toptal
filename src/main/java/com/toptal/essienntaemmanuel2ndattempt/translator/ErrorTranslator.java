package com.toptal.essienntaemmanuel2ndattempt.translator;

import com.toptal.essienntaemmanuel2ndattempt.exception.GenericException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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
        return new ResponseDto().setStatus(ResponseDto.FAIL)
                .setMessage(e.getClientMessage());
    }
}
