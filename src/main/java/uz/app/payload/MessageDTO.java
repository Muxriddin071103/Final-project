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
    private UserDTO senderId;
    private UserDTO receiverId;
    private boolean isRead;
    private LocalDateTime sentAt;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserDTO{
        private Long id;
        private String firstName;
        private String lastName;
    }
}


