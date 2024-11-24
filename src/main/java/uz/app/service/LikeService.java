package uz.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.app.entity.Like;
import uz.app.entity.Article;
import uz.app.entity.User;
import uz.app.repository.LikeRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;

    public Like save(Like like) {
        return likeRepository.save(like);
    }

    public Optional<Like> findByArticleAndUser(Article article, User user) {
        return likeRepository.findByArticleAndUser(article, user);
    }

    public List<Like> findByArticle(Article article) {
        return likeRepository.findByArticle(article);
    }

    public List<Like> findByUser(User user) {
        return likeRepository.findByUser(user);
    }

    public void deleteById(Long id) {
        likeRepository.deleteById(id);
    }

    public Optional<Like> findById(Long id) {
        return likeRepository.findById(id);
    }

    public int getLikesCountByArticle(Long id) {
        return likeRepository.countByArticleId(id);
    }
}
