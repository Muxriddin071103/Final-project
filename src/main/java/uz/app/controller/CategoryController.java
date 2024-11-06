package uz.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.app.entity.Category;
import uz.app.payload.CategoryArticlesDTO;
import uz.app.payload.CategoryDTO;
import uz.app.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Category> createCategory(@RequestBody CategoryDTO categoryDTO) {
        Category category = Category.builder()
                .name(categoryDTO.name())
                .description(categoryDTO.description())
                .build();

        Category createdCategory = categoryService.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    @GetMapping
    public ResponseEntity<List<CategoryArticlesDTO>> getAllCategories() {
        List<CategoryArticlesDTO> categoriesWithArticles = categoryService.findAllWithArticles();
        return ResponseEntity.ok(categoriesWithArticles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryArticlesDTO> getCategoryById(@PathVariable Long id) {
        return categoryService.findByIdWithArticles(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable Long id) {
        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

