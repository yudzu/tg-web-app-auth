package ru.van.tg_web_app_auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.van.tg_web_app_auth.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
