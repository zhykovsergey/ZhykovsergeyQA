package api;

import io.qameta.allure.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import utils.BaseTest;
import utils.TestTag;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * API тесты для ReqRes
 * https://reqres.in/
 * Реалистичные API endpoints с авторизацией
 */
@Epic("ReqRes API Testing")
@Feature("Realistic API Practice Tests")
public class ReqResApiTest extends BaseTest {

    @Test
    @TestTag(id = "REQ001", description = "Получение списка пользователей", category = "API", priority = 1)
    @Story("Users API")
    @DisplayName("Получить список пользователей")
    @Description("Проверяем получение списка пользователей с пагинацией")
    public void testGetUsers() {
        given()
            .when()
                .get("https://reqres.in/api/users?page=2")
            .then()
                .statusCode(200)
                .body("page", equalTo(2))
                .body("per_page", equalTo(6))
                .body("total", equalTo(12))
                .body("total_pages", equalTo(2))
                .body("data", notNullValue())
                .body("data.size()", equalTo(6))
                .body("data[0].id", notNullValue())
                .body("data[0].email", notNullValue())
                .body("data[0].first_name", notNullValue())
                .body("data[0].last_name", notNullValue())
                .body("data[0].avatar", notNullValue());
    }

    @Test
    @TestTag(id = "REQ002", description = "Получение пользователя по ID", category = "API", priority = 1)
    @Story("Users API")
    @DisplayName("Получить пользователя по ID")
    @Description("Проверяем получение конкретного пользователя")
    public void testGetUserById() {
        given()
            .when()
                .get("https://reqres.in/api/users/2")
            .then()
                .statusCode(200)
                .body("data.id", equalTo(2))
                .body("data.email", equalTo("janet.weaver@reqres.in"))
                .body("data.first_name", equalTo("Janet"))
                .body("data.last_name", equalTo("Weaver"))
                .body("data.avatar", equalTo("https://reqres.in/img/faces/2-image.jpg"))
                .body("support", notNullValue());
    }

    @Test
    @TestTag(id = "REQ003", description = "Создание нового пользователя", category = "API", priority = 2)
    @Story("Users API")
    @DisplayName("Создать нового пользователя")
    @Description("Проверяем создание нового пользователя")
    public void testCreateUser() {
        String requestBody = """
            {
                "name": "morpheus",
                "job": "leader"
            }
            """;

        given()
            .body(requestBody)
            .contentType("application/json")
            .when()
                .post("https://reqres.in/api/users")
            .then()
                .statusCode(201)
                .body("name", equalTo("morpheus"))
                .body("job", equalTo("leader"))
                .body("id", notNullValue())
                .body("createdAt", notNullValue());
    }

    @Test
    @TestTag(id = "REQ004", description = "Обновление пользователя", category = "API", priority = 2)
    @Story("Users API")
    @DisplayName("Обновить пользователя")
    @Description("Проверяем обновление существующего пользователя")
    public void testUpdateUser() {
        String requestBody = """
            {
                "name": "morpheus",
                "job": "zion resident"
            }
            """;

        given()
            .body(requestBody)
            .contentType("application/json")
            .when()
                .put("https://reqres.in/api/users/2")
            .then()
                .statusCode(200)
                .body("name", equalTo("morpheus"))
                .body("job", equalTo("zion resident"))
                .body("updatedAt", notNullValue());
    }

    @Test
    @TestTag(id = "REQ005", description = "Удаление пользователя", category = "API", priority = 2)
    @Story("Users API")
    @DisplayName("Удалить пользователя")
    @Description("Проверяем удаление пользователя")
    public void testDeleteUser() {
        given()
            .when()
                .delete("https://reqres.in/api/users/2")
            .then()
                .statusCode(204);
    }

    @Test
    @TestTag(id = "REQ006", description = "Регистрация пользователя", category = "API", priority = 1)
    @Story("Register API")
    @DisplayName("Зарегистрировать пользователя")
    @Description("Проверяем регистрацию нового пользователя")
    public void testRegisterUser() {
        String requestBody = """
            {
                "email": "eve.holt@reqres.in",
                "password": "pistol"
            }
            """;

        given()
            .body(requestBody)
            .contentType("application/json")
            .when()
                .post("https://reqres.in/api/register")
            .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("token", notNullValue());
    }

    @Test
    @TestTag(id = "REQ007", description = "Авторизация пользователя", category = "API", priority = 1)
    @Story("Login API")
    @DisplayName("Авторизовать пользователя")
    @Description("Проверяем авторизацию пользователя")
    public void testLoginUser() {
        String requestBody = """
            {
                "email": "eve.holt@reqres.in",
                "password": "cityslicka"
            }
            """;

        given()
            .body(requestBody)
            .contentType("application/json")
            .when()
                .post("https://reqres.in/api/login")
            .then()
                .statusCode(200)
                .body("token", notNullValue());
    }

    @Test
    @TestTag(id = "REQ008", description = "Неудачная авторизация", category = "API", priority = 2)
    @Story("Login API")
    @DisplayName("Неудачная авторизация")
    @Description("Проверяем обработку ошибки при неверных данных")
    public void testFailedLogin() {
        String requestBody = """
            {
                "email": "peter@klaven"
            }
            """;

        given()
            .body(requestBody)
            .contentType("application/json")
            .when()
                .post("https://reqres.in/api/login")
            .then()
                .statusCode(400)
                .body("error", equalTo("Missing password"));
    }

    @Test
    @TestTag(id = "REQ009", description = "Получение списка ресурсов", category = "API", priority = 1)
    @Story("Resources API")
    @DisplayName("Получить список ресурсов")
    @Description("Проверяем получение списка ресурсов")
    public void testGetResources() {
        given()
            .when()
                .get("https://reqres.in/api/unknown")
            .then()
                .statusCode(200)
                .body("page", equalTo(1))
                .body("per_page", equalTo(6))
                .body("total", equalTo(12))
                .body("total_pages", equalTo(2))
                .body("data", notNullValue())
                .body("data.size()", equalTo(6))
                .body("data[0].id", notNullValue())
                .body("data[0].name", notNullValue())
                .body("data[0].year", notNullValue())
                .body("data[0].color", notNullValue())
                .body("data[0].pantone_value", notNullValue());
    }

    @Test
    @TestTag(id = "REQ010", description = "Получение ресурса по ID", category = "API", priority = 1)
    @Story("Resources API")
    @DisplayName("Получить ресурс по ID")
    @Description("Проверяем получение конкретного ресурса")
    public void testGetResourceById() {
        given()
            .when()
                .get("https://reqres.in/api/unknown/2")
            .then()
                .statusCode(200)
                .body("data.id", equalTo(2))
                .body("data.name", equalTo("fuchsia rose"))
                .body("data.year", equalTo(2001))
                .body("data.color", equalTo("#C74375"))
                .body("data.pantone_value", equalTo("17-2031"))
                .body("support", notNullValue());
    }
}
