package api;

import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static io.qameta.allure.Allure.step;

@Epic("API Testing")
@Feature("Working API Tests")
@Story("Stable API Tests with Real Endpoints")
public class WorkingApiTest {

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    @DisplayName("Получить все посты")
    @Description("Тест получения всех постов с проверкой статуса и структуры")
    @Severity(SeverityLevel.CRITICAL)
    void testGetAllPosts() {
        step("Отправляем GET запрос для получения всех постов", () -> {
            Response response = given()
                    .when()
                    .get("/posts")
                    .then()
                    .statusCode(200)
                    .body("size()", greaterThan(0))
                    .body("[0].id", notNullValue())
                    .body("[0].title", notNullValue())
                    .body("[0].body", notNullValue())
                    .body("[0].userId", notNullValue())
                    .extract().response();

            Allure.addAttachment("Response", "application/json", response.asString());
        });
    }

    @Test
    @DisplayName("Получить пост по ID")
    @Description("Тест получения конкретного поста по ID")
    @Severity(SeverityLevel.CRITICAL)
    void testGetPostById() {
        step("Подготавливаем ID поста для запроса", () -> {
            int postId = 1;
        });
        
        step("Отправляем GET запрос для получения поста по ID", () -> {
            int postId = 1;
            
            Response response = given()
                    .when()
                    .get("/posts/" + postId)
                    .then()
                    .statusCode(200)
                    .body("id", equalTo(postId))
                    .body("title", notNullValue())
                    .body("body", notNullValue())
                    .body("userId", notNullValue())
                    .extract().response();

            Allure.addAttachment("Response", "application/json", response.asString());
        });
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    @DisplayName("Получить посты по ID (параметризованный)")
    @Description("Тест получения постов с разными ID")
    @Severity(SeverityLevel.NORMAL)
    void testGetPostsById(int postId) {
        step("Отправляем GET запрос для получения поста с ID = " + postId, () -> {
            Response response = given()
                    .when()
                    .get("/posts/" + postId)
                    .then()
                    .statusCode(200)
                    .body("id", equalTo(postId))
                    .extract().response();

            Allure.addAttachment("Response for ID " + postId, "application/json", response.asString());
        });
    }

    @Test
    @DisplayName("Получить всех пользователей")
    @Description("Тест получения всех пользователей")
    @Severity(SeverityLevel.CRITICAL)
    void testGetAllUsers() {
        step("Отправляем GET запрос для получения всех пользователей", () -> {
            Response response = given()
                    .when()
                    .get("/users")
                    .then()
                    .statusCode(200)
                    .body("size()", greaterThan(0))
                    .body("[0].id", notNullValue())
                    .body("[0].name", notNullValue())
                    .body("[0].email", notNullValue())
                    .extract().response();

            Allure.addAttachment("Users Response", "application/json", response.asString());
        });
    }

    @Test
    @DisplayName("Получить пользователя по ID")
    @Description("Тест получения конкретного пользователя по ID")
    @Severity(SeverityLevel.NORMAL)
    void testGetUserById() {
        step("Подготавливаем ID пользователя для запроса", () -> {
            int userId = 1;
        });
        
        step("Отправляем GET запрос для получения пользователя по ID", () -> {
            int userId = 1;
            
            Response response = given()
                    .when()
                    .get("/users/" + userId)
                    .then()
                    .statusCode(200)
                    .body("id", equalTo(userId))
                    .body("name", notNullValue())
                    .body("email", notNullValue())
                    .body("address", notNullValue())
                    .extract().response();

            Allure.addAttachment("User Response", "application/json", response.asString());
        });
    }

    @Test
    @DisplayName("Создать новый пост")
    @Description("Тест создания нового поста")
    @Severity(SeverityLevel.CRITICAL)
    void testCreatePost() {
        step("Подготавливаем данные для создания поста", () -> {
            String postData = """
                    {
                        "title": "Test Post",
                        "body": "This is a test post",
                        "userId": 1
                    }
                    """;
        });
        
        step("Отправляем POST запрос для создания нового поста", () -> {
            String postData = """
                    {
                        "title": "Test Post",
                        "body": "This is a test post",
                        "userId": 1
                    }
                    """;

            Response response = given()
                    .contentType("application/json")
                    .body(postData)
                    .when()
                    .post("/posts")
                    .then()
                    .statusCode(201)
                    .body("id", notNullValue())
                    .body("title", equalTo("Test Post"))
                    .body("body", equalTo("This is a test post"))
                    .body("userId", equalTo(1))
                    .extract().response();

            Allure.addAttachment("Created Post", "application/json", response.asString());
        });
    }

    @Test
    @DisplayName("Обновить пост")
    @Description("Тест обновления существующего поста")
    @Severity(SeverityLevel.NORMAL)
    void testUpdatePost() {
        step("Подготавливаем данные для обновления поста", () -> {
            int postId = 1;
            String updateData = """
                    {
                        "id": 1,
                        "title": "Updated Post",
                        "body": "This post has been updated",
                        "userId": 1
                    }
                    """;
        });
        
        step("Отправляем PUT запрос для обновления поста", () -> {
            int postId = 1;
            String updateData = """
                    {
                        "id": 1,
                        "title": "Updated Post",
                        "body": "This post has been updated",
                        "userId": 1
                    }
                    """;

            Response response = given()
                    .contentType("application/json")
                    .body(updateData)
                    .when()
                    .put("/posts/" + postId)
                    .then()
                    .statusCode(200)
                    .body("id", equalTo(postId))
                    .body("title", equalTo("Updated Post"))
                    .body("body", equalTo("This post has been updated"))
                    .extract().response();

            Allure.addAttachment("Updated Post", "application/json", response.asString());
        });
    }

    @Test
    @DisplayName("Удалить пост")
    @Description("Тест удаления поста")
    @Severity(SeverityLevel.NORMAL)
    void testDeletePost() {
        step("Подготавливаем ID поста для удаления", () -> {
            int postId = 1;
        });
        
        step("Отправляем DELETE запрос для удаления поста", () -> {
            int postId = 1;
            
            given()
                    .when()
                    .delete("/posts/" + postId)
                    .then()
                    .statusCode(200);
        });
    }

    @Test
    @DisplayName("Получить комментарии к посту")
    @Description("Тест получения комментариев к конкретному посту")
    @Severity(SeverityLevel.NORMAL)
    void testGetPostComments() {
        step("Подготавливаем ID поста для получения комментариев", () -> {
            int postId = 1;
        });
        
        step("Отправляем GET запрос для получения комментариев к посту", () -> {
            int postId = 1;
            
            Response response = given()
                    .when()
                    .get("/posts/" + postId + "/comments")
                    .then()
                    .statusCode(200)
                    .body("size()", greaterThan(0))
                    .body("[0].postId", equalTo(postId))
                    .body("[0].id", notNullValue())
                    .body("[0].name", notNullValue())
                    .body("[0].email", notNullValue())
                    .body("[0].body", notNullValue())
                    .extract().response();

            Allure.addAttachment("Comments Response", "application/json", response.asString());
        });
    }

    @Test
    @DisplayName("Проверка производительности API")
    @Description("Тест проверки времени ответа API")
    @Severity(SeverityLevel.MINOR)
    void testApiPerformance() {
        step("Запускаем измерение времени ответа", () -> {
            long startTime = System.currentTimeMillis();
        });
        
        step("Отправляем GET запрос для проверки производительности", () -> {
            given()
                    .when()
                    .get("/posts")
                    .then()
                    .statusCode(200);
        });
        
        step("Проверяем время ответа и прикрепляем метрики", () -> {
            long startTime = System.currentTimeMillis();
            
            given()
                    .when()
                    .get("/posts")
                    .then()
                    .statusCode(200);
            
            long endTime = System.currentTimeMillis();
            long responseTime = endTime - startTime;
            
            Allure.addAttachment("Response Time", "text/plain", "Response time: " + responseTime + "ms");
            
            // Проверяем, что время ответа меньше 5 секунд
            assertTrue(responseTime < 5000, "Response time should be less than 5 seconds");
        });
    }
}
