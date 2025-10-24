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
        LoggerUtils.logTestStart(null, "testQualityApiWithValidation", "API тест с валидацией");
        
        try {
            // Валидируем входные данные
            int postId = 1;
            ValidationResult postIdValidation = ValidationUtils.validatePositiveNumber(postId, "Post ID");
            RefactoringUtils.validateWithLogging(postIdValidation, "Валидация Post ID");
            
            // Выполняем API запрос с retry
            var response = RefactoringUtils.executeApiOperation(() -> {
                return given()
                        .when()
                        .get("/posts/" + postId)
                        .then()
                        .extract().response();
            }, "Получение поста " + postId);
            
            // Валидируем ответ
            ValidationResult statusCodeValidation = ValidationUtils.validateNumberRange(
                response.getStatusCode(), "Status Code", 200, 299);
            RefactoringUtils.validateWithLogging(statusCodeValidation, "Валидация статус-кода");
            
            // Валидируем структуру ответа
            String responseBody = response.asString();
            ValidationResult responseBodyValidation = ValidationUtils.validateNotEmpty(responseBody, "Response Body");
            RefactoringUtils.validateWithLogging(responseBodyValidation, "Валидация тела ответа");
            
            // Валидируем поля ответа
            Integer responseId = response.jsonPath().getInt("id");
            String responseTitle = response.jsonPath().getString("title");
            String responseBodyContent = response.jsonPath().getString("body");
            
            ValidationResult idValidation = ValidationUtils.validatePositiveNumber(responseId, "Response ID");
            ValidationResult titleValidation = ValidationUtils.validateNotEmpty(responseTitle, "Response Title");
            ValidationResult bodyValidation = ValidationUtils.validateNotEmpty(responseBodyContent, "Response Body Content");
            
            RefactoringUtils.validateWithLogging(idValidation, "Валидация ID ответа");
            RefactoringUtils.validateWithLogging(titleValidation, "Валидация заголовка ответа");
            RefactoringUtils.validateWithLogging(bodyValidation, "Валидация тела ответа");
            
            // Проверяем соответствие ID
            assertEquals(postId, responseId, "ID в ответе должен соответствовать запрошенному");
            
            // Дополнительные проверки с RestAssured
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
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    @TestTag(id = "QUALITY_API_002", description = "Параметризованный API тест с валидацией", category = "API", priority = 2)
    @Story("Parameterized API Validation")
    @DisplayName("Проверить API с различными ID (с валидацией)")
    @Description("Тестирует получение постов с разными ID с комплексной валидацией")
    @RetryExtension.RetryOnFailure(maxAttempts = 2, delayMs = 500, exponentialBackoff = false)
    public void testParameterizedApiWithValidation(int postId) {
        LoggerUtils.logTestStart(null, "testParameterizedApiWithValidation", "Параметризованный API тест");
        
        try {
            // Валидируем входные данные
            ValidationResult postIdValidation = ValidationUtils.validateNumberRange(postId, "Post ID", 1, 100);
            RefactoringUtils.validateWithLogging(postIdValidation, "Валидация Post ID " + postId);
            
            // Выполняем API запрос
            var response = RefactoringUtils.executeApiOperation(() -> {
                return given()
                        .when()
                        .get("/posts/" + postId)
                        .then()
                        .extract().response();
            }, "Получение поста " + postId);
            
            // Валидируем ответ
            if (response.getStatusCode() != 200) {
                throw new ApiTestException("Неожиданный статус-код для поста " + postId, 
                    response.getStatusCode(), "/posts/" + postId);
            }
            
            // Валидируем структуру ответа
            String responseBody = response.asString();
            ValidationResult responseBodyValidation = ValidationUtils.validateNotEmpty(responseBody, "Response Body");
            RefactoringUtils.validateWithLogging(responseBodyValidation, "Валидация тела ответа для поста " + postId);
            
            // Валидируем поля ответа
            Integer responseId = response.jsonPath().getInt("id");
            String responseTitle = response.jsonPath().getString("title");
            String responseBodyContent = response.jsonPath().getString("body");
            
            ValidationResult idValidation = ValidationUtils.validatePositiveNumber(responseId, "Response ID");
            ValidationResult titleValidation = ValidationUtils.validateLength(responseTitle, "Response Title", 1, 200);
            ValidationResult bodyValidation = ValidationUtils.validateLength(responseBodyContent, "Response Body Content", 1, 1000);
            
            RefactoringUtils.validateWithLogging(idValidation, "Валидация ID ответа для поста " + postId);
            RefactoringUtils.validateWithLogging(titleValidation, "Валидация заголовка ответа для поста " + postId);
            RefactoringUtils.validateWithLogging(bodyValidation, "Валидация тела ответа для поста " + postId);
            
            // Проверяем соответствие ID
            assertEquals(postId, responseId, "ID в ответе должен соответствовать запрошенному");
            
            LoggerUtils.logData("Post Data", "ID: " + responseId + ", Title: " + responseTitle);
            
        } catch (Exception e) {
            LoggerUtils.logError("Ошибка в параметризованном API тесте для поста " + postId, e);
            throw new ApiTestException("Ошибка в параметризованном API тесте для поста " + postId, e);
        }
    }

    @Test
    @TestTag(id = "QUALITY_API_003", description = "API тест создания поста с валидацией", category = "API", priority = 3)
    @Story("Create Post with Validation")
    @DisplayName("Создать пост с комплексной валидацией")
    @Description("Демонстрирует создание поста с валидацией входных и выходных данных")
    @RetryExtension.RetryOnFailure(maxAttempts = 3, delayMs = 1000, exponentialBackoff = true)
    public void testCreatePostWithValidation() {
        LoggerUtils.logTestStart(null, "testCreatePostWithValidation", "API тест создания поста");
        
        try {
            // Создаем тестовый пост с валидацией
            Post testPost = TestDataFactory.createValidPost();
            
            // Валидируем данные поста
            ValidationResult postValidation = ValidationUtils.validatePost(
                testPost.getUserId(),
                testPost.getTitle(),
                testPost.getBody()
            );
            RefactoringUtils.validateWithLogging(postValidation, "Валидация данных поста");
            
            // Выполняем API запрос
            var response = RefactoringUtils.executeApiOperation(() -> {
                return given()
                        .contentType("application/json")
                        .body(testPost)
                        .when()
                        .post("/posts")
                        .then()
                        .extract().response();
            }, "Создание поста");
            
            // Валидируем ответ
            if (response.getStatusCode() != 201) {
                throw new ApiTestException("Неожиданный статус-код при создании поста", 
                    response.getStatusCode(), "/posts");
            }
            
            // Валидируем структуру ответа
            String responseBody = response.asString();
            ValidationResult responseBodyValidation = ValidationUtils.validateNotEmpty(responseBody, "Response Body");
            RefactoringUtils.validateWithLogging(responseBodyValidation, "Валидация тела ответа");
            
            // Валидируем поля ответа
            Integer createdPostId = response.jsonPath().getInt("id");
            String createdTitle = response.jsonPath().getString("title");
            String createdBody = response.jsonPath().getString("body");
            Integer createdUserId = response.jsonPath().getInt("userId");
            
            ValidationResult idValidation = ValidationUtils.validatePositiveNumber(createdPostId, "Created Post ID");
            ValidationResult titleValidation = ValidationUtils.validateNotEmpty(createdTitle, "Created Title");
            ValidationResult bodyValidation = ValidationUtils.validateNotEmpty(createdBody, "Created Body");
            ValidationResult userIdValidation = ValidationUtils.validatePositiveNumber(createdUserId, "Created User ID");
            
            RefactoringUtils.validateWithLogging(idValidation, "Валидация ID созданного поста");
            RefactoringUtils.validateWithLogging(titleValidation, "Валидация заголовка созданного поста");
            RefactoringUtils.validateWithLogging(bodyValidation, "Валидация тела созданного поста");
            RefactoringUtils.validateWithLogging(userIdValidation, "Валидация User ID созданного поста");
            
            // Проверяем соответствие данных
            assertEquals(testPost.getTitle(), createdTitle, "Заголовок созданного поста должен соответствовать");
            assertEquals(testPost.getBody(), createdBody, "Тело созданного поста должно соответствовать");
            assertEquals(testPost.getUserId(), createdUserId, "User ID созданного поста должен соответствовать");
            
            LoggerUtils.logData("Created Post", "ID: " + createdPostId + ", Title: " + createdTitle);
            
        } catch (Exception e) {
            LoggerUtils.logError("Ошибка в API тесте создания поста", e);
            throw new ApiTestException("Ошибка в API тесте создания поста", e);
        }
    }

    @Test
    @TestTag(id = "QUALITY_API_004", description = "API тест с обработкой ошибок", category = "API", priority = 4)
    @Story("API Error Handling")
    @DisplayName("Проверить обработку ошибок API")
    @Description("Демонстрирует правильную обработку ошибок API с валидацией")
    public void testApiErrorHandlingWithValidation() {
        LoggerUtils.logTestStart(null, "testApiErrorHandlingWithValidation", "API тест обработки ошибок");
        
        try {
            // Тестируем получение несуществующего поста
            int nonExistentPostId = 99999;
            
            // Валидируем входные данные
            ValidationResult postIdValidation = ValidationUtils.validatePositiveNumber(nonExistentPostId, "Post ID");
            RefactoringUtils.validateWithLogging(postIdValidation, "Валидация несуществующего Post ID");
            
            // Выполняем API запрос
            var response = RefactoringUtils.executeApiOperation(() -> {
                return given()
                        .when()
                        .get("/posts/" + nonExistentPostId)
                        .then()
                        .extract().response();
            }, "Получение несуществующего поста");
            
            // Валидируем ответ (ожидаем 404)
            ValidationResult statusCodeValidation = ValidationUtils.validateNumberRange(
                response.getStatusCode(), "Status Code", 400, 499);
            RefactoringUtils.validateWithLogging(statusCodeValidation, "Валидация статус-кода ошибки");
            
            // Проверяем, что API вернул ошибку 404
            if (response.getStatusCode() != 404) {
                throw new ApiTestException("Ожидался статус-код 404 для несуществующего поста", 
                    response.getStatusCode(), "/posts/" + nonExistentPostId);
            }
            
            LoggerUtils.logAction("Обработка ошибок API протестирована успешно", "JSONPlaceholder");
            
        } catch (Exception e) {
            LoggerUtils.logError("Ошибка в API тесте обработки ошибок", e);
            throw new ApiTestException("Ошибка в API тесте обработки ошибок", e);
        }
    }

    @Test
    @TestTag(id = "QUALITY_API_005", description = "API тест производительности с валидацией", category = "API", priority = 5)
    @Story("API Performance Testing")
    @DisplayName("Проверить производительность API с валидацией")
    @Description("Демонстрирует измерение производительности API с валидацией результатов")
    public void testApiPerformanceWithValidation() {
        LoggerUtils.logTestStart(null, "testApiPerformanceWithValidation", "API тест производительности");
        
        try {
            // Измеряем время получения всех постов
            long startTime = System.currentTimeMillis();
            
            var response = RefactoringUtils.executeApiOperation(() -> {
                return given()
                        .when()
                        .get("/posts")
                        .then()
                        .extract().response();
            }, "Получение всех постов");
            
            long endTime = System.currentTimeMillis();
            long responseTime = endTime - startTime;
            
            // Валидируем производительность
            ValidationResult performanceValidation = ValidationUtils.validateNumberRange(
                (int) responseTime, "Response Time", 0, 5000);
            RefactoringUtils.validateWithLogging(performanceValidation, "Валидация времени ответа");
            
            // Валидируем ответ
            if (response.getStatusCode() != 200) {
                throw new ApiTestException("Неожиданный статус-код при получении всех постов", 
                    response.getStatusCode(), "/posts");
            }
            
            // Валидируем структуру ответа
            String responseBody = response.asString();
            ValidationResult responseBodyValidation = ValidationUtils.validateNotEmpty(responseBody, "Response Body");
            RefactoringUtils.validateWithLogging(responseBodyValidation, "Валидация тела ответа");
            
            // Валидируем количество постов
            int postCount = response.jsonPath().getList("$").size();
            ValidationResult postCountValidation = ValidationUtils.validatePositiveNumber(postCount, "Post Count");
            RefactoringUtils.validateWithLogging(postCountValidation, "Валидация количества постов");
            
            // Логируем метрики производительности
            LoggerUtils.logPerformance("Получение всех постов", responseTime);
            LoggerUtils.logData("API Performance", "Response Time: " + responseTime + "ms, Post Count: " + postCount);
            
            // Проверяем, что ответ пришел быстро
            assertTrue(responseTime < 5000L, "Время ответа должно быть менее 5 секунд");
            assertTrue(postCount > 0, "Количество постов должно быть больше 0");
            
        } catch (Exception e) {
            LoggerUtils.logError("Ошибка в API тесте производительности", e);
            throw new ApiTestException("Ошибка в API тесте производительности", e);
        }
    }
}
