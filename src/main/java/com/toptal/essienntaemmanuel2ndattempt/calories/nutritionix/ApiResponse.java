package com.toptal.essienntaemmanuel2ndattempt.calories.nutritionix;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author bodmas
 */
public class ApiResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Food> foods;

    public ApiResponse() {
    }

    public List<Food> getFoods() {
        return foods;
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
    }

    public static class Food implements Serializable {

        private static final long serialVersionUID = 1L;

        @JsonProperty("food_name")
        private String foodName;

        @JsonProperty("serving_qty")
        private Integer servingQuantity;

        @JsonProperty("nf_calories")
        private BigDecimal numberOfCalories;

        public String getFoodName() {
            return foodName;
        }

        public void setFoodName(String foodName) {
            this.foodName = foodName;
        }

        public Integer getServingQuantity() {
            return servingQuantity;
        }

        public void setServingQuantity(Integer servingQuantity) {
            this.servingQuantity = servingQuantity;
        }

        public BigDecimal getNumberOfCalories() {
            return numberOfCalories;
        }

        public void setNumberOfCalories(BigDecimal numberOfCalories) {
            this.numberOfCalories = numberOfCalories;
        }
    }
}
