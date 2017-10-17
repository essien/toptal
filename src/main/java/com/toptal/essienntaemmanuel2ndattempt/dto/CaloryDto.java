package com.toptal.essienntaemmanuel2ndattempt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.toptal.essienntaemmanuel2ndattempt.deserializer.LocalDateDeserializer;
import com.toptal.essienntaemmanuel2ndattempt.deserializer.LocalTimeDeserializer;
import com.toptal.essienntaemmanuel2ndattempt.serializer.LocalDateSerializer;
import com.toptal.essienntaemmanuel2ndattempt.serializer.LocalTimeSerializer;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author bodmas
 */
public class CaloryDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "date is a required field")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate date;

    @NotNull(message = "time is a required field")
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime time;

    @NotBlank(message = "text is a required field")
    private String text;

    @JsonProperty("num_of_calories")
    @NotNull(message = "num_of_calories is a required field")
    private Long numberOfCalories;

    @JsonProperty("calories_less_than_expected")
    private boolean caloriesLessThanExpected;

    public CaloryDto() {
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getNumberOfCalories() {
        return numberOfCalories;
    }

    public void setNumberOfCalories(Long numberOfCalories) {
        this.numberOfCalories = numberOfCalories;
    }

    public boolean isCaloriesLessThanExpected() {
        return caloriesLessThanExpected;
    }

    public void setCaloriesLessThanExpected(boolean caloriesLessThanExpected) {
        this.caloriesLessThanExpected = caloriesLessThanExpected;
    }
}
