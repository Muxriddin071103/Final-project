package uz.app.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookmarkDTO {
    private Long id;
    private Long articleId;
    private Long userId;
    private LocalDateTime bookmarkedAt;
}
