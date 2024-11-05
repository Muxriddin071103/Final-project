package uz.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import uz.app.config.JwtProvider;
import uz.app.entity.User;
import uz.app.entity.enums.UserRole;
import uz.app.payload.UserDTO;
import uz.app.payload.LoginRequest;
import uz.app.repository.UserRepository;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.username())) {
            throw new RuntimeException("Username is already in use");
        }
        User user = User
                .builder()
                .password(passwordEncoder.encode(userDTO.password()))
                .username(userDTO.username())
                .role(UserRole.ROLE_USER)
                .createdAt(LocalDateTime.now())
                .enabled(false)
                .build();
        userRepository.save(user);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.username())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isEnabled()) {
            user.setEnabled(true);
            userRepository.save(user);
        }

        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        String token = jwtProvider.generateToken(user);
        return ResponseEntity.ok().body(token);
    }
}
