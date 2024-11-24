package uz.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.app.entity.Like;
import uz.app.entity.Article;
import uz.app.entity.User;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findByArticle(Article article);
    List<Like> findByUser(User user);
    Optional<Like> findByArticleAndUser(Article article, User user);
    int countByArticleId(Long articleId);
}
