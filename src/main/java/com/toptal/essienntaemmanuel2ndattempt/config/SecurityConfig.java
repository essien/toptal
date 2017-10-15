package com.toptal.essienntaemmanuel2ndattempt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author bodmas
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/accounts").permitAll()
                .antMatchers("/accounts/*/verify/*").permitAll()
                .anyRequest().authenticated()
                .and().formLogin()
                .and().csrf().disable()
                .logout().invalidateHttpSession(true).deleteCookies("JSESSIONID");
    }

    @Autowired
    public void authenticationManager(AuthenticationManagerBuilder auth, DaoAuthenticationProvider authProvider) throws Exception {
        auth.authenticationProvider(authProvider);
    }
}
