package datadriven;

import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@Epic("Data-Driven Testing")
@Feature("Working Data-Driven Tests")
@Story("Parameterized Tests with Real Data")
public class WorkingDataDrivenTest {

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    @DisplayName("Получить посты по ID (ValueSource)")
    @Description("Тест получения постов с разными ID используя ValueSource")
    @Severity(SeverityLevel.NORMAL)
    void testGetPostsById(int postId) {
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

        String title = response.jsonPath().getString("title");
        String body = response.jsonPath().getString("body");
        
        Allure.addAttachment("Post " + postId + " Response", "application/json", response.asString());
        
        // Валидация данных
        assertNotNull(title, "Title should not be null for post " + postId);
        assertNotNull(body, "Body should not be null for post " + postId);
        assertTrue(title.length() > 0, "Title should not be empty for post " + postId);
        assertTrue(body.length() > 0, "Body should not be empty for post " + postId);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    @DisplayName("Получить пользователей по ID (ValueSource)")
    @Description("Тест получения пользователей с разными ID используя ValueSource")
    @Severity(SeverityLevel.NORMAL)
    void testGetUsersById(int userId) {
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

        String name = response.jsonPath().getString("name");
        String email = response.jsonPath().getString("email");
        
        Allure.addAttachment("User " + userId + " Response", "application/json", response.asString());
        
        // Валидация данных
        assertNotNull(name, "Name should not be null for user " + userId);
        assertNotNull(email, "Email should not be null for user " + userId);
        assertTrue(name.length() > 0, "Name should not be empty for user " + userId);
        assertTrue(email.contains("@"), "Email should contain @ for user " + userId);
    }

    @ParameterizedTest
    @CsvSource({
        "1, sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
        "2, qui est esse",
        "3, ea molestias quasi exercitationem repellat qui ipsa sit aut",
        "4, eum et est occaecati",
        "5, nesciunt quas odio"
    })
    @DisplayName("Проверить посты с ожидаемыми заголовками (CsvSource)")
    @Description("Тест проверки постов с ожидаемыми заголовками используя CsvSource")
    @Severity(SeverityLevel.NORMAL)
    void testPostsWithExpectedTitles(int postId, String expectedTitle) {
        Response response = given()
                .when()
                .get("/posts/" + postId)
                .then()
                .statusCode(200)
                .body("id", equalTo(postId))
                .body("title", equalTo(expectedTitle))
                .extract().response();

        String actualTitle = response.jsonPath().getString("title");
        
        Allure.addAttachment("Post " + postId + " Title Check", "text/plain", 
            "Expected: " + expectedTitle + "\nActual: " + actualTitle);
        
        assertEquals(expectedTitle, actualTitle, "Title should match expected value for post " + postId);
    }

    @ParameterizedTest
    @CsvSource({
        "1, Leanne Graham, Sincere@april.biz",
        "2, Ervin Howell, Shanna@melissa.tv",
        "3, Clementine Bauch, Nathan@yesenia.net",
        "4, Patricia Lebsack, Julianne.OConner@kory.org",
        "5, Chelsey Dietrich, Lucio_Hettinger@annie.ca"
    })
    @DisplayName("Проверить пользователей с ожидаемыми данными (CsvSource)")
    @Description("Тест проверки пользователей с ожидаемыми именами и email используя CsvSource")
    @Severity(SeverityLevel.NORMAL)
    void testUsersWithExpectedData(int userId, String expectedName, String expectedEmail) {
        Response response = given()
                .when()
                .get("/users/" + userId)
                .then()
                .statusCode(200)
                .body("id", equalTo(userId))
                .body("name", equalTo(expectedName))
                .body("email", equalTo(expectedEmail))
                .extract().response();

        String actualName = response.jsonPath().getString("name");
        String actualEmail = response.jsonPath().getString("email");
        
        Allure.addAttachment("User " + userId + " Data Check", "text/plain", 
            "Expected Name: " + expectedName + "\nActual Name: " + actualName + 
            "\nExpected Email: " + expectedEmail + "\nActual Email: " + actualEmail);
        
        assertEquals(expectedName, actualName, "Name should match expected value for user " + userId);
        assertEquals(expectedEmail, actualEmail, "Email should match expected value for user " + userId);
    }

    @Test
    @DisplayName("Получить все посты и проверить структуру")
    @Description("Тест получения всех постов и проверки их структуры")
    @Severity(SeverityLevel.CRITICAL)
    void testGetAllPostsStructure() {
        Response response = given()
                .when()
                .get("/posts")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .extract().response();

        int postCount = response.jsonPath().getList("").size();
        
        Allure.addAttachment("All Posts Response", "application/json", response.asString());
        Allure.addAttachment("Post Count", "text/plain", "Total posts: " + postCount);
        
        // Проверяем, что у нас есть посты
        assertTrue(postCount > 0, "Should have at least one post");
        
        // Проверяем структуру первого поста
        String firstPostTitle = response.jsonPath().getString("[0].title");
        String firstPostBody = response.jsonPath().getString("[0].body");
        Integer firstPostUserId = response.jsonPath().getInt("[0].userId");
        
        assertNotNull(firstPostTitle, "First post title should not be null");
        assertNotNull(firstPostBody, "First post body should not be null");
        assertNotNull(firstPostUserId, "First post userId should not be null");
        assertTrue(firstPostUserId > 0, "First post userId should be positive");
    }

    @Test
    @DisplayName("Получить всех пользователей и проверить структуру")
    @Description("Тест получения всех пользователей и проверки их структуры")
    @Severity(SeverityLevel.CRITICAL)
    void testGetAllUsersStructure() {
        Response response = given()
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .extract().response();

        int userCount = response.jsonPath().getList("").size();
        
        Allure.addAttachment("All Users Response", "application/json", response.asString());
        Allure.addAttachment("User Count", "text/plain", "Total users: " + userCount);
        
        // Проверяем, что у нас есть пользователи
        assertTrue(userCount > 0, "Should have at least one user");
        
        // Проверяем структуру первого пользователя
        String firstName = response.jsonPath().getString("[0].name");
        String firstEmail = response.jsonPath().getString("[0].email");
        String firstPhone = response.jsonPath().getString("[0].phone");
        
        assertNotNull(firstName, "First user name should not be null");
        assertNotNull(firstEmail, "First user email should not be null");
        assertNotNull(firstPhone, "First user phone should not be null");
        assertTrue(firstEmail.contains("@"), "First user email should contain @");
    }
}
