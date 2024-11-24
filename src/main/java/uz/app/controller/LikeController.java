package uz.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uz.app.entity.Article;
import uz.app.entity.Like;
import uz.app.entity.User;
import uz.app.payload.LikeDTO;
import uz.app.service.ArticleService;
import uz.app.service.LikeService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/likes")
public class LikeController {

    private final LikeService likeService;
    private final ArticleService articleService;

    @PostMapping("/add")
    public ResponseEntity<?> addLike(@RequestParam Long articleId, @AuthenticationPrincipal User user) {
        Optional<Article> articleOpt = articleService.findById(articleId);
        if (articleOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Article not found.");
        }

        Article article = articleOpt.get();
        if (likeService.findByArticleAndUser(article, user).isPresent()) {
            return ResponseEntity.badRequest().body("Already liked.");
        }

        Like like = Like.builder()
                .article(article)
                .user(user)
                .likedAt(LocalDateTime.now())
                .build();

        likeService.save(like);
        return ResponseEntity.ok("Article liked successfully.");
    }

    @GetMapping("/article/{articleId}")
    public ResponseEntity<?> getLikesByArticle(@PathVariable Long articleId) {
        Optional<Article> articleOpt = articleService.findById(articleId);
        if (articleOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<LikeDTO> likes = likeService.findByArticle(articleOpt.get())
                .stream()
                .map(like -> new LikeDTO(
                        like.getId(),
                        like.getArticle().getId(),
                        like.getUser().getId(),
                        like.getLikedAt()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(likes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLike(@PathVariable Long id) {
        Optional<Like> likeOpt = likeService.findById(id);
        if (likeOpt.isPresent()) {
            likeService.deleteById(id);
            return ResponseEntity.ok("Like removed successfully.");
        }
        return ResponseEntity.notFound().build();
    }

}
