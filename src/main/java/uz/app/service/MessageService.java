package uz.app.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.app.entity.Message;
import uz.app.entity.User;
import uz.app.repository.MessageRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public Message sendMessage(User sender, User receiver, String content) {
        if (receiver == null) {
            throw new IllegalArgumentException("Receiver not found");
        }
        Message message = Message.builder()
                .message(content)
                .sender(sender)
                .receiver(receiver)
                .isRead(false)
                .sentAt(LocalDateTime.now())
                .build();
        return messageRepository.save(message);
    }

    public Message replyToMessage(Long messageId, String content, User admin) {
        Optional<Message> originalMessage = messageRepository.findById(messageId);
        if (originalMessage.isPresent()) {
            Message replyMessage = new Message();
            replyMessage.setMessage(content);
            replyMessage.setSender(admin);
            replyMessage.setReceiver(originalMessage.get().getSender()); // Assuming admin replies to the user
            replyMessage.setSentAt(LocalDateTime.now());
            replyMessage.setRead(false); // Set as unread
            return messageRepository.save(replyMessage);
        } else {
            throw new EntityNotFoundException("Message not found");
        }
    }

    public Message findById(Long messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message not found with ID: " + messageId));
    }

    public long getUnreadMessagesCount(User receiver) {
        return messageRepository.findByReceiverAndIsReadFalse(receiver).size();
    }

    public List<Message> getConversation(User user, User admin) {
        if (user == null || admin == null) {
            throw new IllegalArgumentException("User or Admin cannot be null");
        }
        return messageRepository.findBySenderAndReceiver(user, admin);
    }

    public void markAllAsRead(List<Message> messages) {
        for (Message message : messages) {
            if (!message.isRead()) {
                message.setRead(true); // Set the read status to true
                messageRepository.save(message); // Save the updated message back to the database
            }
        }
    }
}
