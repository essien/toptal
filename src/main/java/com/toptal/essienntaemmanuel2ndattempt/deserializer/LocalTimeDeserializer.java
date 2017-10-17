package com.toptal.essienntaemmanuel2ndattempt.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bodmas
 */
public class LocalTimeDeserializer extends JsonDeserializer<LocalTime> {

    private static final Logger log = LoggerFactory.getLogger(LocalTimeDeserializer.class);

    @Override
    public LocalTime deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JsonProcessingException {
        String valueAsString = jp.getValueAsString();
        return LocalTime.parse(valueAsString);
    }
}
