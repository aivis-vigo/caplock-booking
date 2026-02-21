package com.caplock.booking.entity.dto;

import com.caplock.booking.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserDto {
    private Long id;
    private String name;
    private String emailHash;
    private String passwordHash;
    private UserRole role;
    private LocalDateTime createdAt;
    private String notificationToken;
}
