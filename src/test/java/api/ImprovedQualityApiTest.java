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
import static org.junit.jupiter.api.Assertions.*;
import static io.qameta.allure.Allure.step;

/**
 * Улучшенный API тест с новой системой валидации и логирования
 */
@Epic("API Testing")
@Feature("Improved Quality API Tests")
@ExtendWith(RetryExtension.class)
public class ImprovedQualityApiTest extends BaseApiTest {

    @Test
    @TestTag(id = "QUALITY_API_001", description = "API тест с валидацией и улучшенным логированием", category = "API", priority = 1)
    @Story("Quality API Testing")
    @DisplayName("Проверить API с валидацией данных")
    @Description("Этот тест демонстрирует использование новой системы валидации и логирования для API")
    @RetryExtension.RetryOnFailure(maxAttempts = 3, delayMs = 1000, exponentialBackoff = true)
    public void testQualityApiWithValidation() {
        step("Выполняем API тест с валидацией данных", () -> {
            LoggerUtils.logTestStart(null, "testQualityApiWithValidation", "API тест с валидацией");

            try {
                int postId = 1;
                ValidationResult postIdValidation = ValidationUtils.validatePositiveNumber(postId, "Post ID");
                if (!postIdValidation.isValid()) {
                    throw new ApiTestException("Валидация Post ID не прошла: " + postIdValidation.getErrorMessage());
                }

                var response = given()
                        .when()
                        .get("/posts/" + postId)
                        .then()
                        .extract().response();

                ValidationResult statusCodeValidation = ValidationUtils.validateNumberRange(
                    response.getStatusCode(), "Status Code", 200, 299);
                if (!statusCodeValidation.isValid()) {
                    throw new ApiTestException("Валидация статус-кода не прошла: " + statusCodeValidation.getErrorMessage());
                }

                String responseBody = response.asString();
                ValidationResult responseBodyValidation = ValidationUtils.validateNotEmpty(responseBody, "Response Body");
                if (!responseBodyValidation.isValid()) {
                    throw new ApiTestException("Валидация тела ответа не прошла: " + responseBodyValidation.getErrorMessage());
                }

                Integer responseId = response.jsonPath().getInt("id");
                String responseTitle = response.jsonPath().getString("title");
                String responseBodyContent = response.jsonPath().getString("body");

                ValidationResult idValidation = ValidationUtils.validatePositiveNumber(responseId, "Response ID");
                ValidationResult titleValidation = ValidationUtils.validateNotEmpty(responseTitle, "Response Title");
                ValidationResult bodyValidation = ValidationUtils.validateNotEmpty(responseBodyContent, "Response Body Content");

                if (!idValidation.isValid()) {
                    throw new ApiTestException("Валидация ID ответа не прошла: " + idValidation.getErrorMessage());
                }
                if (!titleValidation.isValid()) {
                    throw new ApiTestException("Валидация заголовка ответа не прошла: " + titleValidation.getErrorMessage());
                }
                if (!bodyValidation.isValid()) {
                    throw new ApiTestException("Валидация тела ответа не прошла: " + bodyValidation.getErrorMessage());
                }

                assertEquals(postId, responseId, "ID в ответе должен соответствовать запрошенному");

                response.then()
                    .body("id", equalTo(postId))
                    .body("userId", notNullValue())
                    .body("title", notNullValue())
                    .body("body", notNullValue());

                LoggerUtils.logAction("API тест с валидацией завершен успешно", "JSONPlaceholder");

            } catch (Exception e) {
                LoggerUtils.logError("Ошибка в API тесте с валидацией", e);
                throw new ApiTestException("Ошибка в API тесте с валидацией", e);
            }
        });
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    @TestTag(id = "QUALITY_API_002", description = "Параметризованный API тест с валидацией", category = "API", priority = 2)
    @Story("Quality API Testing")
    @DisplayName("Параметризованный тест получения поста с валидацией")
    @Description("Этот тест демонстрирует параметризованное получение поста с валидацией")
    public void testQualityApiWithValidationParameterized(int postId) {
        step("Выполняем параметризованный API тест для поста с ID = " + postId, () -> {
            LoggerUtils.logTestStart(null, "testQualityApiWithValidationParameterized", "API тест с валидацией для ID: " + postId);

            try {
                ValidationResult postIdValidation = ValidationUtils.validatePositiveNumber(postId, "Post ID");
                if (!postIdValidation.isValid()) {
                    throw new ApiTestException("Валидация Post ID не прошла: " + postIdValidation.getErrorMessage());
                }

                var response = given()
                        .when()
                        .get("/posts/" + postId)
                        .then()
                        .extract().response();

                ValidationResult statusCodeValidation = ValidationUtils.validateNumberRange(
                    response.getStatusCode(), "Status Code", 200, 299);
                if (!statusCodeValidation.isValid()) {
                    throw new ApiTestException("Валидация статус-кода не прошла: " + statusCodeValidation.getErrorMessage());
                }

                String responseBody = response.asString();
                ValidationResult responseBodyValidation = ValidationUtils.validateNotEmpty(responseBody, "Response Body");
                if (!responseBodyValidation.isValid()) {
                    throw new ApiTestException("Валидация тела ответа не прошла: " + responseBodyValidation.getErrorMessage());
                }

                Integer responseId = response.jsonPath().getInt("id");
                String responseTitle = response.jsonPath().getString("title");
                String responseBodyContent = response.jsonPath().getString("body");

                ValidationResult idValidation = ValidationUtils.validatePositiveNumber(responseId, "Response ID");
                ValidationResult titleValidation = ValidationUtils.validateNotEmpty(responseTitle, "Response Title");
                ValidationResult bodyValidation = ValidationUtils.validateNotEmpty(responseBodyContent, "Response Body Content");

                if (!idValidation.isValid()) {
                    throw new ApiTestException("Валидация ID ответа не прошла: " + idValidation.getErrorMessage());
                }
                if (!titleValidation.isValid()) {
                    throw new ApiTestException("Валидация заголовка ответа не прошла: " + titleValidation.getErrorMessage());
                }
                if (!bodyValidation.isValid()) {
                    throw new ApiTestException("Валидация тела ответа не прошла: " + bodyValidation.getErrorMessage());
                }

                assertEquals(postId, responseId, "ID в ответе должен соответствовать запрошенному");

                response.then()
                    .body("id", equalTo(postId))
                    .body("userId", notNullValue())
                    .body("title", notNullValue())
                    .body("body", notNullValue());

                LoggerUtils.logAction("Параметризованный API тест с валидацией завершен успешно для ID: " + postId, "JSONPlaceholder");

            } catch (Exception e) {
                LoggerUtils.logError("Ошибка в параметризованном API тесте с валидацией для ID: " + postId, e);
                throw new ApiTestException("Ошибка в параметризованном API тесте с валидацией для ID: " + postId, e);
            }
        });
    }

    @Test
    @TestTag(id = "QUALITY_API_003", description = "API тест с негативным сценарием и валидацией", category = "API", priority = 2)
    @Story("Quality API Testing")
    @DisplayName("Проверить API с негативным сценарием (несуществующий пост)")
    @Description("Этот тест демонстрирует обработку негативного сценария (несуществующий пост) с валидацией")
    public void testQualityApiNegativeScenario() {
        step("Выполняем API тест с негативным сценарием", () -> {
            LoggerUtils.logTestStart(null, "testQualityApiNegativeScenario", "API тест с негативным сценарием");

            try {
                int nonExistentPostId = 999999;
                ValidationResult postIdValidation = ValidationUtils.validatePositiveNumber(nonExistentPostId, "Non-existent Post ID");
                if (!postIdValidation.isValid()) {
                    throw new ApiTestException("Валидация несуществующего Post ID не прошла: " + postIdValidation.getErrorMessage());
                }

                var response = given()
                        .when()
                        .get("/posts/" + nonExistentPostId)
                        .then()
                        .extract().response();

                ValidationResult statusCodeValidation = ValidationUtils.validateNumberRange(
                    response.getStatusCode(), "Status Code", 400, 499);
                if (!statusCodeValidation.isValid()) {
                    throw new ApiTestException("Валидация статус-кода для негативного сценария не прошла: " + statusCodeValidation.getErrorMessage());
                }

                assertEquals(404, response.getStatusCode(), "Статус-код должен быть 404 для несуществующего поста");

                LoggerUtils.logAction("API тест с негативным сценарием завершен успешно", "JSONPlaceholder");

            } catch (Exception e) {
                LoggerUtils.logError("Ошибка в API тесте с негативным сценарием", e);
                throw new ApiTestException("Ошибка в API тесте с негативным сценарием", e);
            }
        });
    }
}