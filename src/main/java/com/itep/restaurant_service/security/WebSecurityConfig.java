package com.itep.restaurant_service.security;

import com.itep.restaurant_service.repositories.AdminRepository;
import com.itep.restaurant_service.repositories.RestaurantRepository;
import com.itep.restaurant_service.repositories.entities.AdminEntity;
import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers( "/home", "/").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .logout(LogoutConfigurer::permitAll);

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService()  {
        adminRepository.save(new AdminEntity("admin", "admin"));
        ArrayList<UserDetails> users = new ArrayList<>();

        List<AdminEntity> admins = adminRepository.findAll();
        for(AdminEntity admin: admins){
            users.add(User.withDefaultPasswordEncoder()
                    .username(admin.username)
                    .password(admin.password)
                    .roles("ADMIN")
                    .build());

        }

        List<RestaurantEntity> restaurants = restaurantRepository.findAll();
        for(RestaurantEntity restaurant: restaurants){
            users.add(User.withDefaultPasswordEncoder()
                    .username(restaurant.username)
                    .password(restaurant.password)
                    .roles("RESTAURANT")
                    .build());

        }
        return new InMemoryUserDetailsManager(users);
    }
}