package api;

import io.qameta.allure.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import utils.BaseApiTest;
import utils.TestTag;
import utils.AssertUtils;

import static io.qameta.allure.Allure.step;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Простой API тест с улучшенными ассертами
 */
@Epic("API Testing")
@Feature("Basic API Tests")
public class SimpleApiTest extends BaseApiTest {

    @Test
    @TestTag(id = "API001", description = "Получение поста по ID", category = "API", priority = 1)
    @Story("Get Post")
    @DisplayName("Получить пост по ID")
    @Description("Проверяем получение поста с ID = 1")
    public void testGetPost() {
        step("Отправляем GET запрос для получения поста с ID = 1", () -> {
            var response = given()
                .when()
                    .get("https://jsonplaceholder.typicode.com/posts/1")
                .then()
                    .statusCode(200)
                    .body("id", equalTo(1))
                    .body("userId", equalTo(1))
                    .body("title", notNullValue())
                    .body("body", notNullValue())
                    .extract().response();
            
            // Дополнительные проверки с AssertUtils
            AssertUtils.assertStatusCode(200, response.getStatusCode(), "Статус-код должен быть 200");
            AssertUtils.assertNotNull(response.jsonPath().getString("title"), "Заголовок поста не должен быть null");
            AssertUtils.assertNotNull(response.jsonPath().getString("body"), "Тело поста не должно быть null");
        });
    }

    @Test
    @TestTag(id = "API002", description = "Получение списка пользователей", category = "API", priority = 2)
    @Story("Get Users")
    @DisplayName("Получить список пользователей")
    @Description("Проверяем получение списка пользователей")
    public void testGetUsers() {
        step("Отправляем GET запрос для получения списка пользователей", () -> {
            var response = given()
                .when()
                    .get("https://jsonplaceholder.typicode.com/users")
                .then()
                    .statusCode(200)
                    .body("size()", greaterThan(0))
                    .body("[0].id", notNullValue())
                    .body("[0].name", notNullValue())
                    .extract().response();
            
            // Дополнительные проверки с AssertUtils
            AssertUtils.assertStatusCode(200, response.getStatusCode(), "Статус-код должен быть 200");
            AssertUtils.assertGreaterThan(0, response.jsonPath().getList("").size(), "Список пользователей не должен быть пустым");
            AssertUtils.assertNotNull(response.jsonPath().getString("[0].name"), "Имя первого пользователя не должно быть null");
        });
    }
}
