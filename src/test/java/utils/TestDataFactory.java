package utils;

import models.Post;
import models.User;
import models.Comment;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Фабрика для создания тестовых данных
 */
public class TestDataFactory {
    
    private static final Random random = new Random();
    
    // ==================== ПОЛЬЗОВАТЕЛИ ====================
    
    /**
     * Создает валидного пользователя
     */
    public static User createValidUser() {
        return User.builder()
                .id(1)
                .name("Test User")
                .username("testuser")
                .email("test@example.com")
                .phone("123-456-7890")
                .website("test.com")
                .build();
    }
    
    /**
     * Создает невалидного пользователя
     */
    public static User createInvalidUser() {
        return User.builder()
                .id(-1)
                .name("")
                .username("")
                .email("invalid-email")
                .phone("")
                .website("")
                .build();
    }
    
    /**
     * Создает случайного пользователя
     */
    public static User createRandomUser() {
        String[] names = {"John Doe", "Jane Smith", "Bob Johnson", "Alice Brown", "Charlie Wilson"};
        String[] usernames = {"johndoe", "janesmith", "bobjohnson", "alicebrown", "charliewilson"};
        String[] emails = {"john@example.com", "jane@example.com", "bob@example.com", "alice@example.com", "charlie@example.com"};
        
        int index = random.nextInt(names.length);
        
        return User.builder()
                .id(random.nextInt(1000) + 1)
                .name(names[index])
                .username(usernames[index])
                .email(emails[index])
                .phone("555-" + String.format("%03d", random.nextInt(1000)) + "-" + String.format("%04d", random.nextInt(10000)))
                .website("example" + index + ".com")
                .build();
    }
    
    /**
     * Создает список пользователей
     */
    public static List<User> createUserList(int count) {
        return Arrays.asList(
                User.builder().id(1).name("User 1").username("user1").email("user1@example.com").build(),
                User.builder().id(2).name("User 2").username("user2").email("user2@example.com").build(),
                User.builder().id(3).name("User 3").username("user3").email("user3@example.com").build()
        );
    }
    
    // ==================== ПОСТЫ ====================
    
    /**
     * Создает валидный пост
     */
    public static Post createValidPost() {
        return Post.builder()
                .id(1)
                .userId(1)
                .title("Test Post Title")
                .body("This is a test post body content with some meaningful text.")
                .build();
    }
    
    /**
     * Создает невалидный пост
     */
    public static Post createInvalidPost() {
        return Post.builder()
                .id(-1)
                .userId(-1)
                .title("")
                .body("")
                .build();
    }
    
    /**
     * Создает случайный пост
     */
    public static Post createRandomPost() {
        String[] titles = {
                "Amazing Technology Trends",
                "The Future of Web Development",
                "Machine Learning Insights",
                "Cloud Computing Benefits",
                "Cybersecurity Best Practices"
        };
        
        String[] bodies = {
                "This is a comprehensive article about the latest trends in technology and how they impact our daily lives.",
                "Web development is evolving rapidly with new frameworks and tools emerging every day.",
                "Machine learning algorithms are becoming more sophisticated and accessible to developers.",
                "Cloud computing offers numerous advantages for businesses of all sizes.",
                "Cybersecurity is more important than ever in our digital world."
        };
        
        int index = random.nextInt(titles.length);
        
        return Post.builder()
                .id(random.nextInt(1000) + 1)
                .userId(random.nextInt(10) + 1)
                .title(titles[index])
                .body(bodies[index])
                .build();
    }
    
    /**
     * Создает пост с длинным содержимым
     */
    public static Post createLongPost() {
        StringBuilder longBody = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            longBody.append("This is a very long post content. ");
        }
        
        return Post.builder()
                .id(1)
                .userId(1)
                .title("Very Long Post Title")
                .body(longBody.toString())
                .build();
    }
    
    // ==================== КОММЕНТАРИИ ====================
    
    /**
     * Создает валидный комментарий
     */
    public static Comment createValidComment() {
        return Comment.builder()
                .id(1)
                .postId(1)
                .name("Test Commenter")
                .email("commenter@example.com")
                .body("This is a test comment with meaningful content.")
                .build();
    }
    
    /**
     * Создает невалидный комментарий
     */
    public static Comment createInvalidComment() {
        return Comment.builder()
                .id(-1)
                .postId(-1)
                .name("")
                .email("invalid-email")
                .body("")
                .build();
    }
    
    /**
     * Создает случайный комментарий
     */
    public static Comment createRandomComment() {
        String[] names = {"John Smith", "Jane Doe", "Bob Wilson", "Alice Brown", "Charlie Davis"};
        String[] emails = {"john@example.com", "jane@example.com", "bob@example.com", "alice@example.com", "charlie@example.com"};
        String[] bodies = {
                "Great article! Very informative.",
                "I disagree with some points, but overall good content.",
                "Thanks for sharing this valuable information.",
                "This helped me understand the topic better.",
                "Looking forward to more content like this."
        };
        
        int index = random.nextInt(names.length);
        
        return Comment.builder()
                .id(random.nextInt(1000) + 1)
                .postId(random.nextInt(100) + 1)
                .name(names[index])
                .email(emails[index])
                .body(bodies[index])
                .build();
    }
    
    // ==================== СПЕЦИАЛЬНЫЕ СЛУЧАИ ====================
    
    /**
     * Создает пользователя с SQL injection попыткой
     */
    public static User createUserWithSqlInjection() {
        return User.builder()
                .id(1)
                .name("'; DROP TABLE users; --")
                .username("admin' OR '1'='1")
                .email("test@example.com'; DROP TABLE users; --")
                .phone("123-456-7890")
                .website("test.com")
                .build();
    }
    
    /**
     * Создает пост с XSS попыткой
     */
    public static Post createPostWithXss() {
        return Post.builder()
                .id(1)
                .userId(1)
                .title("<script>alert('XSS')</script>")
                .body("<img src=x onerror=alert('XSS')>")
                .build();
    }
    
    /**
     * Создает пользователя с очень длинными полями
     */
    public static User createUserWithLongFields() {
        StringBuilder longName = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longName.append("A");
        }
        
        return User.builder()
                .id(1)
                .name(longName.toString())
                .username("verylongusername")
                .email("verylongemail@example.com")
                .phone("123-456-7890")
                .website("verylongwebsite.com")
                .build();
    }
    
    // ==================== УТИЛИТЫ ====================
    
    /**
     * Создает случайную строку заданной длины
     */
    public static String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < length; i++) {
            result.append(chars.charAt(random.nextInt(chars.length())));
        }
        
        return result.toString();
    }
    
    /**
     * Создает случайный email
     */
    public static String generateRandomEmail() {
        return generateRandomString(8) + "@" + generateRandomString(5) + ".com";
    }
    
    /**
     * Создает случайный телефон
     */
    public static String generateRandomPhone() {
        return String.format("555-%03d-%04d", random.nextInt(1000), random.nextInt(10000));
    }
}
