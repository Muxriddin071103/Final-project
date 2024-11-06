package uz.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.app.entity.Category;
import uz.app.payload.CategoryArticleItemDTO;
import uz.app.payload.CategoryArticlesDTO;
import uz.app.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    public Optional<CategoryArticlesDTO> findByIdWithArticles(Long id) {
        return categoryRepository
                .findById(id)
                .map(category -> {
            List<CategoryArticleItemDTO> articles = category.getArticles().stream()
                    .map(article -> new CategoryArticleItemDTO(
                            article.getTitle(),
                            article.getSummary()
                    ))
                    .collect(Collectors.toList());

            return new CategoryArticlesDTO(category.getName(), articles);
        });
    }

    public List<CategoryArticlesDTO> findAllWithArticles() {
        return categoryRepository
                .findAll()
                .stream()
                .map(category -> {
                    List<CategoryArticleItemDTO> articles = category.getArticles().stream()
                            .map(article -> new CategoryArticleItemDTO(
                                    article.getTitle(),
                                    article.getSummary()
                            ))
                            .collect(Collectors.toList());

                    return new CategoryArticlesDTO(category.getName(), articles);
                })
                .collect(Collectors.toList());
    }
}
