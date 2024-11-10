package uz.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import uz.app.entity.Article;
import uz.app.entity.Bookmark;
import uz.app.entity.User;
import uz.app.payload.*;
import uz.app.service.ArticleService;
import uz.app.service.BookmarkService;
import uz.app.service.UserService;
import uz.app.util.UserUtil;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
public class UserController {

    private final UserService userService;
    private final ArticleService articleService;
    private final UserUtil userUtil;
    private final PasswordEncoder passwordEncoder;
    private final BookmarkService bookmarkService;

    @PutMapping
    public ResponseEntity<?> updateUserProfile(@RequestBody UpdateProfileDTO updateProfileDTO) {
        Optional<User> currentUser = userUtil.getCurrentUser();
        if (currentUser.isPresent()) {
            User user = currentUser.get();


            if (!passwordEncoder.matches(updateProfileDTO.oldPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("old password is incorrect");
            }

            user.setFirstName(updateProfileDTO.firstName());
            user.setLastName(updateProfileDTO.lastName());
            user.setUsername(updateProfileDTO.username());


            if (updateProfileDTO.newPassword() != null && !updateProfileDTO.newPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(updateProfileDTO.newPassword()));
            }

            user.setAge(updateProfileDTO.age());
            User savedUser = userService.save(user);
            return ResponseEntity.ok(savedUser);
        }
        return ResponseEntity.notFound().build();
    }



    @GetMapping("/articles")
    public ResponseEntity<?> getUserArticles() {
        Optional<User> currentUser = userUtil.getCurrentUser();
        if (currentUser.isPresent()) {
            List<Article> articles = articleService.findArticlesByAuthor(currentUser.get());
            List<ArticleSummaryDTO> articleSummaryDTOs = articleService.convertToSummaryDto(articles);
            return ResponseEntity.ok(articleSummaryDTOs);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/bookmarks")
    public ResponseEntity<?> getUserBookmarks() {
        Optional<User> currentUser = userUtil.getCurrentUser();
        if (currentUser.isPresent()) {
            List<Bookmark> bookmarks = bookmarkService.findByUser(currentUser.get());
            List<BookmarkDTO> bookmarkDTOs = bookmarks.stream()
                    .map(bookmark -> new BookmarkDTO(
                            bookmark.getId(),
                            bookmark.getArticle().getId(),
                            bookmark.getUser().getId(),
                            bookmark.getBookmarkedAt()
                    )).collect(Collectors.toList());
            return ResponseEntity.ok(bookmarkDTOs);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<ProfileDTO> getUserProfile() {
        Optional<User> currentUser = userUtil.getCurrentUser();
        if (currentUser.isPresent()) {
            User user = currentUser.get();

            ProfileDTO profileDTO = new ProfileDTO();
            profileDTO.setUserId(user.getId());
            profileDTO.setUsername(user.getUsername());
            System.out.println("user: " + user.getUsername());
            if (user.getArticles() != null) {
                List<ProfileDTO.PublishedArticleDTO> articles = user.getArticles().stream()
                        .map(article -> new ProfileDTO.PublishedArticleDTO(
                                article.getId(),
                                article.getTitle(),
                                article.getSummary(),
                                article.getViews().size(),
                                article.getLikes().size()
                        ))
                        .collect(Collectors.toList());
                profileDTO.setPublishedArticles(articles);
            } else {
                System.out.println("article not found");
            }

            if (user.getFollowers() != null) {
                List<Long> followerUserIds = user.getFollowers().stream()
                        .map(subscription -> subscription.getFollower().getId())
                        .collect(Collectors.toList());
                profileDTO.setFollowerIds(followerUserIds);
            } else {
                System.out.println("subscriber not found");
            }

            return ResponseEntity.ok(profileDTO);
        }
        return ResponseEntity.notFound().build();
    }


}
