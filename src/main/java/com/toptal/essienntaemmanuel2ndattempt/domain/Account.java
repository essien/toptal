package com.toptal.essienntaemmanuel2ndattempt.domain;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * @author bodmas
 */
@Entity
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Role> roles;

    private int loginAttempts;

    /**
     * If this field is null, then account has been verified. Otherwise, account can be verified with this token.
     */
    private String verificationToken;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false)
    private final Settings settings = new Settings();

    Account(Long id) {
        this.id = id;
    }

    public Account() {
    }

    public Account(String email, String password, String... roles) {
        this.email = email;
        this.password = password;
        this.roles = Arrays.stream(roles).map(Role::new).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public int getLoginAttempts() {
        return loginAttempts;
    }

    public void setLoginAttempts(int loginAttempts) {
        this.loginAttempts = loginAttempts;
    }

    public Optional<String> getVerificationToken() {
        return Optional.ofNullable(verificationToken);
    }

    /**
     * Sets the verification token. A null argument implies that the account is verified.
     * @param verificationToken the verification token
     */
    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public Settings getSettings() {
        return settings;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.email);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Account))
            return false;
        final Account other = (Account) obj;
        if (!Objects.equals(this.email, other.email))
            return false;
        return true;
    }
}
