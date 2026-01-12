package com.example.crawling.controller;

import com.example.crawling.model.User;
import com.example.crawling.repository.UserRepository;
import com.example.crawling.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

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
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "message", required = false) String message,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model) {
        if (error != null) {
            if (message != null) {
                model.addAttribute("errorMessage", message);
            } else {
                model.addAttribute("errorMessage", "아이디 또는 비밀번호가 올바르지 않습니다.");
            }
        }
        if (logout != null) {
            model.addAttribute("logoutMessage", "로그아웃되었습니다.");
        }
        return "user/login";
    }

    @GetMapping("/change-password")
    public String changePasswordForm(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }
        model.addAttribute("username", principal.getName());
        return "user/change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(Principal principal,
                                 @RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("newPasswordConfirm") String newPasswordConfirm,
                                 RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/login";
        }

        userService.changePassword(principal.getName(), currentPassword, newPassword, newPasswordConfirm);
        redirectAttributes.addFlashAttribute("successMessage", "비밀번호가 성공적으로 변경되었습니다.");
        return "redirect:/change-password";
    }

    @GetMapping("/api/check-username")
    @ResponseBody
    public Map<String, Boolean> checkUsername(@RequestParam String username) {

        boolean exist = userRepository.existsByUsername(username);
        return Map.of("exists", exist);
    }

    @GetMapping("/api/check-password")
    @ResponseBody
    public Map<String, Boolean> checkPassword(Principal principal, @RequestParam String password) {
        Optional<User> user = userRepository.findByUsername(principal.getName());
        String currentPassword = user.get().getPassword();
        boolean exist = currentPassword.equals(passwordEncoder.encode(password));

        return Map.of("exists", exist);
    }
}
