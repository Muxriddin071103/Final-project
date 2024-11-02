package uz.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.app.entity.Media;

public interface MediaRepository extends JpaRepository<Media, Long> {
}
