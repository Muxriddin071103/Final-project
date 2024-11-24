package uz.app.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.app.entity.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
    private Long id;
    private String content;
    private Long senderId; // Changed to just sender ID
    private Long receiverId; // Changed to just receiver ID
    private boolean isRead;
    private LocalDateTime sentAt;
}
