package com.example.crawling.controller;

import com.example.crawling.model.User;
import com.example.crawling.repository.UserRepository;
import com.example.crawling.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/signup")
    public String singupForm(Model model) {
        model.addAttribute("user", new User());
        return "user/signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute User user, @RequestParam("passwordConfirm") String passwordConfirm) {

        userService.signup(user,passwordConfirm);
        return "redirect:user/login?success";
    }

    @GetMapping("/login")
    public String login() {
        return "user/login";
    }
}
