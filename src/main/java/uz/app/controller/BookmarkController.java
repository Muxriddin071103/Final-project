package uz.app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uz.app.entity.Bookmark;
import uz.app.entity.User;
import uz.app.payload.BookmarkDTO;
import uz.app.service.ArticleService;
import uz.app.service.BookmarkService;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final ArticleService articleService;

    @PostMapping("/add")
    public ResponseEntity<?> addBookmark(@RequestParam Long articleId, @AuthenticationPrincipal User user) {
        Optional<Bookmark> existingBookmark = bookmarkService.findByUserAndArticle(user.getId(), articleId);
        if (existingBookmark.isPresent()) {
            return ResponseEntity.badRequest().body("Bookmark already exists for this article.");
        }

        return articleService.findById(articleId).map(article -> {
            Bookmark bookmark = Bookmark.builder()
                    .article(article)
                    .user(user)
                    .bookmarkedAt(LocalDateTime.now())
                    .build();
            Bookmark savedBookmark = bookmarkService.save(bookmark);
            return ResponseEntity.ok(convertToDto(savedBookmark));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBookmark(@PathVariable Long id, @AuthenticationPrincipal User user) {
        Optional<Bookmark> bookmark = bookmarkService.findById(id);
        if (bookmark.isPresent() && bookmark.get().getUser().getId().equals(user.getId())) {
            bookmarkService.deleteById(id);
            return ResponseEntity.ok("Bookmark deleted successfully.");
        }
        return ResponseEntity.notFound().build();
    }

    private BookmarkDTO convertToDto(Bookmark bookmark) {
        return new BookmarkDTO(
                bookmark.getId(),
                bookmark.getArticle().getId(),
                bookmark.getUser().getId(),
                bookmark.getBookmarkedAt()
        );
    }
}
