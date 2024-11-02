package uz.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.app.entity.Message;
import uz.app.entity.User;
import uz.app.repository.MessageRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;
    public Message sendMessage(User sender, User receiver, String content) {
        Message message = Message.builder()
                .message(content)
                .sender(sender)
                .receiver(receiver)
                .isRead(false)
                .sentAt(LocalDateTime.now())
                .build();
        return messageRepository.save(message);
    }
    public Message findById(Long messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message not found with ID: " + messageId));
    }


    public long getUnreadMessagesCount(User receiver) {
        return messageRepository.findByReceiverAndIsReadFalse(receiver).size();
    }
    public void markAsRead(Message message) {
        message.setIsRead(true);
        messageRepository.save(message);
    }

    public List<Message> getConversation(User user, User admin) {
        return messageRepository.findBySenderAndReceiver(user, admin);
    }
}
