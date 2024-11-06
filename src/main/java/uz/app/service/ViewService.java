package uz.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.app.entity.Article;
import uz.app.entity.User;
import uz.app.entity.View;
import uz.app.repository.ViewRepository;

import java.time.LocalDateTime;

@Service
public class ViewService {

    @Autowired
    private ViewRepository viewRepository;

    public void trackView(Article article, User user) {
        if (viewRepository.findByArticleAndUser(article, user).isEmpty()) {
            View view = View.builder()
                    .article(article)
                    .user(user)
                    .viewedAt(LocalDateTime.now())
                    .build();
            viewRepository.save(view);
        }
    }

    public int getViewsCount(Long id) {
        return viewRepository.countByArticleId(id);
    }
}
