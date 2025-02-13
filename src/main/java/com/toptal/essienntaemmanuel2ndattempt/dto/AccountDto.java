package com.toptal.essienntaemmanuel2ndattempt.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author bodmas
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Email(message = "email must be in a valid email format")
    @NotBlank(message = "email is a required field")
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "password is a required field")
    private String password;

    private boolean verified;

    private List<String> roles = new ArrayList<>();

    private SettingsDto settings;

    public AccountDto() {
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

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public SettingsDto getSettings() {
        return settings;
    }

    public void setSettings(SettingsDto settings) {
        this.settings = settings;
    }

    @Override
    public String toString() {
        return "AccountDto{" + "email=" + email + ", password=" + password + ", verified=" + verified + ", roles=" + roles
                + ", settings=" + settings + '}';
    }
}
