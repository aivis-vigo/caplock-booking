package com.caplock.booking.util.old;

import com.caplock.booking.entity.old.object.User;
import com.caplock.booking.entity.old.dto.UserDTO;

import java.util.function.Function;

public class UserDTOMapper implements Function<User, UserDTO> {
    @Override
    public UserDTO apply(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
