package api;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import utils.*;
import models.Post;
import exceptions.ApiTestException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static io.qameta.allure.Allure.step;

/**
 * Улучшенный стабильный API тест с новой системой обработки ошибок
 */
@Epic("API Testing")
@Feature("Improved Stable API Tests")
@ExtendWith(RetryExtension.class)
public class ImprovedStableApiTest extends BaseApiTest {

    @Test
    @TestTag(id = "STABLE_API_001", description = "Стабильный тест получения поста", category = "API", priority = 1)
    @Story("Stable Get Post")
    @DisplayName("Получить пост с улучшенной обработкой ошибок")
    @Description("Демонстрирует использование новой системы обработки ошибок")
    @RetryExtension.RetryOnFailure(maxAttempts = 3, delayMs = 1000, exponentialBackoff = true)
    public void testGetPostStable() {
        try {
            step("Отправляем GET запрос для получения поста с ID = 1", () -> {
                var response = given()
                        .when()
                        .get("/posts/1")
                        .then()
                        .extract().response();
            });
            
            step("Проверяем статус-код ответа", () -> {
                var response = given()
                        .when()
                        .get("/posts/1")
                        .then()
                        .extract().response();
                
                if (response.getStatusCode() != 200) {
                    throw new ApiTestException("Неожиданный статус-код", response.getStatusCode(), "/posts/1");
                }
            });
            
            step("Проверяем структуру ответа", () -> {
                var response = given()
                        .when()
                        .get("/posts/1")
                        .then()
                        .extract().response();
                
                AssertUtils.assertTrue(response.getStatusCode() == 200, "Ответ должен быть успешным");
                AssertUtils.assertTrue(response.jsonPath().get("id") != null, "Ответ должен содержать поле id");
                AssertUtils.assertTrue(response.jsonPath().get("title") != null, "Ответ должен содержать поле title");
                AssertUtils.assertTrue(response.jsonPath().get("body") != null, "Ответ должен содержать поле body");
            });
            
            step("Проверяем значения полей", () -> {
                var response = given()
                        .when()
                        .get("/posts/1")
                        .then()
                        .extract().response();
                
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
            });
        } catch (Exception e) {
            if (e instanceof ApiTestException) {
                throw e;
            } else {
                throw new ApiTestException("Ошибка при получении поста", e);
            }
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    @TestTag(id = "STABLE_API_002", description = "Стабильный параметризованный тест постов", category = "API", priority = 2)
    @Story("Stable Parameterized Posts")
    @DisplayName("Проверить посты с различными ID (стабильная версия)")
    @Description("Тестирует получение постов с разными ID с улучшенной обработкой ошибок")
    @RetryExtension.RetryOnFailure(maxAttempts = 2, delayMs = 500, exponentialBackoff = false)
    public void testGetPostsParameterizedStable(int postId) {
        try {
            step("Отправляем GET запрос для получения поста с ID = " + postId, () -> {
                var response = given()
                        .when()
                        .get("/posts/" + postId)
                        .then()
                        .extract().response();

                // Проверяем статус-код с улучшенной обработкой ошибок
                if (response.getStatusCode() != 200) {
                    throw new ApiTestException("Неожиданный статус-код для поста " + postId, 
                        response.getStatusCode(), "/posts/" + postId);
                }

                AssertUtils.assertStatusCode(200, response.getStatusCode(),
                    String.format("Статус-код для поста %d должен быть 200", postId));
            });

            step("Проверяем содержимое поста", () -> {
                var response = given()
                        .when()
                        .get("/posts/" + postId)
                        .then()
                        .extract().response();

                String actualTitle = response.jsonPath().getString("title");
                AssertUtils.assertNotEmpty(actualTitle,
                    String.format("Заголовок поста %d не должен быть пустым", postId));
            });

            step("Проверяем корректность ID поста", () -> {
                var response = given()
                        .when()
                        .get("/posts/" + postId)
                        .then()
                        .extract().response();

                // Проверяем, что ID соответствует ожидаемому
                int actualId = response.jsonPath().getInt("id");
                AssertUtils.assertEquals(postId, actualId,
                    String.format("ID поста должен быть %d", postId));
            });

        } catch (Exception e) {
            if (e instanceof ApiTestException) {
                throw e;
            } else {
                throw new ApiTestException("Ошибка при получении поста " + postId, e);
            }
        }
    }

    @Test
    @TestTag(id = "STABLE_API_003", description = "Стабильный тест создания поста", category = "API", priority = 2)
    @Story("Stable Create Post")
    @DisplayName("Создать новый пост (стабильная версия)")
    @Description("Демонстрирует создание поста с валидацией и улучшенной обработкой ошибок")
    @RetryExtension.RetryOnFailure(maxAttempts = 3, delayMs = 1000, exponentialBackoff = true)
    public void testCreatePostStable() {
        try {
            step("Создаем тестовый пост с использованием фабрики", () -> {
                Post testPost = TestDataFactory.createValidPost();
            });

            step("Отправляем POST запрос для создания поста", () -> {
                Post testPost = TestDataFactory.createValidPost();

                var response = given()
                        .contentType("application/json")
                        .body(testPost)
                        .when()
                        .post("/posts")
                        .then()
                        .extract().response();

                // Проверяем результат с улучшенной обработкой ошибок
                if (response.getStatusCode() != 201) {
                    throw new ApiTestException("Неожиданный статус-код при создании поста", 
                        response.getStatusCode(), "/posts");
                }

                AssertUtils.assertStatusCode(201, response.getStatusCode(), "Статус-код создания должен быть 201");
                AssertUtils.assertTrue(response.getStatusCode() == 201, "Создание поста должно быть успешным");
            });

            step("Проверяем данные созданного поста", () -> {
                Post testPost = TestDataFactory.createValidPost();

                var response = given()
                        .contentType("application/json")
                        .body(testPost)
                        .when()
                        .post("/posts")
                        .then()
                        .extract().response();

                // Проверяем, что пост был создан с правильными данными
                int createdPostId = response.jsonPath().getInt("id");
                AssertUtils.assertGreaterThan(0, createdPostId, "ID созданного поста должен быть больше 0");

                String createdTitle = response.jsonPath().getString("title");
                AssertUtils.assertEquals("Test Post Title", createdTitle, "Заголовок созданного поста должен соответствовать");

                String createdBody = response.jsonPath().getString("body");
                AssertUtils.assertEquals("This is a test post body content with some meaningful text.", createdBody, 
                    "Тело созданного поста должно соответствовать");
            });

        } catch (Exception e) {
            if (e instanceof ApiTestException) {
                throw e;
            } else {
                throw new ApiTestException("Ошибка при создании поста", e);
            }
        }
    }

    @Test
    @TestTag(id = "STABLE_API_004", description = "Стабильный тест получения всех пользователей", category = "API", priority = 3)
    @Story("Stable Get All Users")
    @DisplayName("Получить всех пользователей (стабильная версия)")
    @Description("Проверяет получение списка всех пользователей с улучшенной обработкой ошибок")
    @RetryExtension.RetryOnFailure(maxAttempts = 3, delayMs = 1000, exponentialBackoff = true)
    public void testGetAllUsersStable() {
        try {
            step("Отправляем GET запрос для получения всех пользователей", () -> {
                var response = given()
                        .when()
                        .get("/users")
                        .then()
                        .extract().response();

                // Проверяем статус-код с улучшенной обработкой ошибок
                if (response.getStatusCode() != 200) {
                    throw new ApiTestException("Неожиданный статус-код при получении пользователей", 
                        response.getStatusCode(), "/users");
                }

                AssertUtils.assertStatusCode(200, response.getStatusCode(), "Статус-код должен быть 200");
            });

            step("Проверяем количество пользователей", () -> {
                var response = given()
                        .when()
                        .get("/users")
                        .then()
                        .extract().response();

                int userCount = response.jsonPath().getList("$").size();
                AssertUtils.assertGreaterThan(0, userCount, "Количество пользователей должно быть больше 0");
            });

            step("Проверяем структуру данных пользователей", () -> {
                var response = given()
                        .when()
                        .get("/users")
                        .then()
                        .extract().response();

                // Проверяем структуру первого пользователя
                AssertUtils.assertTrue(response.jsonPath().get("[0].id") != null, "Первый пользователь должен иметь ID");
                AssertUtils.assertTrue(response.jsonPath().get("[0].name") != null, "Первый пользователь должен иметь имя");
                AssertUtils.assertTrue(response.jsonPath().get("[0].email") != null, "Первый пользователь должен иметь email");
            });

            step("Проверяем содержимое полей пользователя", () -> {
                var response = given()
                        .when()
                        .get("/users")
                        .then()
                        .extract().response();

                // Проверяем, что имя не пустое
                String firstName = response.jsonPath().getString("[0].name");
                AssertUtils.assertNotEmpty(firstName, "Имя первого пользователя не должно быть пустым");
            });

        } catch (Exception e) {
            if (e instanceof ApiTestException) {
                throw e;
            } else {
                throw new ApiTestException("Ошибка при получении пользователей", e);
            }
        }
    }

    @Test
    @TestTag(id = "STABLE_API_005", description = "Тест обработки ошибок API", category = "API", priority = 4)
    @Story("Error Handling Test")
    @DisplayName("Проверить обработку ошибок API")
    @Description("Демонстрирует правильную обработку ошибок API")
    public void testApiErrorHandling() {
        try {
            step("Пытаемся получить несуществующий пост", () -> {
                var response = given()
                        .when()
                        .get("/posts/99999")
                        .then()
                        .extract().response();

                // Проверяем, что API вернул ошибку 404
                if (response.getStatusCode() != 404) {
                    throw new ApiTestException("Ожидался статус-код 404 для несуществующего поста", 
                        response.getStatusCode(), "/posts/99999");
                }

                AssertUtils.assertStatusCode(404, response.getStatusCode(), 
                    "Статус-код для несуществующего поста должен быть 404");
            });

        } catch (Exception e) {
            if (e instanceof ApiTestException) {
                throw e;
            } else {
                throw new ApiTestException("Ошибка при тестировании обработки ошибок API", e);
            }
        }
    }

    @Test
    @TestTag(id = "STABLE_API_006", description = "Тест с невалидными данными", category = "API", priority = 5)
    @Story("Invalid Data Test")
    @DisplayName("Проверить обработку невалидных данных")
    @Description("Демонстрирует обработку невалидных данных при создании поста")
    public void testInvalidDataHandling() {
        try {
            step("Создаем невалидный пост", () -> {
                Post invalidPost = TestDataFactory.createInvalidPost();
            });

            step("Отправляем POST запрос с невалидными данными", () -> {
                Post invalidPost = TestDataFactory.createInvalidPost();

                var response = given()
                        .contentType("application/json")
                        .body(invalidPost)
                        .when()
                        .post("/posts")
                        .then()
                        .extract().response();

                // JSONPlaceholder API принимает любые данные и возвращает 201
                // В реальном API здесь был бы статус 400 или 422
                AssertUtils.assertStatusCode(201, response.getStatusCode(), 
                    "API должен принять невалидные данные (JSONPlaceholder поведение)");
            });

        } catch (Exception e) {
            if (e instanceof ApiTestException) {
                throw e;
            } else {
                throw new ApiTestException("Ошибка при тестировании невалидных данных", e);
            }
        }
    }
}
