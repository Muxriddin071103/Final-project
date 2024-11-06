package uz.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.app.entity.Bookmark;
import uz.app.entity.User;
import uz.app.repository.BookmarkRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    public Bookmark save(Bookmark bookmark) {
        return bookmarkRepository.save(bookmark);
    }

    public Optional<Bookmark> findById(Long id) {
        return bookmarkRepository.findById(id);
    }

    public List<Bookmark> findByUser(User user) {
        return bookmarkRepository.findByUser(user);
    }

    public Optional<Bookmark> findByUserAndArticle(Long userId, Long articleId) {
        return bookmarkRepository.findByUserIdAndArticleId(userId, articleId);
    }

    public void deleteById(Long id) {
        bookmarkRepository.deleteById(id);
    }
}
