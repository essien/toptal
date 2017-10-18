package com.toptal.essienntaemmanuel2ndattempt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.toptal.essienntaemmanuel2ndattempt.deserializer.LocalDateDeserializer;
import com.toptal.essienntaemmanuel2ndattempt.deserializer.LocalTimeDeserializer;
import com.toptal.essienntaemmanuel2ndattempt.serializer.LocalDateSerializer;
import com.toptal.essienntaemmanuel2ndattempt.serializer.LocalTimeSerializer;
import java.io.Serializable;
import java.math.BigDecimal;
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

    @JsonProperty("food")
    @NotBlank(message = "food is a required field")
    private String food;

    @JsonProperty("num_calories")
    private BigDecimal numberOfCalories;

    @JsonProperty("less_than_expected")
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

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public BigDecimal getNumberOfCalories() {
        return numberOfCalories;
    }

    public void setNumberOfCalories(BigDecimal numberOfCalories) {
        this.numberOfCalories = numberOfCalories;
    }

    public boolean isCaloriesLessThanExpected() {
        return caloriesLessThanExpected;
    }

    public void setCaloriesLessThanExpected(boolean caloriesLessThanExpected) {
        this.caloriesLessThanExpected = caloriesLessThanExpected;
    }

    @Override
    public String toString() {
        return "CaloryDto{" + "date=" + date + ", time=" + time + ", foodDescription=" + food
                + ", numberOfCalories=" + numberOfCalories + ", caloriesLessThanExpected=" + caloriesLessThanExpected + '}';
    }
}
