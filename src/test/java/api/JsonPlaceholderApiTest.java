package api;

import io.qameta.allure.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import utils.BaseTest;
import utils.TestTag;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static io.qameta.allure.Allure.step;

/**
 * API тесты для JSONPlaceholder
 * https://jsonplaceholder.typicode.com/
 * Идеальный сайт для изучения API тестирования
 */
@Epic("JSONPlaceholder API Testing")
@Feature("REST API Practice Tests")
public class JsonPlaceholderApiTest extends BaseTest {

    @Test
    @TestTag(id = "JSON001", description = "Получение всех постов", category = "API", priority = 1)
    @Story("Posts API")
    @DisplayName("Получить все посты")
    @Description("Проверяем получение списка всех постов")
    public void testGetAllPosts() {
        step("Отправляем GET запрос для получения всех постов", () -> {
            given()
                .when()
                    .get("/posts")
                .then()
                    .statusCode(200)
                    .body("size()", greaterThan(0))
                    .body("[0].id", notNullValue())
                    .body("[0].title", notNullValue())
                    .body("[0].body", notNullValue())
                    .body("[0].userId", notNullValue());
        });
    }

    @Test
    @TestTag(id = "JSON002", description = "Получение поста по ID", category = "API", priority = 1)
    @Story("Posts API")
    @DisplayName("Получить пост по ID")
    @Description("Проверяем получение конкретного поста")
    public void testGetPostById() {
        step("Отправляем GET запрос для получения поста с ID = 1", () -> {
            given()
                .when()
                    .get("/posts/1")
                .then()
                    .statusCode(200)
                    .body("id", equalTo(1))
                    .body("userId", equalTo(1))
                    .body("title", notNullValue())
                    .body("body", notNullValue());
        });
    }

    @Test
    @TestTag(id = "JSON003", description = "Создание нового поста", category = "API", priority = 2)
    @Story("Posts API")
    @DisplayName("Создать новый пост")
    @Description("Проверяем создание нового поста")
    public void testCreatePost() {
        step("Подготавливаем данные для создания нового поста", () -> {
            String requestBody = """
                {
                    "title": "Test Post Title",
                    "body": "This is a test post body",
                    "userId": 1
                }
                """;
        });
        
        step("Отправляем POST запрос для создания нового поста", () -> {
            String requestBody = """
                {
                    "title": "Test Post Title",
                    "body": "This is a test post body",
                    "userId": 1
                }
                """;

            given()
                .body(requestBody)
                .contentType("application/json")
                .when()
                    .post("/posts")
                .then()
                    .statusCode(201)
                    .body("id", notNullValue())
                    .body("title", equalTo("Test Post Title"))
                    .body("body", equalTo("This is a test post body"))
                    .body("userId", equalTo(1));
        });
    }

    @Test
    @TestTag(id = "JSON004", description = "Обновление поста", category = "API", priority = 2)
    @Story("Posts API")
    @DisplayName("Обновить пост")
    @Description("Проверяем обновление существующего поста")
    public void testUpdatePost() {
        String requestBody = """
            {
                "id": 1,
                "title": "Updated Post Title",
                "body": "This is an updated post body",
                "userId": 1
            }
            """;

        given()
            .body(requestBody)
            .contentType("application/json")
            .when()
                .put("/posts/1")
            .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("title", equalTo("Updated Post Title"))
                .body("body", equalTo("This is an updated post body"));
    }

    @Test
    @TestTag(id = "JSON005", description = "Удаление поста", category = "API", priority = 2)
    @Story("Posts API")
    @DisplayName("Удалить пост")
    @Description("Проверяем удаление поста")
    public void testDeletePost() {
        given()
            .when()
                .delete("/posts/1")
            .then()
                .statusCode(200);
    }

    @Test
    @TestTag(id = "JSON006", description = "Получение комментариев", category = "API", priority = 1)
    @Story("Comments API")
    @DisplayName("Получить комментарии")
    @Description("Проверяем получение списка комментариев")
    public void testGetComments() {
        given()
            .when()
                .get("/comments")
            .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("[0].id", notNullValue())
                .body("[0].name", notNullValue())
                .body("[0].email", notNullValue())
                .body("[0].body", notNullValue());
    }

    @Test
    @TestTag(id = "JSON007", description = "Получение пользователей", category = "API", priority = 1)
    @Story("Users API")
    @DisplayName("Получить пользователей")
    @Description("Проверяем получение списка пользователей")
    public void testGetUsers() {
        given()
            .when()
                .get("/users")
            .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("[0].id", notNullValue())
                .body("[0].name", notNullValue())
                .body("[0].email", notNullValue())
                .body("[0].address", notNullValue());
    }

    @Test
    @TestTag(id = "JSON008", description = "Получение альбомов", category = "API", priority = 1)
    @Story("Albums API")
    @DisplayName("Получить альбомы")
    @Description("Проверяем получение списка альбомов")
    public void testGetAlbums() {
        given()
            .when()
                .get("/albums")
            .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("[0].id", notNullValue())
                .body("[0].title", notNullValue())
                .body("[0].userId", notNullValue());
    }

    @Test
    @TestTag(id = "JSON009", description = "Получение фотографий", category = "API", priority = 1)
    @Story("Photos API")
    @DisplayName("Получить фотографии")
    @Description("Проверяем получение списка фотографий")
    public void testGetPhotos() {
        given()
            .when()
                .get("/photos")
            .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("[0].id", notNullValue())
                .body("[0].title", notNullValue())
                .body("[0].url", notNullValue())
                .body("[0].thumbnailUrl", notNullValue());
    }

    @Test
    @TestTag(id = "JSON010", description = "Получение задач", category = "API", priority = 1)
    @Story("Todos API")
    @DisplayName("Получить задачи")
    @Description("Проверяем получение списка задач")
    public void testGetTodos() {
        given()
            .when()
                .get("/todos")
            .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("[0].id", notNullValue())
                .body("[0].title", notNullValue())
                .body("[0].completed", notNullValue())
                .body("[0].userId", notNullValue());
    }
}
