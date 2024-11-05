package uz.app.payload;

public record ArticleDTO(String title, String content, String summary, Long mediaId, Long categoryId) {
}
