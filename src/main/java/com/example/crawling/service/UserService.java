package com.example.crawling.service;

import com.example.crawling.exception.PasswordChangeException;
import com.example.crawling.exception.SignupValidationException;
import com.example.crawling.model.User;
import com.example.crawling.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(User user, String passwordConfirm) {

        validateSignup(user, passwordConfirm);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        userRepository.save(user);
    }

    private void validateSignup(User user, String passwordConfirm) {

        // 1. 아이디 필수
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new SignupValidationException("아이디는 필수입니다.");
        }
        // 2. 아이디 길이
        if (user.getUsername().length() < 6 || user.getUsername().length() > 30) {
            throw new SignupValidationException("아이디는 6자 이상 30자 이하로 입력해야 합니다.");
        }

        // 3. 아이디 중복
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new SignupValidationException("이미 존재하는 아이디입니다.");
        }

        // 4. 비밀번호 필수
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new SignupValidationException("비밀번호는 필수입니다.");
        }

        // 5. 비밀번호 길이
        if (user.getPassword().length() < 8 || user.getPassword().length() > 20) {
            throw new SignupValidationException("비밀번호는 8자 이상 20자 이하로 입력해야 합니다.");
        }

        // 6. 숫자 포함
        if (!user.getPassword().matches(".*[0-9].*")) {
            throw new SignupValidationException("비밀번호는 숫자를 1개 이상 포함해야 합니다.");
        }

        // 7. 비밀번호 확인 필수
        if (passwordConfirm == null || passwordConfirm.trim().isEmpty()) {
            throw new SignupValidationException("비밀번호 확인은 필수입니다.");
        }

        // 8. 비밀번호 = 확인
        if (!user.getPassword().equals(passwordConfirm)) {
            throw new SignupValidationException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

    }

    public void changePassword(String username, String currentPassword,
                               String newPassword, String newPasswordConfirm) {

        // 1. 사용자 조회
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new PasswordChangeException("사용자를 찾을 수 없습니다."));

        // 2. 현재 비밀번호 확인
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new PasswordChangeException("현재 비밀번호가 일치하지 않습니다.");
        }

        // 3. 새 비밀번호 필수
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new PasswordChangeException("새 비밀번호는 필수입니다.");
        }

        // 4. 새 비밀번호 길이
        if (newPassword.length() < 8 || newPassword.length() > 20) {
            throw new PasswordChangeException("새 비밀번호는 8자 이상 20자 이하로 입력해야 합니다.");
        }

        // 5. 새 비밀번호 숫자 포함
        if (!newPassword.matches(".*[0-9].*")) {
            throw new PasswordChangeException("새 비밀번호는 숫자를 1개 이상 포함해야 합니다.");
        }

        // 6. 새 비밀번호 확인 필수
        if (newPasswordConfirm == null || newPasswordConfirm.trim().isEmpty()) {
            throw new PasswordChangeException("새 비밀번호 확인은 필수입니다.");
        }

        // 7. 새 비밀번호 = 확인
        if (!newPassword.equals(newPasswordConfirm)) {
            throw new PasswordChangeException("새 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        // 8. 현재 비밀번호와 새 비밀번호가 같은지 확인
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new PasswordChangeException("새 비밀번호는 현재 비밀번호와 달라야 합니다.");
        }

        // 9. 비밀번호 변경
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
