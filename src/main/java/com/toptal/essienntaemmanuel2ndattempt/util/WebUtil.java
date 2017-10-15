package com.toptal.essienntaemmanuel2ndattempt.util;

import com.toptal.essienntaemmanuel2ndattempt.exception.GenericException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * @author bodmas
 */
public class WebUtil {

    private static final Logger log = LoggerFactory.getLogger(WebUtil.class);

    /**
     * Performs validation.
     * @param fields the binding result holding the fields
     * @throws GenericException if one or more field fails the validation test
     */
    public static void validate(BindingResult fields) {
        if (fields.hasErrors()) {
            final FieldError fieldError = fields.getFieldError();
            if (fieldError != null) {
                throw new GenericException("field errors: " + fieldError, fieldError.getDefaultMessage());
            }
        }
    }
}
