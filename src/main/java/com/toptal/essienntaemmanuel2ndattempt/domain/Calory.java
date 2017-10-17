package com.toptal.essienntaemmanuel2ndattempt.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

/**
 * @author bodmas
 */
@Entity
public class Calory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "_date", nullable = false, updatable = false) // Because SQL keyword.
    private LocalDate date;

    @Column(name = "_time", nullable = false, updatable = false) // Because SQL keyword.
    private LocalTime time;

    @Column(nullable = false, updatable = false)
    @Lob
    private String foodDescription;

    /**
     * {@link #getNumberOfCalories()}
     */
    @Column(name = "num_calories", nullable = false, updatable = false)
    private BigDecimal numberOfCalories;

    /**
     * {@link #getAccount()}
     */
    @ManyToOne
    @JoinColumn(nullable = false, name = "account_fk", updatable = false)
    private Account account;

    /**
     * {@link #isCaloriesLessThanExpected()}
     */
    @Column(name = "less_than_expected", updatable = false)
    private boolean caloriesLessThanExpected;

    public Calory() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getFoodDescription() {
        return foodDescription;
    }

    public void setFoodDescription(String foodDescription) {
        this.foodDescription = foodDescription;
    }

    /**
     * @return the number of calories burned
     */
    public BigDecimal getNumberOfCalories() {
        return numberOfCalories;
    }

    public void setNumberOfCalories(BigDecimal numberOfCalories) {
        this.numberOfCalories = numberOfCalories;
    }

    /**
     *
     * @return true if the calories burned was less than expected when this entry was made, otherwise false
     */
    public boolean isCaloriesLessThanExpected() {
        return caloriesLessThanExpected;
    }

    public void setCaloriesLessThanExpected(boolean caloriesLessThanExpected) {
        this.caloriesLessThanExpected = caloriesLessThanExpected;
    }

    /**
     * @return the account that created this record
     */
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "Calory{" + "id=" + id + ", date=" + date + ", time=" + time + ", text=" + foodDescription
                + ", numberOfCalories=" + numberOfCalories + '}';
    }
}
