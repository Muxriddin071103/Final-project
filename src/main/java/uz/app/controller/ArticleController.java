package uz.app.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.app.entity.Article;
import uz.app.service.ArticleService;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping
    public ResponseEntity<Article> createArticle(@RequestBody Article article) {
        Article build = Article.builder().
                title(article.getTitle()).
                content(article.getContent()).
                summary(article.getSummary()).
                imageUrl(article.getImageUrl()).
                publishedAt(article.getPublishedAt()).
                status(article.getStatus()).
                category(article.getCategory()).
                build();
        Article createdArticle = articleService.save(build);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdArticle);
    }
    //putmapping

    @GetMapping
    public ResponseEntity<List<Article>> getAllArticles() {
        return ResponseEntity.ok(articleService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable Long id) {
        return articleService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticleById(@PathVariable Long id) {
        articleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
