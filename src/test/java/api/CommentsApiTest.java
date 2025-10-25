package api;

import io.qameta.allure.*;
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
import static io.qameta.allure.Allure.step;

/**
 * API тесты для работы с комментариями
 */
@Epic("API Testing")
@Feature("Comments API Tests")
public class CommentsApiTest extends BaseTest {

    // ==================== ПОЗИТИВНЫЕ ТЕСТЫ ====================

    @Test
    @TestTag(id = "COMMENT_POS_001", description = "Получение всех комментариев", category = "API", priority = 1)
    @Story("Comments Positive Tests")
    @DisplayName("Получить все комментарии")
    @Description("Проверяем получение списка всех комментариев")
    public void testGetAllComments() {
        step("Отправляем GET запрос для получения всех комментариев", () -> {
            given()
                .when()
                    .get("/comments")
                .then()
                    .statusCode(200)
                    .body("size()", greaterThan(0))
                    .body("id", everyItem(notNullValue()))
                    .body("postId", everyItem(notNullValue()))
                    .body("name", everyItem(notNullValue()))
                    .body("email", everyItem(notNullValue()))
                    .body("body", everyItem(notNullValue()));
        });
    }

    @Test
    @TestTag(id = "COMMENT_POS_002", description = "Получение комментария по ID", category = "API", priority = 1)
    @Story("Comments Positive Tests")
    @DisplayName("Получить комментарий по ID")
    @Description("Проверяем получение конкретного комментария")
    public void testGetCommentById() {
        step("Отправляем GET запрос для получения комментария с ID = 1", () -> {
            given()
                .when()
                    .get("/comments/1")
                .then()
                    .statusCode(200)
                    .body(matchesJsonSchemaInClasspath(SchemaValidator.getCommentSchemaPath()))
                    .body("id", equalTo(1))
                    .body("postId", notNullValue())
                    .body("name", notNullValue())
                    .body("email", notNullValue())
                    .body("body", notNullValue());
        });
    }

    @Test
    @TestTag(id = "COMMENT_POS_003", description = "Получение комментариев к посту", category = "API", priority = 1)
    @Story("Comments Positive Tests")
    @DisplayName("Получить комментарии к посту")
    @Description("Проверяем получение комментариев для конкретного поста")
    public void testGetCommentsByPostId() {
        step("Отправляем GET запрос для получения комментариев к посту с ID = 1", () -> {
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
        });
    }

    @Test
    @TestTag(id = "COMMENT_POS_004", description = "Создание нового комментария", category = "API", priority = 2)
    @Story("Comments Positive Tests")
    @DisplayName("Создать новый комментарий")
    @Description("Проверяем создание нового комментария")
    public void testCreateComment() {
        step("Создаем новый комментарий", () -> {
            Comment newComment = Comment.createTestComment();
            
            given()
                .contentType("application/json")
                .body(newComment)
                .when()
                    .post("/comments")
                .then()
                    .statusCode(201)
                    .body("id", notNullValue())
                    .body("postId", equalTo(newComment.getPostId()))
                    .body("name", equalTo(newComment.getName()))
                    .body("email", equalTo(newComment.getEmail()))
                    .body("body", equalTo(newComment.getBody()));
        });
    }

    @Test
    @TestTag(id = "COMMENT_POS_005", description = "Обновление комментария", category = "API", priority = 2)
    @Story("Comments Positive Tests")
    @DisplayName("Обновить комментарий")
    @Description("Проверяем обновление существующего комментария")
    public void testUpdateComment() {
        step("Обновляем комментарий", () -> {
            Comment updatedComment = Comment.builder()
                    .id(1)
                    .postId(1)
                    .name("Updated Commenter")
                    .email("updated@example.com")
                    .body("Updated comment body")
                    .build();
            
            given()
                .contentType("application/json")
                .body(updatedComment)
                .when()
                    .put("/comments/1")
                .then()
                    .statusCode(200)
                    .body("id", equalTo(1))
                    .body("name", equalTo("Updated Commenter"))
                    .body("email", equalTo("updated@example.com"))
                    .body("body", equalTo("Updated comment body"));
        });
    }

    // ==================== НЕГАТИВНЫЕ ТЕСТЫ ====================

    @Test
    @TestTag(id = "COMMENT_NEG_001", description = "Получение несуществующего комментария", category = "API", priority = 1)
    @Story("Comments Negative Tests")
    @DisplayName("Получить несуществующий комментарий (404)")
    @Description("Проверяем обработку ошибки при запросе несуществующего комментария")
    public void testGetNonExistentComment() {
        step("Отправляем GET запрос для получения несуществующего комментария", () -> {
            given()
                .when()
                    .get("/comments/999999")
                .then()
                    .statusCode(404);
        });
    }

    @Test
    @TestTag(id = "COMMENT_NEG_002", description = "Создание комментария с невалидными данными", category = "API", priority = 2)
    @Story("Comments Negative Tests")
    @DisplayName("Создать комментарий с невалидными данными")
    @Description("Проверяем поведение JSONPlaceholder API при отправке невалидных данных (API принимает любые данные)")
    public void testCreateCommentWithInvalidData() {
        step("Тестируем создание комментария с невалидными данными", () -> {
            Comment invalidComment = Comment.builder()
                    .postId(null)
                    .name("")
                    .email("invalid-email")
                    .body("")
                    .build();
            
            // JSONPlaceholder - это mock API, которое принимает любые данные
            // и всегда возвращает 201, даже для невалидных данных
            given()
                .contentType("application/json")
                .body(invalidComment)
                .when()
                .post("https://jsonplaceholder.typicode.com/comments")
                .then()
                .statusCode(201)  // JSONPlaceholder всегда возвращает 201 для POST
                .body("id", notNullValue())  // API генерирует ID
                .body("postId", nullValue())  // null значения сохраняются как есть
                .body("name", equalTo(""))    // пустые строки сохраняются
                .body("email", equalTo("invalid-email"))  // невалидный email принимается
                .body("body", equalTo(""));   // пустое тело сохраняется
        });
    }

    @Test
    @TestTag(id = "COMMENT_NEG_003", description = "Обновление несуществующего комментария", category = "API", priority = 2)
    @Story("Comments Negative Tests")
    @DisplayName("Обновить несуществующий комментарий")
    @Description("Проверяем поведение JSONPlaceholder API при обновлении несуществующего комментария")
    public void testUpdateNonExistentComment() {
        step("Подготавливаем данные для обновления", () -> {
            Comment comment = Comment.createTestComment();
        });
        
        step("Отправляем PUT запрос для обновления несуществующего комментария", () -> {
            Comment comment = Comment.createTestComment();
            
            // JSONPlaceholder возвращает 500 при попытке обновить несуществующий ресурс
            given()
                .contentType("application/json")
                .body(comment)
                .when()
                    .put("/comments/999999")
                .then()
                    .statusCode(500);  // JSONPlaceholder возвращает 500 для несуществующих ресурсов
        });
    }

    @Test
    @TestTag(id = "COMMENT_NEG_004", description = "Удаление несуществующего комментария", category = "API", priority = 2)
    @Story("Comments Negative Tests")
    @DisplayName("Удалить несуществующий комментарий")
    @Description("Проверяем поведение JSONPlaceholder API при удалении несуществующего комментария")
    public void testDeleteNonExistentComment() {
        step("Отправляем DELETE запрос для удаления несуществующего комментария", () -> {
            // JSONPlaceholder возвращает 200 даже при удалении несуществующего ресурса
            given()
                .when()
                    .delete("/comments/999999")
                .then()
                    .statusCode(200);  // JSONPlaceholder всегда возвращает 200 для DELETE
        });
    }

    // ==================== ПАРАМЕТРИЗОВАННЫЕ ТЕСТЫ ====================

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    @TestTag(id = "COMMENT_PARAM_001", description = "Параметризованный тест комментариев", category = "API", priority = 2)
    @Story("Comments Parameterized Tests")
    @DisplayName("Получить комментарий по ID (параметризованный)")
    @Description("Проверяем получение комментариев с разными ID")
    public void testGetCommentById(int commentId) {
        step("Отправляем GET запрос для получения комментария с ID = " + commentId, () -> {
            given()
                .when()
                    .get("/comments/" + commentId)
                .then()
                    .statusCode(200)
                    .body("id", equalTo(commentId))
                    .body("postId", notNullValue())
                    .body("name", notNullValue())
                    .body("email", notNullValue())
                    .body("body", notNullValue());
        });
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    @TestTag(id = "COMMENT_PARAM_002", description = "Параметризованный тест комментариев к постам", category = "API", priority = 2)
    @Story("Comments Parameterized Tests")
    @DisplayName("Получить комментарии к посту (параметризованный)")
    @Description("Проверяем получение комментариев для разных постов")
    public void testGetCommentsByPostId(int postId) {
        step("Отправляем GET запрос для получения комментариев к посту с ID = " + postId, () -> {
            given()
                .when()
                    .get("/posts/" + postId + "/comments")
                .then()
                    .statusCode(200)
                    .body("postId", everyItem(equalTo(postId)))
                    .body("id", everyItem(notNullValue()))
                    .body("name", everyItem(notNullValue()))
                    .body("email", everyItem(notNullValue()))
                    .body("body", everyItem(notNullValue()));
        });
    }

    // ==================== ТЕСТЫ ПРОИЗВОДИТЕЛЬНОСТИ ====================

    @Test
    @TestTag(id = "COMMENT_PERF_001", description = "Тест производительности получения комментариев", category = "API", priority = 3)
    @Story("Comments Performance Tests")
    @DisplayName("Проверка времени ответа для комментариев")
    @Description("Проверяем, что API комментариев отвечает в разумное время")
    public void testCommentsResponseTime() {
        step("Отправляем GET запрос для получения всех комментариев и проверяем время ответа", () -> {
            given()
                .when()
                    .get("/comments")
                .then()
                    .statusCode(200)
                    .time(lessThan(2000L)); // Ответ должен быть быстрее 2 секунд
        });
    }

    @Test
    @TestTag(id = "COMMENT_PERF_002", description = "Тест производительности получения комментария", category = "API", priority = 3)
    @Story("Comments Performance Tests")
    @DisplayName("Проверка времени ответа для конкретного комментария")
    @Description("Проверяем время ответа при получении конкретного комментария")
    public void testCommentResponseTime() {
        step("Отправляем GET запрос для получения конкретного комментария и проверяем время ответа", () -> {
            given()
                .when()
                    .get("/comments/1")
                .then()
                    .statusCode(200)
                    .time(lessThan(1000L)); // Ответ должен быть быстрее 1 секунды
        });
    }

    // ==================== CONTRACT TESTS ====================

    @Test
    @TestTag(id = "COMMENT_CONTRACT_001", description = "Contract test для комментария", category = "API", priority = 2)
    @Story("Comments Contract Tests")
    @DisplayName("Проверка контракта API для комментария")
    @Description("Проверяем, что API комментария соответствует ожидаемому контракту")
    public void testCommentContract() {
        step("Отправляем GET запрос для получения комментария и проверяем контракт API", () -> {
            given()
                .when()
                    .get("/comments/1")
                .then()
                    .statusCode(200)
                    .body(matchesJsonSchemaInClasspath(SchemaValidator.getCommentSchemaPath()))
                    .body("id", instanceOf(Integer.class))
                    .body("postId", instanceOf(Integer.class))
                    .body("name", instanceOf(String.class))
                    .body("email", instanceOf(String.class))
                    .body("body", instanceOf(String.class));
        });
    }

    @Test
    @TestTag(id = "COMMENT_CONTRACT_002", description = "Contract test для списка комментариев", category = "API", priority = 2)
    @Story("Comments Contract Tests")
    @DisplayName("Проверка контракта API для списка комментариев")
    @Description("Проверяем, что API списка комментариев соответствует контракту")
    public void testCommentsListContract() {
        step("Отправляем GET запрос для получения списка комментариев и проверяем контракт API", () -> {
            given()
                .when()
                    .get("/comments")
                .then()
                    .statusCode(200)
                    .body("", instanceOf(java.util.List.class))
                    .body("id", everyItem(instanceOf(Integer.class)))
                    .body("postId", everyItem(instanceOf(Integer.class)))
                    .body("name", everyItem(instanceOf(String.class)))
                    .body("email", everyItem(instanceOf(String.class)))
                    .body("body", everyItem(instanceOf(String.class)));
        });
    }
}
