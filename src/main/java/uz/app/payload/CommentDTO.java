package uz.app.payload;

import java.time.LocalDateTime;

public record CommentDTO(Long id, Long articleId, Long userId, String message, LocalDateTime createdAt) { }
