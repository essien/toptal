package com.toptal.essienntaemmanuel2ndattempt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author bodmas
 */
public class UserDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Email(message = "email must be in a valid email format")
    @NotBlank(message = "email is a required field")
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "password is a required field")
    private String password;

    private boolean verified;

    public UserDto() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
