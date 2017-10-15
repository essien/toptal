package com.toptal.essienntaemmanuel2ndattempt.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

/**
 * @author bodmas
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SettingsDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("expected_num_calories")
    private Long expectedNumberOfCalories;

    public SettingsDto() {
    }

    public Long getExpectedNumberOfCalories() {
        return expectedNumberOfCalories;
    }

    public void setExpectedNumberOfCalories(Long expectedNumberOfCalories) {
        this.expectedNumberOfCalories = expectedNumberOfCalories;
    }
}
