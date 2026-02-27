package com.caplock.booking.config;

import com.caplock.booking.auth.AuthEntryPointJwt;
import com.caplock.booking.auth.JwtAuthFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SpringSecurityConf {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;
    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            String accept = request.getHeader("Accept");
            boolean wantsHtml = (accept != null && accept.contains("text/html"))
                    || (request.getRequestURI() != null && request.getRequestURI().startsWith("/ui/"));
            if (wantsHtml) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                request.getRequestDispatcher("/ui/error").forward(request, response);
                return;
            }
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "403 Forbidden");
        };
    }

    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurity(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(unauthorizedHandler)
                        .accessDeniedHandler(accessDeniedHandler()))
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(a -> a.requestMatchers("/api/v1/auth/**")
                        .permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain uiSecurity(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/ui/**", "/login", "/h2-console/**")
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .exceptionHandling(e -> e.accessDeniedHandler(accessDeniedHandler()))
                .authorizeHttpRequests(a -> a.requestMatchers(
                                "/login",
                                "/ui/auth/**",
                                "/h2-console/**")
                        .permitAll()
                        .requestMatchers("/ui/users/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,
                                "/ui/events/new", "/ui/events/*/edit",
                                "/ui/bookings/new", "/ui/bookings/*/edit",
                                "/ui/tickets/new", "/ui/tickets/*/edit",
                                "/ui/invoices/new", "/ui/invoices/*/edit",
                                "/ui/payments/new", "/ui/payments/*/edit",
                                "/ui/event-ticket-configs/new", "/ui/event-ticket-configs/*/edit",
                                "/ui/booking-items/new", "/ui/booking-items/*/edit",
                                "/ui/users/new", "/ui/users/*/edit")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,
                                "/ui/events", "/ui/events/*", "/ui/events/*/delete",
                                "/ui/bookings", "/ui/bookings/*", "/ui/bookings/*/delete",
                                "/ui/tickets", "/ui/tickets/*", "/ui/tickets/*/delete",
                                "/ui/invoices", "/ui/invoices/*", "/ui/invoices/*/delete",
                                "/ui/payments", "/ui/payments/*", "/ui/payments/*/delete",
                                "/ui/event-ticket-configs", "/ui/event-ticket-configs/*", "/ui/event-ticket-configs/*/delete",
                                "/ui/booking-items", "/ui/booking-items/*", "/ui/booking-items/*/delete",
                                "/ui/users", "/ui/users/*", "/ui/users/*/delete")
                        .hasRole("ADMIN")
                        .requestMatchers("/ui/events/**", "/ui/bookings/**", "/ui/tickets/**")
                        .hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated())
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .formLogin(form -> form
                        .loginPage("/ui/auth/login")
                        .defaultSuccessUrl("/ui/events", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/ui/auth/login?logout")
                        .permitAll()
                        .deleteCookies("jwt")
                )
                .rememberMe(rm -> rm
                        .key("myKey")
                        .tokenValiditySeconds(3000)
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
