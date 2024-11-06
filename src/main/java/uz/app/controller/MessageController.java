package uz.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uz.app.entity.Message;
import uz.app.entity.User;
import uz.app.payload.MessageDTO;
import uz.app.payload.UserDTO;
import uz.app.payload.UserSmsDto;
import uz.app.service.MessageService;
import uz.app.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;
    
    @PostMapping("/send")
    @PreAuthorize("hasRole('ROLE_USER')")
    public MessageDTO sendMessage(@AuthenticationPrincipal User sender,
                                  @RequestParam Long receiverId,
                                  @RequestParam String content) {
        Optional<User> receiver = userService.findUserById(receiverId);
        Message message = messageService.sendMessage(sender, receiver.orElse(null), content);
        return mapToDTO(message); // Map to DTO before returning
    }


    @PostMapping("/messages/{messageId}/reply")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<MessageDTO> replyToMessage(
            @PathVariable Long messageId,
            @RequestParam String content,
            @AuthenticationPrincipal User admin) {
        Message message = messageService.replyToMessage(messageId, content, admin);
        return ResponseEntity.ok(new MessageDTO(message.getId(), message.getMessage(),
                convertToUserDTO(message.getSender()).getId(),
                convertToUserDTO(message.getReceiver()).getId(),
                message.isRead(),
                message.getSentAt()));
    }

    private UserSmsDto convertToUserDTO(User sender) {
        if (sender == null) {
            return null;
        }
        return new UserSmsDto(sender.getId(),sender.getUsername());
    }

    @GetMapping("/unread/count")
    public long getUnreadMessagesCount(@AuthenticationPrincipal User admin) {
        return messageService.getUnreadMessagesCount(admin);
    }

    @GetMapping("/conversation/{userId}")
    public List<MessageDTO> getConversation(@AuthenticationPrincipal User admin,
                                            @PathVariable Long userId) {
        Optional<User> user = userService.findUserById(userId);
        List<Message> messages = messageService.getConversation(user.orElse(null), admin);

        messageService.markAllAsRead(messages);

        return messages.stream().map(this::mapToDTO).collect(Collectors.toList());
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
