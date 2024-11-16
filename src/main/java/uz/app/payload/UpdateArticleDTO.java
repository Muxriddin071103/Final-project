package uz.app.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateArticleDTO {
    @NotBlank
    private String title;
    @NotBlank
    private String summary;
    private Long mediaId;
    private Long categoryId;
    @NotNull
    private String status;
}
