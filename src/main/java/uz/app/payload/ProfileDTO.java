package uz.app.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDTO {
    private Long userId; // ID of the user
    private String username; // Username of the user
    private List<PublishedArticleDTO> publishedArticles; // List of articles
    private List<Long> followerIds; // List of follower IDs

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PublishedArticleDTO {
        private Long articleId; // Article ID
        private String title; // Title of the article
        private String summary; // Summary of the article
        private int viewCount; // Number of views
        private int likeCount; // Number of likes
    }
}
