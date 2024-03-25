package com.itep.restaurant_service.security;

import com.itep.restaurant_service.repositories.RestaurantRepository;
import com.itep.restaurant_service.repositories.UserRepository;
import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
import com.itep.restaurant_service.repositories.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    @Autowired
    private UserRepository userRepository;


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


    @Bean
    public UserDetailsService userDetailsService()  {
        ArrayList<UserDetails> users = new ArrayList<>();
//        TODO figure out if there is a better way to implement this
        users.add(User.withUsername("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("ADMIN")
                .build());
        List<UserEntity> usersEntities = userRepository.findAll();
        for(UserEntity user: usersEntities){
            users.add(User.withUsername(user.getUsername())
                    .password(passwordEncoder().encode(user.getPassword()))
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