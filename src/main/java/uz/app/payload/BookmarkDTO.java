package uz.app.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookmarkDTO {
    private Long id;
    private ArticleSummaryDTO articleSummaryDTO;
    private Long userId;
    private LocalDateTime bookmarkedAt;
}
