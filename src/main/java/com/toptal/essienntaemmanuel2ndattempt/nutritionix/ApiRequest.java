package com.toptal.essienntaemmanuel2ndattempt.nutritionix;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

/**
 * @author bodmas
 */
public class ApiRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String query;

    @JsonProperty("line_delimited")
    private boolean lineDelimited = false;

    @JsonProperty("timezone")
    private String timeZone = "Africa/Bangui";

    @JsonProperty("use_branded_foods")
    private boolean useBrandedFoods = false;

    @JsonProperty("use_raw_foods")
    private boolean useRawFoods = false;

    public ApiRequest() {
    }

    public ApiRequest(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public boolean isLineDelimited() {
        return lineDelimited;
    }

    public void setLineDelimited(boolean lineDelimited) {
        this.lineDelimited = lineDelimited;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public boolean isUseBrandedFoods() {
        return useBrandedFoods;
    }

    public void setUseBrandedFoods(boolean useBrandedFoods) {
        this.useBrandedFoods = useBrandedFoods;
    }

    public boolean isUseRawFoods() {
        return useRawFoods;
    }

    public void setUseRawFoods(boolean useRawFoods) {
        this.useRawFoods = useRawFoods;
    }
}
