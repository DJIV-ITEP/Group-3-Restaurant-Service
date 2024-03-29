package com.itep.restaurant_service.security;

import com.itep.restaurant_service.services.impl.RestaurantServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig  {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http   .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(HttpMethod.POST,"/restaurants").authenticated()
                        .requestMatchers(HttpMethod.POST,"/restaurants/{restaurantId}/status").authenticated()
                        .requestMatchers(HttpMethod.POST,"/restaurants/{rest_id}/category").authenticated()
                        .requestMatchers(HttpMethod.PUT,"/restaurants/{rest_id}/category/update/{id}").authenticated()
                        .requestMatchers(HttpMethod.DELETE,"/restaurants/{rest_id}/category/delete/{id}").authenticated()
                        .requestMatchers(HttpMethod.POST,"/restaurants/{rest_id}/category/{cat_id}/menus").authenticated()
                        .requestMatchers(HttpMethod.PUT,"/restaurants/{rest_id}/category/{cat_id}/menus/update/{id}").authenticated()
                        .requestMatchers(HttpMethod.DELETE,"/restaurants/{rest_id}/category/{cat_id}/menus/delete/{id}").authenticated()
                        .requestMatchers(HttpMethod.POST,"/restaurants/{rest_id}/category/{cat_id}/menus/{menu_id}/item").authenticated()
                        .requestMatchers(HttpMethod.PUT,"/restaurants/{rest_id}/category/{cat_id}/menus/{menu_id}/item/update/{id}").authenticated()
                        .requestMatchers(HttpMethod.DELETE,"/restaurants/{rest_id}/category/{cat_id}/menus/{menu_id}/item/delete/{id}").authenticated()
                        .anyRequest().permitAll()
                )
                .logout(LogoutConfigurer::permitAll)
                .httpBasic(withDefaults())
                .formLogin(withDefaults())
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Autowired
    private RestaurantServiceImpl restaurantService;

    @Bean
    public UserDetailsService userDetailsService() {
        return restaurantService::getRestaurantUserByUsername;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}