package uz.app.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ArticleDto {
    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotBlank(message = "Content is mandatory")
    private String content;

    private String summary;

    @NotBlank(message = "Media file name is mandatory")
    private String mediaFileName;

    @NotBlank(message = "Media file URL is mandatory")
    private String mediaFileUrl;

    private String status;

    @NotNull(message = "Category ID is mandatory")
    private Long categoryId;

}
