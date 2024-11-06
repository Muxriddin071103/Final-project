package uz.app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uz.app.entity.Article;
import uz.app.entity.Comment;
import uz.app.entity.User;
import uz.app.payload.CommentDTO;
import uz.app.service.ArticleService;
import uz.app.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final ArticleService articleService;

    @PostMapping("/add")
    public ResponseEntity<?> addComment(@Valid @RequestBody CommentDTO commentDto,
                                        @AuthenticationPrincipal User user) {
        Optional<Article> articleOpt = articleService.findById(commentDto.articleId());
        if (articleOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Article not found.");
        }

        Comment comment = Comment.builder()
                .article(articleOpt.get())
                .user(user)
                .message(commentDto.message())
                .createdAt(LocalDateTime.now())
                .build();

        commentService.save(comment);
        return ResponseEntity.ok("Comment added successfully.");
    }

    @GetMapping("/article/{articleId}")
    public ResponseEntity<?> getCommentsByArticle(@PathVariable Long articleId) {
        Optional<Article> articleOpt = articleService.findById(articleId);
        if (articleOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<CommentDTO> comments = commentService.findByArticleId(articleOpt.get().getId())
                .stream()
                .map(comment -> new CommentDTO(
                        comment.getId(),
                        comment.getArticle().getId(),
                        comment.getUser().getId(),
                        comment.getMessage(),
                        comment.getCreatedAt()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {
        if (commentService.findById(id).isPresent()) {
            commentService.deleteById(id);
            return ResponseEntity.ok("Comment deleted successfully.");
        }
        return ResponseEntity.notFound().build();
    }
}
