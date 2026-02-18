package com.caplock.booking.dto;

import com.caplock.booking.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserCreationDTO {
    private String name;
    private String email;
    private String password;
    private UserRole role;
}
