package com.itep.restaurant_service.security;

import com.itep.restaurant_service.repositories.AdminRepository;
import com.itep.restaurant_service.repositories.RestaurantRepository;
import com.itep.restaurant_service.repositories.entities.AdminEntity;
import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http   .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(HttpMethod.POST,"/restaurants").authenticated()
                        .anyRequest().permitAll()
                )
                .logout(LogoutConfigurer::permitAll)
                .httpBasic(withDefaults())
                .formLogin(withDefaults())
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }


    @Bean
    public UserDetailsService userDetailsService()  {
        adminRepository.save(new AdminEntity("admin", "admin"));
        ArrayList<UserDetails> users = new ArrayList<>();
//        TODO figure out if there is a better way to implement this
        List<AdminEntity> admins = adminRepository.findAll();
        for(AdminEntity admin: admins){
            users.add(User.withUsername(admin.getUsername())
                    .password(passwordEncoder().encode(admin.getPassword()))
                    .roles("ADMIN")
                    .build());

        }

        List<RestaurantEntity> restaurants = restaurantRepository.findAll();
        for(RestaurantEntity restaurant: restaurants){
            users.add(User.withUsername(restaurant.getUsername())
                    .password(passwordEncoder().encode(restaurant.getPassword()))
                    .roles("RESTAURANT")
                    .build());

        }
        return new InMemoryUserDetailsManager(users);
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }
}