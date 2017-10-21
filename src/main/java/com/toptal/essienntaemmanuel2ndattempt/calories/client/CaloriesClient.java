package com.toptal.essienntaemmanuel2ndattempt.calories.client;

import com.toptal.essienntaemmanuel2ndattempt.exception.MealNotFoundException;
import java.math.BigDecimal;

/**
 * @author bodmas
 */
public interface CaloriesClient {

    /**
     * Retrieves the number of calories burned for the given {@code mealDescription}
     * @param mealDescription the meal description
     * @return the number of calories associated with the given {@code mealDescription}
     * @throws MealNotFoundException if no meal is found with the given description
     */
    BigDecimal getCaloriesForMeal(String mealDescription) throws MealNotFoundException;
}
