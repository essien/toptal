package com.toptal.essienntaemmanuel2ndattempt.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bodmas
 */
public class LocalTimeSerializer extends JsonSerializer<LocalTime> {

    private static final Logger log = LoggerFactory.getLogger(LocalTimeSerializer.class);

    @Override
    public void serialize(LocalTime t, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
        jg.writeString(t == null ? null : t.toString());
    }
}
