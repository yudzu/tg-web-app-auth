package ru.van.tg_web_app_auth.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tg_user")
public class TgUser {
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
}
