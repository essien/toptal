package com.toptal.essienntaemmanuel2ndattempt.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author bodmas
 */
@Entity
public class Settings implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "expected_num_calories")
    private BigDecimal expectedNumberOfCalories = BigDecimal.valueOf(10); // Default

    public Settings() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getExpectedNumberOfCalories() {
        return expectedNumberOfCalories;
    }

    public void setExpectedNumberOfCalories(BigDecimal expectedNumberOfCalories) {
        this.expectedNumberOfCalories = expectedNumberOfCalories;
    }

    @Override
    public String toString() {
        return "Settings{" + "id=" + id + ", expectedNumberOfCalories=" + expectedNumberOfCalories + '}';
    }
}
