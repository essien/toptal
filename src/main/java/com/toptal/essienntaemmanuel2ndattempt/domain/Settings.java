package com.toptal.essienntaemmanuel2ndattempt.domain;

import java.io.Serializable;
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
    private Long expectedNumberOfCalories = 10L; // Default

    public Settings() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getExpectedNumberOfCalories() {
        return expectedNumberOfCalories;
    }

    public void setExpectedNumberOfCalories(Long expectedNumberOfCalories) {
        this.expectedNumberOfCalories = expectedNumberOfCalories;
    }

    @Override
    public String toString() {
        return "Settings{" + "id=" + id + ", expectedNumberOfCalories=" + expectedNumberOfCalories + '}';
    }
}
