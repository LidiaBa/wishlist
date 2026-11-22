package com.example.wishlist.config;

import com.example.wishlist.service.AuthService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    private final AuthService authService;

    public FilterConfig(AuthService authService) {
        this.authService = authService;
    }

    @Bean
    public FilterRegistrationBean<AuthFilter> authFilterRegistration() {
        FilterRegistrationBean<AuthFilter> registration = new FilterRegistrationBean<>();

        registration.setFilter(new AuthFilter(authService));

        registration.addUrlPatterns("/wishes/*", "/bookings/*", "/users/*", "/user/*");

        return registration;
    }
}