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
public class UserDTO {
    private Integer id;
    private String name;
    private UserRole role;
    private String createdAt;
    private String updatedAt;
}
