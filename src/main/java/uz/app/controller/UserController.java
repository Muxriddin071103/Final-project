package uz.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.app.entity.Article;
import uz.app.entity.User;
import uz.app.service.ArticleService;
import uz.app.service.UserService;
import uz.app.util.UserUtil;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ArticleService articleService;
    private final UserUtil userUtil;

    @PutMapping("/profile")
    public ResponseEntity<User> updateUserProfile(@RequestBody User updatedUser) {
        Optional<User> currentUser = userUtil.getCurrentUser();
        if (currentUser.isPresent()) {
            currentUser.get().setUsername(updatedUser.getUsername());
            currentUser.get().setPassword(updatedUser.getPassword());
            currentUser.get().setEmail(updatedUser.getEmail());
            currentUser.get().setEnabled(updatedUser.isEnabled());
            User savedUser = userService.save(currentUser.orElse(null));
            return ResponseEntity.ok(savedUser);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/profile/articles")
    public ResponseEntity<List<Article>> getUserArticles() {
        Optional<User> currentUser = userUtil.getCurrentUser();
        if (currentUser.isPresent()) {
            return ResponseEntity.ok(articleService.findArticlesByAuthor(currentUser.orElse(null)));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/profile")
    public ResponseEntity<Optional<User>> getUserById() {
        Optional<User> currentUser = userUtil.getCurrentUser();
        if (currentUser.isPresent()) {
            return ResponseEntity.ok(currentUser);
        }
        return ResponseEntity.notFound().build();
    }

}





