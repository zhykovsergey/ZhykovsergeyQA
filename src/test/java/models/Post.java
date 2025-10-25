package models;

import lombok.Builder;
import lombok.Data;

/**
 * Модель для поста
 */
@Data
@Builder
public class Post {
    private int id;
    private int userId;
    private String title;
    private String body;

    /**
     * Создать валидный пост
     */
    public static Post createValidPost() {
        return Post.builder()
                .userId(1)
                .title("Test Post Title")
                .body("This is a test post body content")
                .build();
    }

    /**
     * Создать невалидный пост
     */
    public static Post createInvalidPost() {
        return Post.builder()
                .userId(-1)
                .title("")
                .body(null)
                .build();
    }
}