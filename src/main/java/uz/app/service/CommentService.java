package uz.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.app.entity.Comment;
import uz.app.entity.Article;
import uz.app.repository.CommentRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }

    public List<Comment> findByArticleId(Long id) {
        return commentRepository.findByArticleId(id);
    }

    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }
}
