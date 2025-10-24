package datadriven;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import utils.BaseApiTest;
import utils.TestTag;
import utils.AssertUtils;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Data-driven тесты для API
 * Демонстрирует использование @ParameterizedTest с API данными
 */
@Epic("Data-driven Testing")
@Feature("API Data-driven Tests")
public class ApiDataDrivenTest extends BaseApiTest {

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    @TestTag(id = "API_DATA_001", description = "ValueSource тест пользователей API", category = "Data-driven", priority = 1)
    @Story("API Data-driven")
    @DisplayName("Проверка пользователей API (ValueSource)")
    @Description("Тестируем различных пользователей API с ValueSource")
    public void testApiUsersWithValueSource(int userId) {
        var response = given()
                .when()
                    .get("/users/" + userId)
                .then()
                    .statusCode(200)
                    .extract()
                    .response();
        
        // Проверяем, что API вернул данные пользователя
        AssertUtils.assertStatusCode(200, response.getStatusCode(), "Статус-код должен быть 200");
        AssertUtils.assertNotEmpty(response.asString(), "API должен вернуть данные пользователя");
        
        // Проверяем ID пользователя
        int actualId = response.jsonPath().getInt("id");
        AssertUtils.assertEquals(userId, actualId, "API должен вернуть правильный id");
        
        // Проверяем наличие полей
        AssertUtils.assertTrue(response.jsonPath().get("name") != null, "API должен содержать поле name");
        AssertUtils.assertTrue(response.jsonPath().get("email") != null, "API должен содержать поле email");
    }

    @ParameterizedTest
    @CsvSource({
        "1, Leanne Graham, Bret, Sincere@april.biz",
        "2, Ervin Howell, Antonette, Shanna@melissa.tv",
        "3, Clementine Bauch, Samantha, Nathan@yesenia.net",
        "4, Patricia Lebsack, Karianne, Julianne.OConner@kory.org",
        "5, Chelsey Dietrich, Kamren, Lucio_Hettinger@annie.ca"
    })
    @TestTag(id = "API_DATA_002", description = "CSV тест пользователей API", category = "Data-driven", priority = 1)
    @Story("API Data-driven")
    @DisplayName("Проверка пользователей API (CSV Source)")
    @Description("Тестируем пользователей API с CSV данными")
    public void testApiUsersWithCsvSource(int userId, String expectedName, String expectedUsername, String expectedEmail) {
        var response = given()
                .when()
                    .get("https://jsonplaceholder.typicode.com/users/" + userId)
                .then()
                    .statusCode(200)
                    .extract()
                    .response();
        
        // Проверяем, что API вернул данные пользователя
        AssertUtils.assertStatusCode(200, response.getStatusCode(), "Статус-код должен быть 200");
        AssertUtils.assertNotEmpty(response.asString(), "API должен вернуть данные пользователя");
        
        // Проверяем ID пользователя
        int actualId = response.jsonPath().getInt("id");
        AssertUtils.assertEquals(userId, actualId, "API должен вернуть правильный id");
        
        // Проверяем имя пользователя
        String actualName = response.jsonPath().getString("name");
        AssertUtils.assertEquals(expectedName, actualName, "API должен вернуть правильное имя");
        
        // Проверяем email
        String actualEmail = response.jsonPath().getString("email");
        AssertUtils.assertEquals(expectedEmail, actualEmail, "API должен вернуть правильный email");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    @TestTag(id = "API_DATA_003", description = "ValueSource тест постов API", category = "Data-driven", priority = 1)
    @Story("API Data-driven")
    @DisplayName("Проверка постов API (ValueSource)")
    @Description("Тестируем посты API с ValueSource")
    public void testApiPostsWithValueSource(int postId) {
        var response = given()
                .when()
                    .get("https://jsonplaceholder.typicode.com/posts/" + postId)
                .then()
                    .statusCode(200)
                    .extract()
                    .response();
        
        // Проверяем, что API вернул данные поста
        AssertUtils.assertStatusCode(200, response.getStatusCode(), "Статус-код должен быть 200");
        AssertUtils.assertNotEmpty(response.asString(), "API должен вернуть данные поста");
        
        // Проверяем ID поста
        int actualId = response.jsonPath().getInt("id");
        AssertUtils.assertEquals(postId, actualId, "API должен вернуть правильный id");
        
        // Проверяем наличие полей
        AssertUtils.assertTrue(response.jsonPath().get("title") != null, "API должен содержать поле title");
        AssertUtils.assertTrue(response.jsonPath().get("body") != null, "API должен содержать поле body");
        AssertUtils.assertTrue(response.jsonPath().get("userId") != null, "API должен содержать поле userId");
    }

    @ParameterizedTest
    @CsvSource({
        "1, sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
        "2, qui est esse",
        "3, ea molestias quasi exercitationem repellat qui ipsa sit aut",
        "4, eum et est occaecati",
        "5, nesciunt quas odio"
    })
    @TestTag(id = "API_DATA_004", description = "CSV тест постов API", category = "Data-driven", priority = 2)
    @Story("API Data-driven")
    @DisplayName("Проверка постов API (CSV Source)")
    @Description("Тестируем посты API с CSV данными")
    public void testApiPostsWithCsvSource(int postId, String expectedTitle) {
        var response = given()
                .when()
                    .get("https://jsonplaceholder.typicode.com/posts/" + postId)
                .then()
                    .statusCode(200)
                    .extract()
                    .response();
        
        // Проверяем, что API вернул данные поста
        AssertUtils.assertStatusCode(200, response.getStatusCode(), "Статус-код должен быть 200");
        AssertUtils.assertNotEmpty(response.asString(), "API должен вернуть данные поста");
        
        // Проверяем ID поста
        int actualId = response.jsonPath().getInt("id");
        AssertUtils.assertEquals(postId, actualId, "API должен вернуть правильный id");
        
        // Проверяем заголовок поста
        String actualTitle = response.jsonPath().getString("title");
        AssertUtils.assertEquals(expectedTitle, actualTitle, "API должен вернуть правильный title");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    @TestTag(id = "API_DATA_005", description = "ValueSource тест комментариев API", category = "Data-driven", priority = 2)
    @Story("API Data-driven")
    @DisplayName("Проверка комментариев API (ValueSource)")
    @Description("Тестируем комментарии API с ValueSource")
    public void testApiCommentsWithValueSource(int commentId) {
        var response = given()
                .when()
                    .get("https://jsonplaceholder.typicode.com/comments/" + commentId)
                .then()
                    .statusCode(200)
                    .extract()
                    .response();
        
        // Проверяем, что API вернул данные комментария
        AssertUtils.assertStatusCode(200, response.getStatusCode(), "Статус-код должен быть 200");
        AssertUtils.assertNotEmpty(response.asString(), "API должен вернуть данные комментария");
        
        // Проверяем ID комментария
        int actualId = response.jsonPath().getInt("id");
        AssertUtils.assertEquals(commentId, actualId, "API должен вернуть правильный id");
        
        // Проверяем наличие полей
        AssertUtils.assertTrue(response.jsonPath().get("name") != null, "API должен содержать поле name");
        AssertUtils.assertTrue(response.jsonPath().get("email") != null, "API должен содержать поле email");
        AssertUtils.assertTrue(response.jsonPath().get("body") != null, "API должен содержать поле body");
    }

    @ParameterizedTest
    @CsvSource({
        "1, id labore ex et quam laborum, Eliseo@gardner.biz",
        "2, quo vero reiciendis velit similique earum, Jayne_Kuhic@sydney.com",
        "3, odio adipisci rerum aut animi, Nikita@garfield.biz",
        "4, alias odio sit, Lew@alysha.tv",
        "5, vero eaque aliquid doloribus et culpa, Haylee@gerry.name"
    })
    @TestTag(id = "API_DATA_006", description = "CSV тест комментариев API", category = "Data-driven", priority = 2)
    @Story("API Data-driven")
    @DisplayName("Проверка комментариев API (CSV Source)")
    @Description("Тестируем комментарии API с CSV данными")
    public void testApiCommentsWithCsvSource(int commentId, String expectedName, String expectedEmail) {
        var response = given()
                .when()
                    .get("https://jsonplaceholder.typicode.com/comments/" + commentId)
                .then()
                    .statusCode(200)
                    .extract()
                    .response();
        
        // Проверяем, что API вернул данные комментария
        AssertUtils.assertStatusCode(200, response.getStatusCode(), "Статус-код должен быть 200");
        AssertUtils.assertNotEmpty(response.asString(), "API должен вернуть данные комментария");
        
        // Проверяем ID комментария
        int actualId = response.jsonPath().getInt("id");
        AssertUtils.assertEquals(commentId, actualId, "API должен вернуть правильный id");
        
        // Проверяем имя комментария
        String actualName = response.jsonPath().getString("name");
        AssertUtils.assertEquals(expectedName, actualName, "API должен вернуть правильное имя");
        
        // Проверяем email
        String actualEmail = response.jsonPath().getString("email");
        AssertUtils.assertEquals(expectedEmail, actualEmail, "API должен вернуть правильный email");
    }

    @ParameterizedTest
    @ValueSource(strings = {"posts", "comments", "albums", "photos", "todos"})
    @TestTag(id = "API_DATA_007", description = "ValueSource тест эндпоинтов API", category = "Data-driven", priority = 3)
    @Story("API Data-driven")
    @DisplayName("Проверка эндпоинтов API (ValueSource)")
    @Description("Тестируем различные эндпоинты API с ValueSource")
    public void testApiEndpointsWithValueSource(String endpoint) {
        String response = given()
                .when()
                    .get("https://jsonplaceholder.typicode.com/" + endpoint)
                .then()
                    .statusCode(200)
                    .extract()
                    .response()
                    .asString();
        
        // Проверяем, что API вернул данные
        assertThat("API должен вернуть данные для эндпоинта " + endpoint, response, not(emptyString()));
        assertThat("API должен содержать массив данных", response, containsString("["));
    }

    @ParameterizedTest
    @CsvSource({
        "posts, 100, title, body",
        "comments, 500, name, email",
        "albums, 100, title, userId",
        "photos, 5000, title, url",
        "todos, 200, title, completed"
    })
    @TestTag(id = "API_DATA_008", description = "CSV тест эндпоинтов API", category = "Data-driven", priority = 3)
    @Story("API Data-driven")
    @DisplayName("Проверка эндпоинтов API (CSV Source)")
    @Description("Тестируем эндпоинты API с CSV данными")
    public void testApiEndpointsWithCsvSource(String endpoint, int expectedCount, String field1, String field2) {
        String response = given()
                .when()
                    .get("https://jsonplaceholder.typicode.com/" + endpoint)
                .then()
                    .statusCode(200)
                    .extract()
                    .response()
                    .asString();
        
        // Проверяем, что API вернул данные
        assertThat("API должен вернуть данные для эндпоинта " + endpoint, response, not(emptyString()));
        assertThat("API должен содержать массив данных", response, containsString("["));
        assertThat("API должен содержать поле " + field1, response, containsString(field1));
        assertThat("API должен содержать поле " + field2, response, containsString(field2));
    }
}
