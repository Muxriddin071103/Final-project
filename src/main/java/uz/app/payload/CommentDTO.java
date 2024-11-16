package uz.app.payload;

import java.time.LocalDateTime;

public record CommentDTO(Long articleId, String message) { }
