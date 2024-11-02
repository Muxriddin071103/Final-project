package uz.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.app.config.LocalDateTimeAttributeConverter;

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
    @Column(columnDefinition = "TEXT")
    private String content;
    private String summary;
    private String imageUrl;

    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime publishedAt;

    private String status;

    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "article")
    private List<Comment> comments;

    @OneToMany(mappedBy = "article")
    private List<Bookmark> bookmarks;

    @OneToMany(mappedBy = "article")
    private List<Like> likes;

    @OneToMany(mappedBy = "article")
    private List<View> views;

    @ManyToMany
    @JoinTable(
            name = "article_tags",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;
}

