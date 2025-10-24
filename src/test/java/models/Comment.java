package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * Модель данных для комментария
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    
    @JsonProperty("id")
    private Integer id;
    
    @JsonProperty("postId")
    private Integer postId;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("email")
    private String email;
    
    @JsonProperty("body")
    private String body;
    
    /**
     * Создать тестовый комментарий
     */
    public static Comment createTestComment() {
        return Comment.builder()
                .postId(1)
                .name("Test Commenter")
                .email("commenter@example.com")
                .body("This is a test comment")
                .build();
    }
    
    /**
     * Создать комментарий для конкретного поста
     */
    public static Comment createCommentForPost(Integer postId) {
        return Comment.builder()
                .postId(postId)
                .name("Commenter for Post " + postId)
                .email("commenter" + postId + "@example.com")
                .body("Comment for post " + postId)
                .build();
    }
}
