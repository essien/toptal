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
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String USER = "USER";
    public static final String USER_MANAGER = "USER-MANAGER";
    public static final String ADMIN = "ADMIN";

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    Role() {
    }

    public Role(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
