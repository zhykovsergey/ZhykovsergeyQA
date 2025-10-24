package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * Модель данных для поста
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
    
    @JsonProperty("id")
    private Integer id;
    
    @JsonProperty("userId")
    private Integer userId;
    
    @JsonProperty("title")
    private String title;
    
    @JsonProperty("body")
    private String body;
    
    /**
     * Создать новый пост для тестирования
     */
    public static Post createTestPost() {
        return Post.builder()
                .userId(1)
                .title("Test Post Title")
                .body("This is a test post body content")
                .build();
    }
    
    /**
     * Создать пост с валидными данными
     */
    public static Post createValidPost() {
        return Post.builder()
                .userId(1)
                .title("Valid Post Title")
                .body("Valid post body with sufficient content")
                .build();
    }
    
    /**
     * Создать пост с невалидными данными
     */
    public static Post createInvalidPost() {
        return Post.builder()
                .userId(null)
                .title("")
                .body("")
                .build();
    }
}
