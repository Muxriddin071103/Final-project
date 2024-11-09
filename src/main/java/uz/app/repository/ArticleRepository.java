package uz.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.app.entity.Article;
import uz.app.entity.User;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByAuthor(User author);
    List<Article> findByAuthorId(Long userId);
    List<Article> findByTitleContainingIgnoreCase(String title);
    List<Article> findByCategoryId(Long categoryId);
    Optional<Article> findByMediaId(Long mediaId);
}
