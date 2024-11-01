package uz.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.app.entity.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
