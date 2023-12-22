package com.example.webscoketnotification.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

   @Bean
   public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
      http
          // Permitimos todas las acceso sin autenticación a '/' y '/ws'
          .authorizeHttpRequests()
            .requestMatchers("/","/ws/**")
            .permitAll()
          .and()
          // Cualquier otra solicitud require de autenticación
          .authorizeHttpRequests()
            .anyRequest().authenticated()
          .and()
          // Permitimos autenticación basada en formularios
          .formLogin()
          .and()
          // Configuramos la funcinalidad de cierre de sesión que redirigirá a la ruta '/'
          .logout( logout -> logout.logoutSuccessUrl("/"));
      return http.build();
   }

   @Bean
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }

   @Bean
   public InMemoryUserDetailsManager userDetailsService() {
      UserDetails user1 = User.builder()
          .username("test1")
          .password(passwordEncoder().encode("password1"))
          .roles("USER")
          .build();

      UserDetails user2 = User.builder()
          .username("test2")
          .password(passwordEncoder().encode("password2"))
          .roles("USER")
          .build();

      return new InMemoryUserDetailsManager(user1, user2);
   }

}
