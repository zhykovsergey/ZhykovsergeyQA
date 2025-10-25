package utils;

import io.qameta.allure.Allure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Утилиты для улучшенного логирования
 */
public class LoggerUtils {
    
    private static final ConcurrentMap<String, Logger> loggers = new ConcurrentHashMap<>();
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    
    // ==================== ПОЛУЧЕНИЕ ЛОГГЕРОВ ====================
    
    /**
     * Получает логгер для класса
     */
    public static Logger getLogger(Class<?> clazz) {
        return loggers.computeIfAbsent(clazz.getName(), LoggerFactory::getLogger);
    }
    
    /**
     * Получает логгер по имени
     */
    public static Logger getLogger(String name) {
        return loggers.computeIfAbsent(name, LoggerFactory::getLogger);
    }
    
    // ==================== ЛОГИРОВАНИЕ С КОНТЕКСТОМ ====================
    
    /**
     * Логирует действие с контекстом
     */
    public static void logAction(Logger logger, String action, String context) {
        String message = String.format("[%s] %s | Context: %s", 
            getCurrentTimestamp(), action, context);
        
        logger.info(message);
        Allure.addAttachment("Action Log", "text/plain", message);
    }
    
    /**
     * Логирует действие с контекстом (без логгера)
     */
    public static void logAction(String action, String context) {
        String message = String.format("[%s] %s | Context: %s", 
            getCurrentTimestamp(), action, context);
        
        System.out.println(message);
        Allure.addAttachment("Action Log", "text/plain", message);
    }
    
    // ==================== ЛОГИРОВАНИЕ ОШИБОК ====================
    
    /**
     * Логирует ошибку с детальной информацией
     */
    public static void logError(Logger logger, String message, Throwable throwable) {
        String errorMessage = String.format("[%s] ERROR: %s", getCurrentTimestamp(), message);
        
        logger.error(errorMessage, throwable);
        
        // Прикрепляем к Allure
        String errorDetails = String.format("%s\nException: %s\nMessage: %s\nStack Trace: %s",
            errorMessage,
            throwable.getClass().getSimpleName(),
            throwable.getMessage(),
            getStackTrace(throwable));
        
        Allure.addAttachment("Error Details", "text/plain", errorDetails);
    }
    
    /**
     * Логирует ошибку с детальной информацией (без логгера)
     */
    public static void logError(String message, Throwable throwable) {
        String errorMessage = String.format("[%s] ERROR: %s", getCurrentTimestamp(), message);
        
        System.err.println(errorMessage);
        throwable.printStackTrace();
        
        // Прикрепляем к Allure
        String errorDetails = String.format("%s\nException: %s\nMessage: %s\nStack Trace: %s",
            errorMessage,
            throwable.getClass().getSimpleName(),
            throwable.getMessage(),
            getStackTrace(throwable));
        
        Allure.addAttachment("Error Details", "text/plain", errorDetails);
    }
    
    // ==================== ЛОГИРОВАНИЕ ПРОИЗВОДИТЕЛЬНОСТИ ====================
    
    /**
     * Логирует метрики производительности
     */
    public static void logPerformance(Logger logger, String operation, long durationMs) {
        String message = String.format("[%s] PERFORMANCE: %s took %d ms", 
            getCurrentTimestamp(), operation, durationMs);
        
        logger.info(message);
        Allure.addAttachment("Performance Metrics", "text/plain", message);
    }
    
    /**
     * Логирует метрики производительности (без логгера)
     */
    public static void logPerformance(String operation, long durationMs) {
        String message = String.format("[%s] PERFORMANCE: %s took %d ms", 
            getCurrentTimestamp(), operation, durationMs);
        
        System.out.println(message);
        Allure.addAttachment("Performance Metrics", "text/plain", message);
    }
    
    // ==================== ЛОГИРОВАНИЕ ДАННЫХ ====================
    
    /**
     * Логирует данные с маскировкой чувствительной информации
     */
    public static void logData(Logger logger, String dataType, String data) {
        String maskedData = maskSensitiveData(data);
        String message = String.format("[%s] DATA: %s = %s", 
            getCurrentTimestamp(), dataType, maskedData);
        
        logger.info(message);
        Allure.addAttachment("Data Log", "text/plain", message);
    }
    
    /**
     * Логирует данные с маскировкой чувствительной информации (без логгера)
     */
    public static void logData(String dataType, String data) {
        String maskedData = maskSensitiveData(data);
        String message = String.format("[%s] DATA: %s = %s", 
            getCurrentTimestamp(), dataType, maskedData);
        
        System.out.println(message);
        Allure.addAttachment("Data Log", "text/plain", message);
    }
    
    // ==================== ЛОГИРОВАНИЕ API ====================
    
    /**
     * Логирует API запрос
     */
    public static void logApiRequest(Logger logger, String method, String url, String body) {
        String message = String.format("[%s] API REQUEST: %s %s", 
            getCurrentTimestamp(), method, url);
        
        logger.info(message);
        
        String requestDetails = String.format("%s\nBody: %s", message, body);
        Allure.addAttachment("API Request", "text/plain", requestDetails);
    }
    
    /**
     * Логирует API ответ
     */
    public static void logApiResponse(Logger logger, int statusCode, String responseBody, long durationMs) {
        String message = String.format("[%s] API RESPONSE: Status %d, Duration %d ms", 
            getCurrentTimestamp(), statusCode, durationMs);
        
        logger.info(message);
        
        String responseDetails = String.format("%s\nResponse Body: %s", message, responseBody);
        Allure.addAttachment("API Response", "text/plain", responseDetails);
    }
    
    // ==================== ЛОГИРОВАНИЕ UI ====================
    
    /**
     * Логирует UI действие
     */
    public static void logUiAction(Logger logger, String action, String element, String value) {
        String message = String.format("[%s] UI ACTION: %s on %s with value '%s'", 
            getCurrentTimestamp(), action, element, value);
        
        logger.info(message);
        Allure.addAttachment("UI Action", "text/plain", message);
    }
    
    /**
     * Логирует UI действие (без логгера)
     */
    public static void logUiAction(String action, String element, String value) {
        String message = String.format("[%s] UI ACTION: %s on %s with value '%s'", 
            getCurrentTimestamp(), action, element, value);
        
        System.out.println(message);
        Allure.addAttachment("UI Action", "text/plain", message);
    }
    
    // ==================== ЛОГИРОВАНИЕ ТЕСТОВ ====================
    
    /**
     * Логирует начало теста
     */
    public static void logTestStart(Logger logger, String testName, String testDescription) {
        String message = String.format("[%s] TEST START: %s - %s", 
            getCurrentTimestamp(), testName, testDescription);
        
        logger.info(message);
        Allure.addAttachment("Test Start", "text/plain", message);
    }
    
    /**
     * Логирует завершение теста
     */
    public static void logTestEnd(Logger logger, String testName, boolean success, long durationMs) {
        String status = success ? "PASSED" : "FAILED";
        String message = String.format("[%s] TEST END: %s - %s (Duration: %d ms)", 
            getCurrentTimestamp(), testName, status, durationMs);
        
        if (success) {
            logger.info(message);
        } else {
            logger.error(message);
        }
        
        Allure.addAttachment("Test End", "text/plain", message);
    }
    
    // ==================== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ====================
    
    /**
     * Получает текущий timestamp
     */
    private static String getCurrentTimestamp() {
        return LocalDateTime.now().format(TIMESTAMP_FORMATTER);
    }
    
    /**
     * Получает stack trace в виде строки
     */
    private static String getStackTrace(Throwable throwable) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : throwable.getStackTrace()) {
            sb.append(element.toString()).append("\n");
        }
        return sb.toString();
    }
    
    /**
     * Маскирует чувствительные данные
     */
    private static String maskSensitiveData(String data) {
        if (data == null) {
            return "null";
        }
        
        // Маскируем пароли
        if (data.toLowerCase().contains("password") || data.toLowerCase().contains("pass")) {
            return "***MASKED***";
        }
        
        // Маскируем токены
        if (data.toLowerCase().contains("token") || data.toLowerCase().contains("key")) {
            return "***MASKED***";
        }
        
        // Маскируем длинные строки
        if (data.length() > 100) {
            return data.substring(0, 50) + "...[TRUNCATED]";
        }
        
        return data;
    }
    
    // ==================== ЛОГИРОВАНИЕ КОНФИГУРАЦИИ ====================
    
    /**
     * Логирует конфигурацию теста
     */
    public static void logConfiguration(Logger logger, String configName, String configValue) {
        String message = String.format("[%s] CONFIG: %s = %s", 
            getCurrentTimestamp(), configName, configValue);
        
        logger.info(message);
        Allure.addAttachment("Configuration", "text/plain", message);
    }
    
    /**
     * Логирует конфигурацию теста (без логгера)
     */
    public static void logConfiguration(String configName, String configValue) {
        String message = String.format("[%s] CONFIG: %s = %s", 
            getCurrentTimestamp(), configName, configValue);
        
        System.out.println(message);
        Allure.addAttachment("Configuration", "text/plain", message);
    }
}

