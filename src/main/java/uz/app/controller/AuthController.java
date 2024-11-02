package uz.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import uz.app.config.JwtProvider;
import uz.app.entity.User;
import uz.app.entity.enums.UserRole;
import uz.app.payload.UserDTO;
import uz.app.repository.UserRepository;

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
                .email(userDTO.email())
                .password(passwordEncoder.encode(userDTO.password()))
                .username(userDTO.username())
                .role(UserRole.USER)
                .enabled(false)
                .build();
        userRepository.save(user);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody UserDTO userDTO) {
        User user = userRepository.findByUsername(userDTO.username())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isEnabled()) {
            user.setEnabled(true);
            userRepository.save(user);
        }

        if (!passwordEncoder.matches(userDTO.password(), user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        String token = jwtProvider.generateToken(user);
        return ResponseEntity.ok().body(token);
    }
}
