package com.toptal.essienntaemmanuel2ndattempt.exception;

/**
 * @author bodmas
 */
public class MealNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    private final String meal;

    public MealNotFoundException(String meal) {
        super("Unable to find meal: " + meal);
        this.meal = meal;
    }

    public String getMeal() {
        return meal;
    }
}
