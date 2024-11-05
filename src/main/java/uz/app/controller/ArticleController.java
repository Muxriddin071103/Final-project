package uz.app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uz.app.entity.Article;
import uz.app.entity.Category;
import uz.app.entity.Media;
import uz.app.entity.User;
import uz.app.entity.enums.Status;
import uz.app.payload.ArticleDTO;
import uz.app.service.ArticleService;
import uz.app.service.CategoryService;
import uz.app.service.MediaService;
import uz.app.service.ViewService;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/articles")
public class ArticleController {

    private final MediaService mediaService;
    private final ArticleService articleService;
    private final CategoryService categoryService;
    private final ViewService viewService;

    @PostMapping("/add")
    public ResponseEntity<?> addArticle(@Valid @RequestBody ArticleDTO articleDto,
                                              @AuthenticationPrincipal User author) {

        Optional<Media> mediaOpt = mediaService.getMediaById(articleDto.mediaId());

        if (mediaOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        Optional<Category> categoryOpt = categoryService.findById(articleDto.categoryId());

        if (categoryOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        Article article = Article.builder()
                .title(articleDto.title())
                .content(articleDto.content())
                .summary(articleDto.summary())
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
        Optional<Article> article = articleService.findById(id);
        if (article.isPresent()) {
            viewService.trackView(article.get(), user);
            return ResponseEntity.ok(article.get());
        }
        return ResponseEntity.notFound().build();
    }
}
