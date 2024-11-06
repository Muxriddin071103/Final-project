package uz.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.app.entity.Bookmark;
import uz.app.entity.User;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findByUser(User user);
    Optional<Bookmark> findByUserIdAndArticleId(Long userId, Long articleId);
}
