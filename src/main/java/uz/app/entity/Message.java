package uz.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message; // The content of the message
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender; // The user who sent the message
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver; // The receiver (admin or user)
    private boolean isRead; // Indicates if the message is read
    private LocalDateTime sentAt; // Timestamp when the message was sent

    private boolean isReply; // Flag to indicate if it's a reply
}
