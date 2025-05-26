package ru.van.tg_web_app_auth.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class User {
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
}
