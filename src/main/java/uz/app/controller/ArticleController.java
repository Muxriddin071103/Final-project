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
        return ResponseEntity.ok(article);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getArticle(@PathVariable Long id, @AuthenticationPrincipal User user) {
        Optional<Article> articleOpt = articleService.findById(id);

        if (articleOpt.isPresent()) {
            Article article = articleOpt.get();
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
        List<ArticleDTO> articleDTOs = articleService.findAll().stream().map(article -> {
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

}
