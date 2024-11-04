package uz.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.app.entity.Article;
import uz.app.entity.User;
import uz.app.repository.ArticleRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ArticleService {

    private final ArticleRepository articleRepository;

    public Article save(Article article) {
        return articleRepository.save(article);
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public Optional<Article> findById(Long id) {
        return articleRepository.findById(id);
    }

    public void deleteById(Long id) {
        articleRepository.deleteById(id);
    }

    public List<Article> findArticlesByAuthor(User author) {
        return articleRepository.findByAuthor(author);
    }
}
