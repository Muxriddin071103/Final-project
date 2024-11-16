package uz.app.payload;

import java.time.LocalDateTime;

public record CommentDTOForView(Long comment_id,Long article_id,String message) {}
