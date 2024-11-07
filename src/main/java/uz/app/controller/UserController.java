package uz.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import uz.app.entity.Article;
import uz.app.entity.Bookmark;
import uz.app.entity.User;
import uz.app.payload.ArticleSummaryDTO;
import uz.app.payload.BookmarkDTO;
import uz.app.payload.ProfileDTO;
import uz.app.payload.UserDTO;
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
    public ResponseEntity<?> updateUserProfile(@RequestBody UserDTO userDto) {
        Optional<User> currentUser = userUtil.getCurrentUser();
        if (currentUser.isPresent()) {
            User user = currentUser.get();
            user.setUsername(userDto.username());
            user.setPassword(passwordEncoder.encode(userDto.password()));
            user.setAge(userDto.age());
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


            List<Long> followerUserIds = user.getFollowers().stream()
                    .map(subscription -> subscription.getFollower().getId())
                    .collect(Collectors.toList());
            profileDTO.setFollowerIds(followerUserIds);

            return ResponseEntity.ok(profileDTO);
        }
        return ResponseEntity.notFound().build();
    }


}
