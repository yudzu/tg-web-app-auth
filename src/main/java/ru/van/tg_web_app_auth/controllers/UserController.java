package ru.van.tg_web_app_auth.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.van.tg_web_app_auth.entities.User;
import ru.van.tg_web_app_auth.repositories.UserRepository;
import ru.van.tg_web_app_auth.services.TgAuthService;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final TgAuthService tgAuthService;
    private final UserRepository userRepository;

    @GetMapping("/")
    public String index(@RequestParam(required = false) String initData, Model model) {
        if (!tgAuthService.validateInitData(initData)) {
            model.addAttribute("error", "Invalid or missing initData");
            model.addAttribute("isAuthenticated", false);
            return "error";
        }
        Map<String, String> data = tgAuthService.parseInitData(initData);
        User user = new User();
        user.setId(Long.parseLong(data.get("user_id")));
        user.setFirstName(data.get("first_name"));
        user.setLastName(data.get("last_name"));
        user.setUsername(data.get("username"));

        userRepository.save(user);

        model.addAttribute("user", user);
        model.addAttribute("isAuthenticated", true);
        return "index";
    }
}
