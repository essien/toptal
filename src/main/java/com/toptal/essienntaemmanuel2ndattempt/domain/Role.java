package com.toptal.essienntaemmanuel2ndattempt.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author bodmas
 */
@Embeddable
public class Role implements Serializable, Comparable<Role> {

    private static final long serialVersionUID = 1L;

    public static final String USER = "USER";
    public static final String USER_MANAGER = "USER-MANAGER";
    public static final String ADMIN = "ADMIN";

    @Column(nullable = false, length = 20)
    private String name;

    Role() {
    }

    public Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Role o) {
        return name.compareTo(o.name);
    }
}
