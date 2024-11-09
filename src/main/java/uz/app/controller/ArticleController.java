package uz.app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uz.app.entity.*;
import uz.app.entity.enums.Status;
import uz.app.payload.ArticleDTO;
import uz.app.payload.CreateArticleDTO;
import uz.app.payload.UpdateArticleDTO;
import uz.app.service.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/articles")
public class ArticleController {

    private final MediaService mediaService;
    private final ArticleService articleService;
    private final CategoryService categoryService;
    private final ViewService viewService;
    private final CommentService commentService;
    private final LikeService likeService;

    @PostMapping("/add")
    public ResponseEntity<?> addArticle(@Valid @RequestBody CreateArticleDTO articleDto,
                                        @AuthenticationPrincipal User author) {

        Optional<Media> mediaOpt = mediaService.getMediaById(articleDto.getMediaId());

        if (mediaOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Media not found");
        }

        Optional<Category> categoryOpt = categoryService.findById(articleDto.getCategoryId());

        if (categoryOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Category not found");
        }

        Article article = Article.builder()
                .title(articleDto.getTitle())
                .summary(articleDto.getSummary())
                .media(mediaOpt.get())
                .status(Status.CREATED)
                .author(author)
                .category(categoryOpt.get())
                .publishedAt(LocalDateTime.now())
                .build();

        articleService.save(article);
        return ResponseEntity.ok("Article added successfully!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getArticle(@PathVariable Long id, @AuthenticationPrincipal User user) {
        Optional<Article> articleOpt = articleService.findById(id);

        if (articleOpt.isPresent()) {
            Article article = articleOpt.get();

            if(article.getStatus() == Status.DELETED){
                return ResponseEntity.noContent().build();
            }

            viewService.trackView(article, user);
            List<String> comments = commentService.findByArticleId(id)
                    .stream()
                    .map(Comment::getMessage)
                    .collect(Collectors.toList());

            ArticleDTO articleDto = new ArticleDTO();
            articleDto.setId(article.getId());
            articleDto.setTitle(article.getTitle());
            articleDto.setSummary(article.getSummary());
            articleDto.setMediaId(article.getMedia().getId());
            articleDto.setCategoryId(article.getCategory().getId());
            articleDto.setAuthorUsername(article.getAuthor().getUsername());
            articleDto.setCategoryName(article.getCategory().getName());
            articleDto.setPublishedAt(article.getPublishedAt());
            articleDto.setStatus(article.getStatus().name());
            articleDto.setComments(comments);
            articleDto.setViewsCount(viewService.getViewsCount(article.getId()));
            articleDto.setLikesCount(likeService.getLikesCountByArticle(article.getId()));

            return ResponseEntity.ok(articleDto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<?> getAllArticles() {
        List<ArticleDTO> articleDTOs = articleService.findAll()
                .stream()
                .filter(article -> article.getStatus() != Status.DELETED)
                .map(article -> {
                    List<String> comments = commentService.findByArticleId(article.getId())
                            .stream()
                            .map(Comment::getMessage)
                            .collect(Collectors.toList());

                    return new ArticleDTO(
                            article.getId(),
                            article.getTitle(),
                            article.getSummary(),
                            article.getMedia().getFileName(),
                            article.getMedia().getId(),
                            article.getCategory().getId(),
                            article.getAuthor().getUsername(),
                            article.getCategory().getName(),
                            article.getPublishedAt(),
                            article.getStatus().name(),
                            comments,
                            viewService.getViewsCount(article.getId()),
                            likeService.getLikesCountByArticle(article.getId())
                    );
                }).collect(Collectors.toList());

        return ResponseEntity.ok(articleDTOs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateArticle(@PathVariable Long id,
                                           @Valid @RequestBody UpdateArticleDTO updateArticleDto,
                                           @AuthenticationPrincipal User author) {
        Optional<Article> articleOpt = articleService.findById(id);

        if (articleOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Article article = articleOpt.get();

        article.setTitle(updateArticleDto.getTitle());
        article.setSummary(updateArticleDto.getSummary());

        if (updateArticleDto.getMediaId() != null) {
            Optional<Media> mediaOpt = mediaService.getMediaById(updateArticleDto.getMediaId());
            if (mediaOpt.isPresent()) {
                article.setMedia(mediaOpt.get());
            } else {
                return ResponseEntity.badRequest().body("Invalid media ID");
            }
        }

        if (updateArticleDto.getCategoryId() != null) {
            Optional<Category> categoryOpt = categoryService.findById(updateArticleDto.getCategoryId());
            if (categoryOpt.isPresent()) {
                article.setCategory(categoryOpt.get());
            } else {
                return ResponseEntity.badRequest().body("Invalid category ID");
            }
        }

        try {
            Status status = Status.valueOf(updateArticleDto.getStatus());
            article.setStatus(status);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid status");
        }

        articleService.save(article);
        return ResponseEntity.ok("Article updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteArticle(@PathVariable Long id) {
        Optional<Article> articleOpt = articleService.findById(id);

        if (articleOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Article article = articleOpt.get();

        if (article.getStatus() == Status.DELETED) {
            return ResponseEntity.badRequest().body("Sorry! This article has already been deleted.");
        }

        article.setStatus(Status.DELETED);
        articleService.save(article);

        return ResponseEntity.ok("Article marked as deleted successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchArticles(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long authorId) {

        List<Article> articles = articleService.searchArticles(title, categoryId, authorId);

        if (articles.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<ArticleDTO> articleDTOs = articles
                .stream()
                .filter(article -> article.getStatus() != Status.DELETED)
                .map(article -> {
                    List<String> comments = commentService.findByArticleId(article.getId())
                            .stream()
                            .map(Comment::getMessage)
                            .collect(Collectors.toList());

                    return new ArticleDTO(
                            article.getId(),
                            article.getTitle(),
                            article.getSummary(),
                            article.getMedia().getFileName(),
                            article.getMedia().getId(),
                            article.getCategory().getId(),
                            article.getAuthor().getUsername(),
                            article.getCategory().getName(),
                            article.getPublishedAt(),
                            article.getStatus().name(),
                            comments,
                            viewService.getViewsCount(article.getId()),
                            likeService.getLikesCountByArticle(article.getId())
                    );
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(articleDTOs);
    }

}
