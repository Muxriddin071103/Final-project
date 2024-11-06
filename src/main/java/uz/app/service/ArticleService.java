package uz.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.app.entity.Article;
import uz.app.entity.Comment;
import uz.app.entity.User;
import uz.app.payload.ArticleSummaryDTO;
import uz.app.repository.ArticleRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ArticleService {

    private final ArticleRepository articleRepository;

    public Optional<Article> findById(Long id) {
        return articleRepository.findById(id);
    }

    public List<Article> findArticlesByAuthor(User author) {
        return articleRepository.findByAuthor(author);
    }

    public void save(Article article) {
        articleRepository.save(article);
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public List<ArticleSummaryDTO> convertToSummaryDto(List<Article> articles) {
        return articles.stream().map(article -> new ArticleSummaryDTO(
                article.getTitle(),
                article.getSummary(),
                article.getMedia() != null ? article.getMedia().getFileName() : null,
                article.getCategory() != null ? article.getCategory().getName() : null,
                article.getPublishedAt(),
                article.getStatus().toString(),
                article.getComments() != null ? article.getComments().stream().map(Comment::getMessage).collect(Collectors.toList()) : null,  // Get comment contents if available
                article.getViews() != null ? article.getViews().size() : 0,
                article.getLikes() != null ? article.getLikes().size() : 0
        )).collect(Collectors.toList());
    }

}
