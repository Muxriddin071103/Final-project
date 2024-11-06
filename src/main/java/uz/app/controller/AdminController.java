package uz.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uz.app.entity.Message;
import uz.app.entity.User;
import uz.app.payload.MessageDTO;
import uz.app.payload.ProfileDTO;
import uz.app.service.MessageService;
import uz.app.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final MessageService messageService;
    private final UserService userService;

    @GetMapping("/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ProfileDTO>> getAllUsers() {
        List<User> users = userService.findAllUsers(); // Retrieves all users from the UserService
        List<ProfileDTO> userProfiles = users.stream()
                .map(this::convertToProfileDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userProfiles);
    }

    private ProfileDTO convertToProfileDTO(User user) {
        List<ProfileDTO.PublishedArticleDTO> articles = user.getArticles().stream()
                .map(article -> new ProfileDTO.PublishedArticleDTO(
                        article.getId(),
                        article.getTitle(),
                        article.getSummary(),
                        article.getViews().size(),
                        article.getLikes().size()
                ))
                .collect(Collectors.toList());

        List<Long> followerIds = user.getFollowers().stream()
                .map(subscription -> subscription.getFollower().getId())
                .collect(Collectors.toList());

        return new ProfileDTO(
                user.getId(),
                user.getUsername(),
                articles,
                followerIds
        );
    }

    @PostMapping("/messages/{messageId}/reply")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<MessageDTO> replyToMessage(
            @PathVariable Long messageId,
            @RequestParam String content,
            @AuthenticationPrincipal User admin) {
        Message message = messageService.replyToMessage(messageId, content, admin);
        return ResponseEntity.ok(mapToDTO(message));
    }

    private MessageDTO mapToDTO(Message message) {
        return new MessageDTO(
                message.getId(),
                message.getMessage(),
                message.getSender().getId(),
                message.getReceiver().getId(),
                message.isRead(),
                message.getSentAt()
        );
    }
}
