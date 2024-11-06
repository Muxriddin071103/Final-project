package uz.app.payload;

import java.time.LocalDateTime;

public record LikeDTO(Long id, Long articleId, Long userId, LocalDateTime likedAt) { }
