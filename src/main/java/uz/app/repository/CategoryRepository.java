package uz.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.app.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
