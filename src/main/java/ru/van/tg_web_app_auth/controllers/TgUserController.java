package ru.van.tg_web_app_auth.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.van.tg_web_app_auth.entities.TgUser;
import ru.van.tg_web_app_auth.repositories.TgUserRepository;
import ru.van.tg_web_app_auth.services.TgAuthService;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class TgUserController {
    private final TgAuthService tgAuthService;
    private final TgUserRepository tgUserRepository;

    @GetMapping("/")
    public String handleGet(@RequestParam(required = false) String initData, Model model) {
        return handleAuth(initData, model);
    }

    @PostMapping("/")
    public String handlePost(@RequestParam(required = false) String initData, Model model) {
        return handleAuth(initData, model);
    }

    private String handleAuth(String initData, Model model) {
        if (!tgAuthService.validateInitData(initData)) {
            model.addAttribute("error", "Invalid or missing initData");
            model.addAttribute("isAuthenticated", false);
            return "home";
        }
        Map<String, String> data = tgAuthService.parseInitData(initData);
        TgUser user = new TgUser();
        user.setId(Long.parseLong(data.get("user_id")));
        user.setFirstName(data.get("first_name"));
        user.setLastName(data.get("last_name"));
        user.setUsername(data.get("username"));

        tgUserRepository.save(user);

        model.addAttribute("user", user);
        model.addAttribute("isAuthenticated", true);
        return "home";
    }
}
