package uz.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.app.entity.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByArticleId(Long id);
}
