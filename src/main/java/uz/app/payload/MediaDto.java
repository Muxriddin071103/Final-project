package uz.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaDto {
    private Long id;
    private String fileName;
    private String fileUrl;
    private LocalDateTime uploadedAt;
}
