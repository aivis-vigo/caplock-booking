package com.caplock.booking.controller;

import com.caplock.booking.entity.UserRole;
import com.caplock.booking.entity.dto.UserDto;
import com.caplock.booking.service.IUserService;
import com.caplock.booking.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final IUserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signin")
    public ResponseEntity<Void> signin(@RequestParam String email, @RequestParam String password) {
        String normalizedEmail = email.trim().toLowerCase();
        Authentication auth = authenticationManager.authenticate(
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(normalizedEmail, password));
        final UserDetails userDetails = (org.springframework.security.core.userdetails.UserDetails) auth.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String token = jwtService.generateToken(userDetails.getUsername(), roles);

        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(false) // set to true when using HTTPS
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofMillis(jwtService.getJwtExpiration()))
                .build();

        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .location(URI.create("/ui/events"))
                .build();
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestParam String email,
                                       @RequestParam String password,
                                       @RequestParam String confirmAdminCode,
                                       @RequestParam String name) {
        UserRole role = UserRole.USER;
        if ("admin123".equals(confirmAdminCode)) {
            role = UserRole.ADMIN;
        }

        String normalizedEmail = email.trim().toLowerCase();
        if (userService.findByEmailHash(normalizedEmail).isEmpty()) {
            final UserDto user = new UserDto(
                    null,
                    name,
                    normalizedEmail,
                    passwordEncoder.encode(password),
                    role,
                    null,
                    null);
            userService.create(user);
            return ResponseEntity.status(HttpStatus.SEE_OTHER)
                    .location(URI.create("/ui/auth/login?registered"))
                    .build();
        }
        return ResponseEntity.status(HttpStatus.SEE_OTHER)
                .location(URI.create("/ui/auth/register?error=exists"))
                .build();

    }

}
