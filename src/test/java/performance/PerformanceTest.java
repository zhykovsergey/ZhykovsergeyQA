package performance;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import utils.*;
import exceptions.ApiConnectionException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static io.qameta.allure.Allure.step;

/**
 * Тесты производительности API
 */
@Epic("Performance Testing")
@Feature("API Performance Tests")
@ExtendWith(RetryExtension.class)
public class PerformanceTest extends BaseApiTest {

    private static final long MAX_RESPONSE_TIME = 2000; // 2 seconds
    private static final long MAX_RESPONSE_TIME_STRICT = 1000; // 1 second

    @Test
    @TestTag(id = "PERF_001", description = "Тест производительности API постов", category = "Performance", priority = 1)
    @Story("API Performance")
    @DisplayName("Проверить время ответа API постов")
    @Description("Проверяем, что API отвечает в разумное время")
    @Severity(SeverityLevel.CRITICAL)
    public void testApiResponseTime() {
        step("Измеряем время ответа API для получения постов", () -> {
            long startTime = System.currentTimeMillis();
            
            try {
                given()
                    .when()
                    .get("https://jsonplaceholder.typicode.com/posts")
                    .then()
                    .statusCode(200)
                    .body("size()", greaterThan(0));
                    
                long responseTime = System.currentTimeMillis() - startTime;
                
                // Прикрепляем метрики производительности
                Allure.addAttachment("Performance Metrics", "text/plain",
                    String.format("Response Time: %d ms\nMax Allowed: %d ms\nStatus: %s",
                        responseTime, MAX_RESPONSE_TIME, 
                        responseTime <= MAX_RESPONSE_TIME ? "PASS" : "FAIL"));
                
                assertTrue(responseTime <= MAX_RESPONSE_TIME, 
                    String.format("Время ответа %d мс превышает допустимое %d мс", 
                        responseTime, MAX_RESPONSE_TIME));
                        
            } catch (Exception e) {
                throw new ApiConnectionException("Ошибка при измерении производительности", 
                    "https://jsonplaceholder.typicode.com/posts", e);
            }
        });
    }

    @Test
    @TestTag(id = "PERF_002", description = "Тест производительности API пользователей", category = "Performance", priority = 1)
    @Story("API Performance")
    @DisplayName("Проверить время ответа API пользователей")
    @Description("Проверяем производительность API пользователей")
    @Severity(SeverityLevel.CRITICAL)
    public void testUsersApiResponseTime() {
        step("Измеряем время ответа API для получения пользователей", () -> {
            long startTime = System.currentTimeMillis();
            
            try {
                given()
                    .when()
                    .get("https://jsonplaceholder.typicode.com/users")
                    .then()
                    .statusCode(200)
                    .body("size()", greaterThan(0));
                    
                long responseTime = System.currentTimeMillis() - startTime;
                
                // Прикрепляем метрики производительности
                Allure.addAttachment("Performance Metrics", "text/plain",
                    String.format("Response Time: %d ms\nMax Allowed: %d ms\nStatus: %s",
                        responseTime, MAX_RESPONSE_TIME, 
                        responseTime <= MAX_RESPONSE_TIME ? "PASS" : "FAIL"));
                
                assertTrue(responseTime <= MAX_RESPONSE_TIME, 
                    String.format("Время ответа %d мс превышает допустимое %d мс", 
                        responseTime, MAX_RESPONSE_TIME));
                        
            } catch (Exception e) {
                throw new ApiConnectionException("Ошибка при измерении производительности пользователей", 
                    "https://jsonplaceholder.typicode.com/users", e);
            }
        });
    }

    @Test
    @TestTag(id = "PERF_003", description = "Тест производительности конкретного поста", category = "Performance", priority = 2)
    @Story("API Performance")
    @DisplayName("Проверить время ответа API для конкретного поста")
    @Description("Проверяем производительность получения конкретного поста")
    @Severity(SeverityLevel.NORMAL)
    public void testSinglePostResponseTime() {
        step("Измеряем время ответа API для получения поста с ID = 1", () -> {
            long startTime = System.currentTimeMillis();
            
            try {
                given()
                    .when()
                    .get("https://jsonplaceholder.typicode.com/posts/1")
                    .then()
                    .statusCode(200)
                    .body("id", equalTo(1));
                    
                long responseTime = System.currentTimeMillis() - startTime;
                
                // Прикрепляем метрики производительности
                Allure.addAttachment("Performance Metrics", "text/plain",
                    String.format("Response Time: %d ms\nMax Allowed: %d ms\nStatus: %s",
                        responseTime, MAX_RESPONSE_TIME_STRICT, 
                        responseTime <= MAX_RESPONSE_TIME_STRICT ? "PASS" : "FAIL"));
                
                assertTrue(responseTime <= MAX_RESPONSE_TIME_STRICT, 
                    String.format("Время ответа %d мс превышает допустимое %d мс", 
                        responseTime, MAX_RESPONSE_TIME_STRICT));
                        
            } catch (Exception e) {
                throw new ApiConnectionException("Ошибка при измерении производительности поста", 
                    "https://jsonplaceholder.typicode.com/posts/1", e);
            }
        });
    }

    @Test
    @TestTag(id = "PERF_004", description = "Нагрузочный тест API", category = "Performance", priority = 3)
    @Story("Load Testing")
    @DisplayName("Нагрузочный тест API")
    @Description("Проверяем производительность API под нагрузкой")
    @Severity(SeverityLevel.MINOR)
    public void testApiLoadPerformance() {
        step("Выполняем нагрузочный тест API", () -> {
            int numberOfRequests = 10;
            long totalTime = 0;
            int successfulRequests = 0;
            
            for (int i = 0; i < numberOfRequests; i++) {
                long startTime = System.currentTimeMillis();
                
                try {
                    given()
                        .when()
                        .get("https://jsonplaceholder.typicode.com/posts")
                        .then()
                        .statusCode(200);
                        
                    long responseTime = System.currentTimeMillis() - startTime;
                    totalTime += responseTime;
                    successfulRequests++;
                    
                } catch (Exception e) {
                    // Логируем ошибку, но продолжаем тест
                    System.err.println("Request " + (i + 1) + " failed: " + e.getMessage());
                }
            }
            
            double averageResponseTime = (double) totalTime / successfulRequests;
            double successRate = (double) successfulRequests / numberOfRequests * 100;
            
            // Прикрепляем результаты нагрузочного теста
            Allure.addAttachment("Load Test Results", "text/plain",
                String.format("Total Requests: %d\nSuccessful: %d\nSuccess Rate: %.2f%%\nAverage Response Time: %.2f ms\nTotal Time: %d ms",
                    numberOfRequests, successfulRequests, successRate, averageResponseTime, totalTime));
            
            // Проверяем, что успешность запросов больше 90%
            assertTrue(successRate >= 90.0, 
                String.format("Success rate %.2f%% is below 90%%", successRate));
                
            // Проверяем, что среднее время ответа приемлемое
            assertTrue(averageResponseTime <= MAX_RESPONSE_TIME, 
                String.format("Average response time %.2f ms exceeds %d ms", 
                    averageResponseTime, MAX_RESPONSE_TIME));
        });
    }
}