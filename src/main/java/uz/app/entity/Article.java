package uz.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Lazy;
import uz.app.config.LocalDateTimeAttributeConverter;
import uz.app.entity.enums.Status;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String summary;

    @ManyToOne
    @JoinColumn(name = "media_id", referencedColumnName = "id")
    private Media media;

    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime publishedAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "article")
    @Lazy
    private List<Comment> comments;

    @OneToMany(mappedBy = "article")
    private List<Bookmark> bookmarks;

    @OneToMany(mappedBy = "article")
    private List<Like> likes;

    @OneToMany(mappedBy = "article")
    private List<View> views;
}
