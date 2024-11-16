package uz.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uz.app.entity.Message;
import uz.app.entity.User;
import uz.app.payload.MessageDTO;
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
                                  @RequestParam String content) {
        Long receiverId = 1L;
        Optional<User> receiver = userService.findUserById(receiverId);

        if (receiver.isEmpty()) {
            throw new IllegalArgumentException("Receiver with ID 1 not found");
        }

        Message message = messageService.sendMessage(sender, receiver.get(), content);
        return mapToDTO(message);
    }

    @GetMapping("/unread/count")
    public long getUnreadMessagesCount(@AuthenticationPrincipal User admin) {
        return messageService.getUnreadMessagesCount(admin);
    }

    @GetMapping("/conversation/{userId}")
    public List<MessageDTO> getConversation(@AuthenticationPrincipal User admin,
                                            @PathVariable Long userId) {
        Long senderId = 1L;

        Optional<User> sender = userService.findUserById(senderId);
        Optional<User> receiver = userService.findUserById(userId);

        if (sender.isEmpty() || receiver.isEmpty()) {
            throw new IllegalArgumentException("User not found for sender or receiver ID");
        }

        List<Message> messages = messageService.getConversation(sender.get(), receiver.get());

        messageService.markAllAsRead(messages);

        return messages.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @GetMapping("/unread")
    public ResponseEntity<?> getUnreadMessages(@AuthenticationPrincipal User user) {
        List<Message> unreadMessages = messageService.getUnreadMessagesForUser(user);
        List<MessageDTO> unreadMessageDTOs = unreadMessages.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(unreadMessageDTOs);
    }

    private MessageDTO mapToDTO(Message message) {
        return new MessageDTO(
                message.getId(),
                message.getMessage(),
                new MessageDTO.UserDTO(
                        message.getSender().getId(),
                        message.getSender().getFirstName(),
                        message.getSender().getLastName()
                ),
                new MessageDTO.UserDTO(
                        message.getReceiver().getId(),
                        message.getReceiver().getFirstName(),
                        message.getReceiver().getLastName()
                ),
                message.isRead(),
                message.getSentAt()
        );
    }
}
