package uz.app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uz.app.entity.Article;
import uz.app.entity.Category;
import uz.app.entity.Media;
import uz.app.entity.User;
import uz.app.entity.enums.Status;
import uz.app.payload.ArticleDto;
import uz.app.repository.ArticleRepository;
import uz.app.repository.CategoryRepository;
import uz.app.service.ArticleService;
import uz.app.service.MediaService;
import uz.app.service.ViewService;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ArticleController {
    private ArticleRepository articleRepository;
private CategoryRepository categoryRepository;
    @Autowired
    MediaService mediaService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ViewService viewService;


    @PostMapping("/add")
    public ResponseEntity<Article> addArticle(@Valid @RequestBody ArticleDto articleDto,
                                              @AuthenticationPrincipal User author) {

        Media media = Media.builder()
                .fileName(articleDto.getMediaFileName())
                .fileUrl(articleDto.getMediaFileUrl())
                .uploadedBy(author)
                .uploadedAt(LocalDateTime.now())                .build();

         Media savedMedia = mediaService.saveMedia(media);

        Optional<Category> categoryOpt = categoryRepository.findById(articleDto.getCategoryId());

        if (categoryOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        Article article = Article.builder()
                .title(articleDto.getTitle())
                .content(articleDto.getContent())
                .summary(articleDto.getSummary())
                .media(savedMedia)
                .status(Status.CREATED)
                .author(author)
                .category(categoryOpt.get())
                .build();

        articleRepository.save(article);
        return ResponseEntity.ok(article);
    }

    @GetMapping("/articles/{id}")
    public Article getArticle(@PathVariable Long id, @AuthenticationPrincipal User user) {
        Optional<Article> article = articleService.findById(id);
        viewService.trackView(article.orElse(null), user);
        return article.orElse(null);
    }
}
