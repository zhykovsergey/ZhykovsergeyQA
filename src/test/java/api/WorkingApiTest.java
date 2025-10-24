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
    }

    @Test
    @DisplayName("Получить пост по ID")
    @Description("Тест получения конкретного поста по ID")
    @Severity(SeverityLevel.CRITICAL)
    void testGetPostById() {
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
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    @DisplayName("Получить посты по ID (параметризованный)")
    @Description("Тест получения постов с разными ID")
    @Severity(SeverityLevel.NORMAL)
    void testGetPostsById(int postId) {
        Response response = given()
                .when()
                .get("/posts/" + postId)
                .then()
                .statusCode(200)
                .body("id", equalTo(postId))
                .extract().response();

        Allure.addAttachment("Response for ID " + postId, "application/json", response.asString());
    }

    @Test
    @DisplayName("Получить всех пользователей")
    @Description("Тест получения всех пользователей")
    @Severity(SeverityLevel.CRITICAL)
    void testGetAllUsers() {
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
    }

    @Test
    @DisplayName("Получить пользователя по ID")
    @Description("Тест получения конкретного пользователя по ID")
    @Severity(SeverityLevel.NORMAL)
    void testGetUserById() {
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
    }

    @Test
    @DisplayName("Создать новый пост")
    @Description("Тест создания нового поста")
    @Severity(SeverityLevel.CRITICAL)
    void testCreatePost() {
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
    }

    @Test
    @DisplayName("Обновить пост")
    @Description("Тест обновления существующего поста")
    @Severity(SeverityLevel.NORMAL)
    void testUpdatePost() {
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
    }

    @Test
    @DisplayName("Удалить пост")
    @Description("Тест удаления поста")
    @Severity(SeverityLevel.NORMAL)
    void testDeletePost() {
        int postId = 1;
        
        given()
                .when()
                .delete("/posts/" + postId)
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Получить комментарии к посту")
    @Description("Тест получения комментариев к конкретному посту")
    @Severity(SeverityLevel.NORMAL)
    void testGetPostComments() {
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
    }

    @Test
    @DisplayName("Проверка производительности API")
    @Description("Тест проверки времени ответа API")
    @Severity(SeverityLevel.MINOR)
    void testApiPerformance() {
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
    }
}
