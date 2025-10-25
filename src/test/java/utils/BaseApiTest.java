package utils;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

/**
 * Базовый класс для всех API тестов
 * Содержит общую настройку RestAssured и логирование
 */
public abstract class BaseApiTest {

    private long testStartTime;

    @BeforeAll
    @Step("Настройка API конфигурации")
    static void setupApi() {
        // Настройка базового URL
        RestAssured.baseURI = Config.getBaseUrl();
        
        // Добавление фильтров для Allure
        RestAssured.filters(
            new AllureRestAssured(),
            new RequestLoggingFilter(),
            new ResponseLoggingFilter()
        );
        
        // Настройка таймаутов
        RestAssured.config = RestAssured.config()
            .httpClient(RestAssured.config().getHttpClientConfig()
                .setParam("http.connection.timeout", Config.getConnectionTimeout())
                .setParam("http.socket.timeout", Config.getSocketTimeout()));
        
        // Прикрепляем API конфигурацию к Allure
        String apiConfig = String.format(
            "Base URL: %s\nConnection Timeout: %d ms\nSocket Timeout: %d ms\nRetry Count: %d\nEnvironment: %s",
            Config.getBaseUrl(),
            Config.getConnectionTimeout(),
            Config.getSocketTimeout(),
            Config.getRetryCount(),
            Config.getEnvironment()
        );
        Allure.addAttachment("API Configuration", "text/plain", apiConfig);
    }

    @BeforeEach
    @Step("Подготовка к тесту")
    void setupTest() {
        testStartTime = System.currentTimeMillis();
        
        // Сброс состояния между тестами
        RestAssured.reset();
        
        // Прикрепляем метрики к каждому тесту
        Allure.addAttachment("Memory Metrics", "text/plain", 
            "Free Memory: " + Runtime.getRuntime().freeMemory() + " bytes\n" +
            "Total Memory: " + Runtime.getRuntime().totalMemory() + " bytes\n" +
            "Max Memory: " + Runtime.getRuntime().maxMemory() + " bytes");
        Allure.addAttachment("Test Timestamp", "text/plain", 
            "Test started at: " + java.time.LocalDateTime.now());
        
        // Прикрепляем информацию о тесте
        String testInfo = String.format(
            "Test Class: %s\nTest Method: %s\nStart Time: %s\nEnvironment: %s",
            this.getClass().getSimpleName(),
            "Current Test Method",
            java.time.LocalDateTime.now().toString(),
            Config.getEnvironment()
        );
        Allure.addAttachment("Test Information", "text/plain", testInfo);
    }

    /**
     * Вспомогательный метод для логирования API запроса
     */
    @Step("API Request: {method} {endpoint}")
    protected void logApiRequest(String method, String endpoint) {
        String requestInfo = String.format(
            "Method: %s\nEndpoint: %s\nFull URL: %s%s\nTimestamp: %s",
            method,
            endpoint,
            RestAssured.baseURI,
            endpoint,
            java.time.LocalDateTime.now().toString()
        );
        Allure.addAttachment("API Request", "text/plain", requestInfo);
    }

    /**
     * Вспомогательный метод для логирования API ответа
     */
    @Step("API Response: {statusCode}")
    protected void logApiResponse(int statusCode, String responseBody) {
        String responseInfo = String.format(
            "Status Code: %d\nResponse Body: %s\nTimestamp: %s",
            statusCode,
            responseBody,
            java.time.LocalDateTime.now().toString()
        );
        Allure.addAttachment("API Response", "text/plain", responseInfo);
        
        // Прикрепляем JSON ответ отдельно для лучшей читаемости
        if (responseBody != null && responseBody.trim().startsWith("{")) {
            Allure.addAttachment("JSON Response", "application/json", responseBody);
        }
    }

    /**
     * Вспомогательный метод для прикрепления метрик времени выполнения
     */
    protected void attachExecutionMetrics() {
        long testEndTime = System.currentTimeMillis();
        long executionTime = testEndTime - testStartTime;
        Allure.addAttachment("Execution Time", "text/plain", 
            "Test execution time: " + executionTime + " ms");
        Allure.addAttachment("Memory Metrics", "text/plain", 
            "Free Memory: " + Runtime.getRuntime().freeMemory() + " bytes\n" +
            "Total Memory: " + Runtime.getRuntime().totalMemory() + " bytes\n" +
            "Max Memory: " + Runtime.getRuntime().maxMemory() + " bytes");
    }

    /**
     * Вспомогательный метод для прикрепления ошибок API
     */
    protected void attachApiError(Throwable error, String endpoint) {
        String errorInfo = String.format(
            "API Error Details:\nEndpoint: %s\nError: %s\nMessage: %s\nTimestamp: %s",
            endpoint,
            error.getClass().getSimpleName(),
            error.getMessage(),
            java.time.LocalDateTime.now().toString()
        );
        Allure.addAttachment("API Error", "text/plain", errorInfo);
    }
}


