package uz.app.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryArticlesDTO {
    private Long categoryId;
    private String name;
    private List<CategoryArticleItemDTO> articles;
}
