package uz.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.app.entity.View;
import uz.app.entity.Article;
import uz.app.entity.User;

import java.util.Optional;

public interface ViewRepository extends JpaRepository<View, Long> {
    Optional<View> findByArticleAndUser(Article article, User user);
}
