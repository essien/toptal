package com.toptal.essienntaemmanuel2ndattempt.nutritionix.client;

import com.toptal.essienntaemmanuel2ndattempt.exception.MealNotFoundException;
import com.toptal.essienntaemmanuel2ndattempt.nutritionix.ApiRequest;
import com.toptal.essienntaemmanuel2ndattempt.nutritionix.ApiResponse;
import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * @author bodmas
 */
@Service
public class CaloriesClient {

    private static final Logger log = LoggerFactory.getLogger(CaloriesClient.class);

    private static final String ERROR_MESSAGE = "Something went wrong. Please try again later";
    private static final String REQUEST_URL = "https://www.nutritionix.com/track-api/v2/natural/nutrients";

    private final RestTemplate restTemplate;

    public CaloriesClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public BigDecimal getCaloriesForMeal(String mealDescription) throws MealNotFoundException {
        log.info("Retrieving calories for meal " + mealDescription);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        httpHeaders.set(HttpHeaders.USER_AGENT, "curl/7.35.0");
        ApiRequest apiRequest = new ApiRequest(mealDescription);
        HttpEntity<ApiRequest> request = new HttpEntity<>(apiRequest, httpHeaders);
        try {
            ApiResponse apiResponse = restTemplate.postForObject(REQUEST_URL, request, ApiResponse.class);
            return apiResponse.getFoods().get(0).getNumberOfCalories();
        } catch (HttpClientErrorException httpClientErrorException) {
            log.warn("", httpClientErrorException);
            if (HttpStatus.NOT_FOUND.equals(httpClientErrorException.getStatusCode()))
                throw new MealNotFoundException(mealDescription);
            else
                throw new RuntimeException(ERROR_MESSAGE);
        } catch (Exception e) {
            log.warn("", e);
            throw new RuntimeException(ERROR_MESSAGE);
        }
    }
}
