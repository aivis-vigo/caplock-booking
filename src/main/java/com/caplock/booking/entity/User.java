package com.caplock.booking.entity;

import com.caplock.booking.UserRole;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
    private @Getter @Setter Integer id;
    private @Getter @Setter String name;
    private @Getter @Setter String emailHash;
    private @Getter @Setter String passwordHash;
    private @Getter @Setter UserRole role;
    private @Getter @Setter String notificationToken;
    private @Getter @Setter String createdAt;
    private @Getter @Setter String updatedAt;
}
