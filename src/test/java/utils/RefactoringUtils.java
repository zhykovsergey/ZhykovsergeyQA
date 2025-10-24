package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Утилиты для рефакторинга дублированного кода
 */
public class RefactoringUtils {
    
    // ==================== ОБЩИЕ ОПЕРАЦИИ ====================
    
    /**
     * Выполняет операцию с повторными попытками
     */
    public static <T> T executeWithRetry(Supplier<T> operation, int maxAttempts, long delayMs) {
        Exception lastException = null;
        
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                return operation.get();
            } catch (Exception e) {
                lastException = e;
                
                if (attempt < maxAttempts) {
                    try {
                        Thread.sleep(delayMs);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Прервано ожидание", ie);
                    }
                }
            }
        }
        
        throw new RuntimeException("Операция не удалась после " + maxAttempts + " попыток", lastException);
    }
    
    /**
     * Выполняет операцию с повторными попытками и логированием
     */
    public static <T> T executeWithRetryAndLogging(Supplier<T> operation, int maxAttempts, long delayMs, String operationName) {
        Exception lastException = null;
        
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                LoggerUtils.logAction("Попытка " + attempt + " из " + maxAttempts, operationName);
                T result = operation.get();
                LoggerUtils.logAction("Операция успешна", operationName);
                return result;
            } catch (Exception e) {
                lastException = e;
                LoggerUtils.logError("Попытка " + attempt + " неудачна", e);
                
                if (attempt < maxAttempts) {
                    try {
                        Thread.sleep(delayMs);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Прервано ожидание", ie);
                    }
                }
            }
        }
        
        throw new RuntimeException("Операция '" + operationName + "' не удалась после " + maxAttempts + " попыток", lastException);
    }
    
    // ==================== UI ОПЕРАЦИИ ====================
    
    /**
     * Безопасное выполнение UI операции
     */
    public static <T> T executeUiOperation(WebDriver driver, Function<WebDriver, T> operation, String operationName) {
        try {
            LoggerUtils.logUiAction("Начало", operationName, "");
            T result = operation.apply(driver);
            LoggerUtils.logUiAction("Завершение", operationName, "Успешно");
            return result;
        } catch (Exception e) {
            LoggerUtils.logError("Ошибка в UI операции: " + operationName, e);
            ScreenshotUtils.attachScreenshot(driver, "Error in " + operationName);
            throw e;
        }
    }
    
    /**
     * Безопасное выполнение UI операции с ожиданием
     */
    public static <T> T executeUiOperationWithWait(WebDriver driver, Function<WebDriver, T> operation, 
                                                   By waitForElement, String operationName) {
        try {
            LoggerUtils.logUiAction("Начало с ожиданием", operationName, waitForElement.toString());
            
            // Ждем элемент
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.elementToBeClickable(waitForElement));
            
            // Выполняем операцию
            T result = operation.apply(driver);
            
            LoggerUtils.logUiAction("Завершение с ожиданием", operationName, "Успешно");
            return result;
        } catch (Exception e) {
            LoggerUtils.logError("Ошибка в UI операции с ожиданием: " + operationName, e);
            ScreenshotUtils.attachScreenshot(driver, "Error in " + operationName);
            throw e;
        }
    }
    
    // ==================== API ОПЕРАЦИИ ====================
    
    /**
     * Безопасное выполнение API операции
     */
    public static <T> T executeApiOperation(Supplier<T> operation, String operationName) {
        long startTime = System.currentTimeMillis();
        
        try {
            LoggerUtils.logAction("Начало API операции", operationName);
            T result = operation.get();
            
            long duration = System.currentTimeMillis() - startTime;
            LoggerUtils.logPerformance("API операция: " + operationName, duration);
            
            return result;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            LoggerUtils.logError("Ошибка в API операции: " + operationName, e);
            LoggerUtils.logPerformance("API операция (с ошибкой): " + operationName, duration);
            throw e;
        }
    }
    
    // ==================== ВАЛИДАЦИЯ ====================
    
    /**
     * Валидация с автоматическим логированием
     */
    public static void validateWithLogging(ValidationResult validationResult, String context) {
        if (!validationResult.isValid()) {
            String errorMessage = "Валидация не пройдена в контексте: " + context + 
                ". Ошибки: " + validationResult.getAllErrorsAsString();
            
            LoggerUtils.logError(errorMessage, new RuntimeException("Validation failed"));
            throw new RuntimeException(errorMessage);
        }
        
        LoggerUtils.logAction("Валидация пройдена", context);
    }
    
    // ==================== ОБРАБОТКА ДАННЫХ ====================
    
    /**
     * Безопасное преобразование данных
     */
    public static <T> T safeTransform(Object input, Function<Object, T> transformer, T defaultValue, String context) {
        try {
            if (input == null) {
                LoggerUtils.logAction("Входные данные null", context);
                return defaultValue;
            }
            
            T result = transformer.apply(input);
            LoggerUtils.logData("Преобразование данных", context + " -> " + result);
            return result;
        } catch (Exception e) {
            LoggerUtils.logError("Ошибка преобразования данных в контексте: " + context, e);
            return defaultValue;
        }
    }
    
    // ==================== ОЧИСТКА РЕСУРСОВ ====================
    
    /**
     * Безопасное закрытие ресурсов
     */
    public static void safeClose(AutoCloseable resource, String resourceName) {
        if (resource != null) {
            try {
                resource.close();
                LoggerUtils.logAction("Ресурс закрыт", resourceName);
            } catch (Exception e) {
                LoggerUtils.logError("Ошибка при закрытии ресурса: " + resourceName, e);
            }
        }
    }
    
    // ==================== УТИЛИТЫ ДЛЯ ТЕСТОВ ====================
    
    /**
     * Выполнение теста с полным логированием
     */
    public static void executeTestWithLogging(String testName, String testDescription, Runnable testLogic) {
        long startTime = System.currentTimeMillis();
        boolean success = false;
        
        try {
            LoggerUtils.logTestStart(null, testName, testDescription);
            testLogic.run();
            success = true;
        } catch (Exception e) {
            LoggerUtils.logError("Тест упал: " + testName, e);
            throw e;
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            LoggerUtils.logTestEnd(null, testName, success, duration);
        }
    }
    
    // ==================== УТИЛИТЫ ДЛЯ КОНФИГУРАЦИИ ====================
    
    /**
     * Безопасное получение конфигурации
     */
    public static String getConfigWithDefault(String key, String defaultValue, String context) {
        try {
            String value = System.getProperty(key);
            if (value == null) {
                value = System.getenv(key);
            }
            
            if (value == null) {
                value = defaultValue;
                LoggerUtils.logConfiguration("Использовано значение по умолчанию", key + " = " + defaultValue);
            } else {
                LoggerUtils.logConfiguration("Конфигурация загружена", key + " = " + value);
            }
            
            return value;
        } catch (Exception e) {
            LoggerUtils.logError("Ошибка получения конфигурации: " + key, e);
            return defaultValue;
        }
    }
    
    // ==================== УТИЛИТЫ ДЛЯ ОЖИДАНИЙ ====================
    
    /**
     * Умное ожидание с логированием
     */
    public static <T> T smartWait(Supplier<T> condition, int maxAttempts, long delayMs, String waitDescription) {
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                T result = condition.get();
                if (result != null) {
                    LoggerUtils.logAction("Ожидание завершено", waitDescription + " (попытка " + attempt + ")");
                    return result;
                }
            } catch (Exception e) {
                LoggerUtils.logError("Ошибка в ожидании: " + waitDescription, e);
            }
            
            if (attempt < maxAttempts) {
                try {
                    Thread.sleep(delayMs);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Прервано ожидание", ie);
                }
            }
        }
        
        throw new RuntimeException("Ожидание не завершено: " + waitDescription);
    }
}
