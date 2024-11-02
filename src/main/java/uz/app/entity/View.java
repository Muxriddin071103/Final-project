package uz.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.app.config.LocalDateTimeAttributeConverter;

import java.time.LocalDateTime;

@Entity
@Table(name = "views")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class View {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime viewedAt;

}
