package api;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import utils.*;
import models.Post;
import models.User;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Улучшенный API тест с использованием всех новых утилит
 */
@Epic("API Testing")
@Feature("Improved API Tests")
public class ImprovedApiTest extends BaseApiTest {

    @Test
    @TestTag(id = "IMPROVED_API_001", description = "Улучшенный тест получения поста", category = "API", priority = 1)
    @Story("Improved Get Post")
    @DisplayName("Получить пост с улучшенными ассертами")
    @Description("Демонстрирует использование RestAssured, AssertUtils и детальной отчетности")
    public void testGetPostImproved() {
        // Используем RestAssured для получения поста
        var response = given()
                .when()
                .get("/posts/1")
                .then()
                .extract().response();
        
        // Проверяем статус-код с AssertUtils
        AssertUtils.assertStatusCode(200, response.getStatusCode(), "Статус-код должен быть 200");
        
        // Проверяем структуру ответа
        AssertUtils.assertTrue(response.getStatusCode() == 200, "Ответ должен быть успешным");
        AssertUtils.assertTrue(response.jsonPath().get("id") != null, "Ответ должен содержать поле id");
        AssertUtils.assertTrue(response.jsonPath().get("title") != null, "Ответ должен содержать поле title");
        AssertUtils.assertTrue(response.jsonPath().get("body") != null, "Ответ должен содержать поле body");
        
        // Проверяем значения полей
        String title = response.jsonPath().getString("title");
        String body = response.jsonPath().getString("body");
        
        AssertUtils.assertNotEmpty(title, "Заголовок поста не должен быть пустым");
        AssertUtils.assertNotEmpty(body, "Тело поста не должно быть пустым");
        
        // Дополнительные проверки с RestAssured
        response.then()
            .body("id", equalTo(1))
            .body("userId", equalTo(1))
            .body("title", notNullValue())
            .body("body", notNullValue());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    @TestTag(id = "IMPROVED_API_002", description = "Параметризованный тест постов", category = "API", priority = 2)
    @Story("Parameterized Posts")
    @DisplayName("Проверить посты с различными ID")
    @Description("Тестирует получение постов с разными ID")
    public void testGetPostsParameterized(int postId) {
        var response = given()
                .when()
                .get("/posts/" + postId)
                .then()
                .extract().response();
        
        AssertUtils.assertStatusCode(200, response.getStatusCode(), 
            String.format("Статус-код для поста %d должен быть 200", postId));
        
        String actualTitle = response.jsonPath().getString("title");
        AssertUtils.assertNotEmpty(actualTitle, 
            String.format("Заголовок поста %d не должен быть пустым", postId));
        
        // Проверяем, что ID соответствует ожидаемому
        int actualId = response.jsonPath().getInt("id");
        AssertUtils.assertEquals(postId, actualId, 
            String.format("ID поста должен быть %d", postId));
    }

    @Test
    @TestTag(id = "IMPROVED_API_003", description = "Тест создания поста", category = "API", priority = 2)
    @Story("Create Post")
    @DisplayName("Создать новый пост")
    @Description("Демонстрирует создание поста с валидацией")
    public void testCreatePostImproved() {
        // Создаем тестовый пост
        Post testPost = Post.builder()
                .userId(1)
                .title("Test Post Title")
                .body("This is a test post body content")
                .build();
        
        // Отправляем запрос на создание
        var response = given()
                .contentType("application/json")
                .body(testPost)
                .when()
                .post("/posts")
                .then()
                .extract().response();
        
        // Проверяем результат
        AssertUtils.assertStatusCode(201, response.getStatusCode(), "Статус-код создания должен быть 201");
        AssertUtils.assertTrue(response.getStatusCode() == 201, "Создание поста должно быть успешным");
        
        // Проверяем, что пост был создан с правильными данными
        int createdPostId = response.jsonPath().getInt("id");
        AssertUtils.assertGreaterThan(0, createdPostId, "ID созданного поста должен быть больше 0");
        
        String createdTitle = response.jsonPath().getString("title");
        AssertUtils.assertEquals("Test Post Title", createdTitle, "Заголовок созданного поста должен соответствовать");
        
        String createdBody = response.jsonPath().getString("body");
        AssertUtils.assertEquals("This is a test post body content", createdBody, "Тело созданного поста должно соответствовать");
    }

    @Test
    @TestTag(id = "IMPROVED_API_004", description = "Тест получения всех пользователей", category = "API", priority = 3)
    @Story("Get All Users")
    @DisplayName("Получить всех пользователей")
    @Description("Проверяет получение списка всех пользователей")
    public void testGetAllUsersImproved() {
        var response = given()
                .when()
                .get("/users")
                .then()
                .extract().response();
        
        AssertUtils.assertStatusCode(200, response.getStatusCode(), "Статус-код должен быть 200");
        
        int userCount = response.jsonPath().getList("$").size();
        AssertUtils.assertGreaterThan(0, userCount, "Количество пользователей должно быть больше 0");
        
        // Проверяем структуру первого пользователя
        AssertUtils.assertTrue(response.jsonPath().get("[0].id") != null, "Первый пользователь должен иметь ID");
        AssertUtils.assertTrue(response.jsonPath().get("[0].name") != null, "Первый пользователь должен иметь имя");
        AssertUtils.assertTrue(response.jsonPath().get("[0].email") != null, "Первый пользователь должен иметь email");
        
        // Проверяем, что имя не пустое
        String firstName = response.jsonPath().getString("[0].name");
        AssertUtils.assertNotEmpty(firstName, "Имя первого пользователя не должно быть пустым");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    @TestTag(id = "IMPROVED_API_005", description = "Параметризованный тест пользователей", category = "API", priority = 3)
    @Story("Parameterized Users")
    @DisplayName("Проверить пользователей с различными ID")
    @Description("Тестирует получение пользователей с разными ID")
    public void testGetUsersParameterized(int userId) {
        var response = given()
                .when()
                .get("/users/" + userId)
                .then()
                .extract().response();
        
        AssertUtils.assertStatusCode(200, response.getStatusCode(), 
            String.format("Статус-код для пользователя %d должен быть 200", userId));
        
        String actualName = response.jsonPath().getString("name");
        AssertUtils.assertNotEmpty(actualName, 
            String.format("Имя пользователя %d не должно быть пустым", userId));
        
        // Проверяем, что ID соответствует ожидаемому
        int actualId = response.jsonPath().getInt("id");
        AssertUtils.assertEquals(userId, actualId, 
            String.format("ID пользователя должен быть %d", userId));
    }

    @Test
    @TestTag(id = "IMPROVED_API_006", description = "Тест с retry механизмом", category = "API", priority = 4)
    @Story("Retry Test")
    @DisplayName("Тест с возможностью повторных попыток")
    @Description("Демонстрирует использование RetryExtension для нестабильных тестов")
    @ExtendWith(RetryExtension.class)
    @RetryExtension.RetryOnFailure(maxAttempts = 3, delayMs = 1000)
    public void testWithRetryMechanism() {
        // Этот тест может иногда падать, но будет повторяться до 3 раз
        var response = given()
                .when()
                .get("/posts/1")
                .then()
                .extract().response();
        
        // Имитируем нестабильное поведение (10% шанс падения)
        if (Math.random() < 0.1) {
            throw new RuntimeException("Случайная ошибка для демонстрации retry");
        }
        
        AssertUtils.assertStatusCode(200, response.getStatusCode(), "Статус-код должен быть 200");
    }

    @Test
    @TestTag(id = "IMPROVED_API_007", description = "Тест производительности", category = "API", priority = 5)
    @Story("Performance Test")
    @DisplayName("Тест производительности API")
    @Description("Проверяет время ответа API")
    public void testApiPerformance() {
        long startTime = System.currentTimeMillis();
        
        var response = given()
                .when()
                .get("/posts")
                .then()
                .extract().response();
        
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;
        
        AssertUtils.assertStatusCode(200, response.getStatusCode(), "Статус-код должен быть 200");
        
        // Проверяем, что ответ пришел быстро (менее 5 секунд)
        AssertUtils.assertTrue(responseTime < 5000L, "Время ответа должно быть менее 5 секунд");
        
        // Прикрепляем метрики производительности к отчету
        Allure.addAttachment("Performance Metrics", "text/plain",
            String.format("Response Time: %d ms\nStatus Code: %d\nResponse Size: %d bytes",
                responseTime, response.getStatusCode(), response.getBody().asString().length()));
    }

    // ==================== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ====================

    /**
     * Провайдер данных для тестирования (альтернатива TestDataProvider)
     */
    public static Stream<Arguments> getCustomTestData() {
        return Stream.of(
            Arguments.of(1, "Post 1", "Content 1"),
            Arguments.of(2, "Post 2", "Content 2"),
            Arguments.of(3, "Post 3", "Content 3")
        );
    }
}

