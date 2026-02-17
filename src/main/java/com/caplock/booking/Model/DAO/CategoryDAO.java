package com.caplock.booking.Model.DAO;
import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDAO {
    private Long id;
    private String name;
    private String description;

}
