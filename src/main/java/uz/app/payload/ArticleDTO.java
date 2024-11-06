package uz.app.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDTO {
    private Long id;
    private String title;
    private String summary;
    private String mediaUrl;
    private Long mediaId;
    private Long categoryId;
    private String authorUsername;
    private String categoryName;
    private LocalDateTime publishedAt;
    private String status;
    private List<String> comments;
    private int viewsCount;
    private int likesCount;
}
