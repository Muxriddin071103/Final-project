package uz.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uz.app.entity.Message;
import uz.app.entity.User;
import uz.app.service.MessageService;
import uz.app.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;

    @PostMapping("/send")
    @PreAuthorize("hasRole('ROLE_USER')")
    public Message sendMessage(@AuthenticationPrincipal User sender,
                               @RequestParam Long receiverId,
                               @RequestParam String content) {
        Optional<User> receiver = userService.findUserById(receiverId);
        return messageService.sendMessage(sender, receiver.orElse(null), content);
    }

    @GetMapping("/unread/count")
    public long getUnreadMessagesCount(@AuthenticationPrincipal User admin) {
        return messageService.getUnreadMessagesCount(admin);
    }

    @GetMapping("/conversation/{userId}")
    public List<Message> getConversation(@AuthenticationPrincipal User admin,
                                         @PathVariable Long userId) {
        Optional<User> user = userService.findUserById(userId);
        return messageService.getConversation(user.orElse(null), admin);
    }

    @PutMapping("/markAsRead/{messageId}")
    public void markMessageAsRead(@PathVariable Long messageId) {
        Message message = messageService.findById(messageId);
        messageService.markAsRead(message);
    }

}
