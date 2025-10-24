package api;

import io.qameta.allure.*;
import models.Post;
import models.User;
import models.Comment;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import utils.BaseTest;
import utils.TestTag;
import utils.SchemaValidator;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Расширенные API тесты с позитивными и негативными сценариями
 */
@Epic("API Testing")
@Feature("Extended API Tests")
public class ExtendedApiTest extends BaseTest {

    // ==================== ПОЗИТИВНЫЕ ТЕСТЫ ====================

    @Test
    @TestTag(id = "API_POS_001", description = "Получение поста с валидацией схемы", category = "API", priority = 1)
    @Story("Positive Tests")
    @DisplayName("Получить пост с валидацией JSON схемы")
    @Description("Проверяем получение поста и валидацию структуры данных")
    public void testGetPostWithSchemaValidation() {
        given()
            .when()
                .get("/posts/1")
            .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath(SchemaValidator.getPostSchemaPath()))
                .body("id", equalTo(1))
                .body("userId", equalTo(1))
                .body("title", notNullValue())
                .body("body", notNullValue())
                .body("title", not(emptyString()))
                .body("body", not(emptyString()));
    }

    @Test
    @TestTag(id = "API_POS_002", description = "Получение всех постов", category = "API", priority = 1)
    @Story("Positive Tests")
    @DisplayName("Получить все посты")
    @Description("Проверяем получение списка всех постов")
    public void testGetAllPosts() {
        given()
            .when()
                .get("/posts")
            .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("id", everyItem(notNullValue()))
                .body("userId", everyItem(notNullValue()))
                .body("title", everyItem(notNullValue()))
                .body("body", everyItem(notNullValue()));
    }

    @Test
    @TestTag(id = "API_POS_003", description = "Создание нового поста", category = "API", priority = 2)
    @Story("Positive Tests")
    @DisplayName("Создать новый пост")
    @Description("Проверяем создание нового поста")
    public void testCreatePost() {
        Post newPost = Post.createValidPost();
        
        given()
            .contentType("application/json")
            .body(newPost)
            .when()
                .post("/posts")
            .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("userId", equalTo(newPost.getUserId()))
                .body("title", equalTo(newPost.getTitle()))
                .body("body", equalTo(newPost.getBody()));
    }

    @Test
    @TestTag(id = "API_POS_004", description = "Обновление поста", category = "API", priority = 2)
    @Story("Positive Tests")
    @DisplayName("Обновить пост")
    @Description("Проверяем обновление существующего поста")
    public void testUpdatePost() {
        Post updatedPost = Post.builder()
                .id(1)
                .userId(1)
                .title("Updated Post Title")
                .body("Updated post body content")
                .build();
        
        given()
            .contentType("application/json")
            .body(updatedPost)
            .when()
                .put("/posts/1")
            .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("title", equalTo("Updated Post Title"))
                .body("body", equalTo("Updated post body content"));
    }

    @Test
    @TestTag(id = "API_POS_005", description = "Получение пользователя с валидацией схемы", category = "API", priority = 1)
    @Story("Positive Tests")
    @DisplayName("Получить пользователя с валидацией схемы")
    @Description("Проверяем получение пользователя и валидацию структуры")
    public void testGetUserWithSchemaValidation() {
        given()
            .when()
                .get("/users/1")
            .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath(SchemaValidator.getUserSchemaPath()))
                .body("id", equalTo(1))
                .body("name", notNullValue())
                .body("username", notNullValue())
                .body("email", notNullValue())
                .body("address", notNullValue())
                .body("company", notNullValue());
    }

    @Test
    @TestTag(id = "API_POS_006", description = "Получение комментариев к посту", category = "API", priority = 1)
    @Story("Positive Tests")
    @DisplayName("Получить комментарии к посту")
    @Description("Проверяем получение комментариев для конкретного поста")
    public void testGetPostComments() {
        given()
            .when()
                .get("/posts/1/comments")
            .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("postId", everyItem(equalTo(1)))
                .body("id", everyItem(notNullValue()))
                .body("name", everyItem(notNullValue()))
                .body("email", everyItem(notNullValue()))
                .body("body", everyItem(notNullValue()));
    }

    // ==================== НЕГАТИВНЫЕ ТЕСТЫ ====================

    @Test
    @TestTag(id = "API_NEG_001", description = "Получение несуществующего поста", category = "API", priority = 1)
    @Story("Negative Tests")
    @DisplayName("Получить несуществующий пост (404)")
    @Description("Проверяем обработку ошибки при запросе несуществующего поста")
    public void testGetNonExistentPost() {
        given()
            .when()
                .get("/posts/999999")
            .then()
                .statusCode(404);
    }

    @Test
    @TestTag(id = "API_NEG_002", description = "Создание поста с невалидными данными", category = "API", priority = 2)
    @Story("Negative Tests")
    @DisplayName("Создать пост с невалидными данными")
    @Description("Проверяем обработку невалидных данных при создании поста")
    public void testCreatePostWithInvalidData() {
        Post invalidPost = Post.createInvalidPost();
        
        given()
            .contentType("application/json")
            .body(invalidPost)
            .when()
                .post("/posts")
            .then()
                .statusCode(400);
    }

    @Test
    @TestTag(id = "API_NEG_003", description = "Обновление несуществующего поста", category = "API", priority = 2)
    @Story("Negative Tests")
    @DisplayName("Обновить несуществующий пост")
    @Description("Проверяем обработку ошибки при обновлении несуществующего поста")
    public void testUpdateNonExistentPost() {
        Post post = Post.createValidPost();
        
        given()
            .contentType("application/json")
            .body(post)
            .when()
                .put("/posts/999999")
            .then()
                .statusCode(404);
    }

    @Test
    @TestTag(id = "API_NEG_004", description = "Удаление несуществующего поста", category = "API", priority = 2)
    @Story("Negative Tests")
    @DisplayName("Удалить несуществующий пост")
    @Description("Проверяем обработку ошибки при удалении несуществующего поста")
    public void testDeleteNonExistentPost() {
        given()
            .when()
                .delete("/posts/999999")
            .then()
                .statusCode(404);
    }

    @Test
    @TestTag(id = "API_NEG_005", description = "Запрос с неверным методом", category = "API", priority = 3)
    @Story("Negative Tests")
    @DisplayName("Запрос с неверным HTTP методом")
    @Description("Проверяем обработку неверного HTTP метода")
    public void testInvalidHttpMethod() {
        given()
            .when()
                .patch("/posts/1") // PATCH не поддерживается
            .then()
                .statusCode(405); // Method Not Allowed
    }

    // ==================== ПАРАМЕТРИЗОВАННЫЕ ТЕСТЫ ====================

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    @TestTag(id = "API_PARAM_001", description = "Параметризованный тест постов", category = "API", priority = 2)
    @Story("Parameterized Tests")
    @DisplayName("Получить пост по ID (параметризованный)")
    @Description("Проверяем получение постов с разными ID")
    public void testGetPostById(int postId) {
        given()
            .when()
                .get("/posts/" + postId)
            .then()
                .statusCode(200)
                .body("id", equalTo(postId))
                .body("userId", notNullValue())
                .body("title", notNullValue())
                .body("body", notNullValue());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    @TestTag(id = "API_PARAM_002", description = "Параметризованный тест пользователей", category = "API", priority = 2)
    @Story("Parameterized Tests")
    @DisplayName("Получить пользователя по ID (параметризованный)")
    @Description("Проверяем получение пользователей с разными ID")
    public void testGetUserById(int userId) {
        given()
            .when()
                .get("/users/" + userId)
            .then()
                .statusCode(200)
                .body("id", equalTo(userId))
                .body("name", notNullValue())
                .body("username", notNullValue())
                .body("email", notNullValue());
    }

    // ==================== ТЕСТЫ ПРОИЗВОДИТЕЛЬНОСТИ ====================

    @Test
    @TestTag(id = "API_PERF_001", description = "Тест производительности получения постов", category = "API", priority = 3)
    @Story("Performance Tests")
    @DisplayName("Проверка времени ответа API")
    @Description("Проверяем, что API отвечает в разумное время")
    public void testApiResponseTime() {
        given()
            .when()
                .get("/posts")
            .then()
                .statusCode(200)
                .time(lessThan(2000L)); // Ответ должен быть быстрее 2 секунд
    }

    @Test
    @TestTag(id = "API_PERF_002", description = "Тест производительности получения пользователя", category = "API", priority = 3)
    @Story("Performance Tests")
    @DisplayName("Проверка времени ответа для пользователя")
    @Description("Проверяем время ответа при получении пользователя")
    public void testUserResponseTime() {
        given()
            .when()
                .get("/users/1")
            .then()
                .statusCode(200)
                .time(lessThan(1000L)); // Ответ должен быть быстрее 1 секунды
    }

    // ==================== CONTRACT TESTS ====================

    @Test
    @TestTag(id = "API_CONTRACT_001", description = "Contract test для поста", category = "API", priority = 2)
    @Story("Contract Tests")
    @DisplayName("Проверка контракта API для поста")
    @Description("Проверяем, что API соответствует ожидаемому контракту")
    public void testPostContract() {
        given()
            .when()
                .get("/posts/1")
            .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath(SchemaValidator.getPostSchemaPath()))
                .body("id", instanceOf(Integer.class))
                .body("userId", instanceOf(Integer.class))
                .body("title", instanceOf(String.class))
                .body("body", instanceOf(String.class));
    }

    @Test
    @TestTag(id = "API_CONTRACT_002", description = "Contract test для пользователя", category = "API", priority = 2)
    @Story("Contract Tests")
    @DisplayName("Проверка контракта API для пользователя")
    @Description("Проверяем, что API пользователя соответствует контракту")
    public void testUserContract() {
        given()
            .when()
                .get("/users/1")
            .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath(SchemaValidator.getUserSchemaPath()))
                .body("id", instanceOf(Integer.class))
                .body("name", instanceOf(String.class))
                .body("username", instanceOf(String.class))
                .body("email", instanceOf(String.class));
    }
}