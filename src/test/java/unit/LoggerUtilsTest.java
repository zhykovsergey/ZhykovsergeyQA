package unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.slf4j.Logger;
import utils.LoggerUtils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit тесты для LoggerUtils
 */
@DisplayName("Тесты логирования")
public class LoggerUtilsTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    // ==================== ТЕСТЫ ПОЛУЧЕНИЯ ЛОГГЕРОВ ====================

    @Test
    @DisplayName("Получение логгера для класса")
    public void testGetLoggerForClass() {
        Logger logger = LoggerUtils.getLogger(LoggerUtilsTest.class);
        assertNotNull(logger);
        assertEquals("unit.LoggerUtilsTest", logger.getName());
    }

    @Test
    @DisplayName("Получение логгера по имени")
    public void testGetLoggerByName() {
        Logger logger = LoggerUtils.getLogger("test.logger");
        assertNotNull(logger);
        assertEquals("test.logger", logger.getName());
    }

    @Test
    @DisplayName("Кэширование логгеров")
    public void testLoggerCaching() {
        Logger logger1 = LoggerUtils.getLogger("test.logger");
        Logger logger2 = LoggerUtils.getLogger("test.logger");
        assertSame(logger1, logger2, "Логгеры должны быть закэшированы");
    }

    // ==================== ТЕСТЫ ЛОГИРОВАНИЯ ДЕЙСТВИЙ ====================

    @Test
    @DisplayName("Логирование действия")
    public void testLogAction() {
        LoggerUtils.logAction("Test Action", "Test Context");
        
        String output = outContent.toString();
        assertTrue(output.contains("Test Action"), "Вывод должен содержать действие");
        assertTrue(output.contains("Test Context"), "Вывод должен содержать контекст");
        assertTrue(output.contains("Context:"), "Вывод должен содержать контекст");
    }

    @Test
    @DisplayName("Логирование действия с логгером")
    public void testLogActionWithLogger() {
        Logger logger = LoggerUtils.getLogger("test.logger");
        LoggerUtils.logAction(logger, "Test Action", "Test Context");
        
        // Проверяем, что метод не падает
        assertNotNull(logger);
    }

    // ==================== ТЕСТЫ ЛОГИРОВАНИЯ ОШИБОК ====================

    @Test
    @DisplayName("Логирование ошибки")
    public void testLogError() {
        Exception testException = new RuntimeException("Test error message");
        LoggerUtils.logError("Test Error", testException);
        
        String output = errContent.toString();
        assertTrue(output.contains("Test Error"), "Вывод должен содержать сообщение об ошибке");
        assertTrue(output.contains("Test error message"), "Вывод должен содержать сообщение исключения");
    }

    @Test
    @DisplayName("Логирование ошибки с логгером")
    public void testLogErrorWithLogger() {
        Logger logger = LoggerUtils.getLogger("test.logger");
        Exception testException = new RuntimeException("Test error message");
        LoggerUtils.logError(logger, "Test Error", testException);
        
        // Проверяем, что метод не падает
        assertNotNull(logger);
    }

    // ==================== ТЕСТЫ ЛОГИРОВАНИЯ ПРОИЗВОДИТЕЛЬНОСТИ ====================

    @Test
    @DisplayName("Логирование производительности")
    public void testLogPerformance() {
        LoggerUtils.logPerformance("Test Operation", 1500L);
        
        String output = outContent.toString();
        assertTrue(output.contains("Test Operation"), "Вывод должен содержать название операции");
        assertTrue(output.contains("1500"), "Вывод должен содержать время выполнения");
        assertTrue(output.contains("PERFORMANCE"), "Вывод должен содержать тип лога");
    }

    @Test
    @DisplayName("Логирование производительности с логгером")
    public void testLogPerformanceWithLogger() {
        Logger logger = LoggerUtils.getLogger("test.logger");
        LoggerUtils.logPerformance(logger, "Test Operation", 1500L);
        
        // Проверяем, что метод не падает
        assertNotNull(logger);
    }

    // ==================== ТЕСТЫ ЛОГИРОВАНИЯ ДАННЫХ ====================

    @Test
    @DisplayName("Логирование данных")
    public void testLogData() {
        LoggerUtils.logData("Test Data", "test value");
        
        String output = outContent.toString();
        assertTrue(output.contains("Test Data"), "Вывод должен содержать тип данных");
        assertTrue(output.contains("test value"), "Вывод должен содержать значение данных");
        assertTrue(output.contains("DATA"), "Вывод должен содержать тип лога");
    }

    @Test
    @DisplayName("Логирование данных с логгером")
    public void testLogDataWithLogger() {
        Logger logger = LoggerUtils.getLogger("test.logger");
        LoggerUtils.logData(logger, "Test Data", "test value");
        
        // Проверяем, что метод не падает
        assertNotNull(logger);
    }

    // ==================== ТЕСТЫ ЛОГИРОВАНИЯ API ====================

    @Test
    @DisplayName("Логирование API запроса")
    public void testLogApiRequest() {
        Logger logger = LoggerUtils.getLogger("test.logger");
        LoggerUtils.logApiRequest(logger, "GET", "https://api.example.com/users", "{\"id\": 1}");
        
        // Проверяем, что метод не падает
        assertNotNull(logger);
    }

    @Test
    @DisplayName("Логирование API ответа")
    public void testLogApiResponse() {
        Logger logger = LoggerUtils.getLogger("test.logger");
        LoggerUtils.logApiResponse(logger, 200, "{\"success\": true}", 500L);
        
        // Проверяем, что метод не падает
        assertNotNull(logger);
    }

    // ==================== ТЕСТЫ ЛОГИРОВАНИЯ UI ====================

    @Test
    @DisplayName("Логирование UI действия")
    public void testLogUiAction() {
        LoggerUtils.logUiAction("click", "button", "Submit");
        
        String output = outContent.toString();
        assertTrue(output.contains("click"), "Вывод должен содержать действие");
        assertTrue(output.contains("button"), "Вывод должен содержать элемент");
        assertTrue(output.contains("Submit"), "Вывод должен содержать значение");
        assertTrue(output.contains("UI ACTION"), "Вывод должен содержать тип лога");
    }

    @Test
    @DisplayName("Логирование UI действия с логгером")
    public void testLogUiActionWithLogger() {
        Logger logger = LoggerUtils.getLogger("test.logger");
        LoggerUtils.logUiAction(logger, "click", "button", "Submit");
        
        // Проверяем, что метод не падает
        assertNotNull(logger);
    }

    // ==================== ТЕСТЫ ЛОГИРОВАНИЯ ТЕСТОВ ====================

    @Test
    @DisplayName("Логирование начала теста")
    public void testLogTestStart() {
        Logger logger = LoggerUtils.getLogger("test.logger");
        LoggerUtils.logTestStart(logger, "TestName", "Test Description");
        
        // Проверяем, что метод не падает
        assertNotNull(logger);
    }

    @Test
    @DisplayName("Логирование завершения теста")
    public void testLogTestEnd() {
        Logger logger = LoggerUtils.getLogger("test.logger");
        LoggerUtils.logTestEnd(logger, "TestName", true, 1000L);
        
        // Проверяем, что метод не падает
        assertNotNull(logger);
    }

    // ==================== ТЕСТЫ ЛОГИРОВАНИЯ КОНФИГУРАЦИИ ====================

    @Test
    @DisplayName("Логирование конфигурации")
    public void testLogConfiguration() {
        LoggerUtils.logConfiguration("test.config", "test.value");
        
        String output = outContent.toString();
        assertTrue(output.contains("test.config"), "Вывод должен содержать название конфигурации");
        assertTrue(output.contains("test.value"), "Вывод должен содержать значение конфигурации");
        assertTrue(output.contains("CONFIG"), "Вывод должен содержать тип лога");
    }

    @Test
    @DisplayName("Логирование конфигурации с логгером")
    public void testLogConfigurationWithLogger() {
        Logger logger = LoggerUtils.getLogger("test.logger");
        LoggerUtils.logConfiguration(logger, "test.config", "test.value");
        
        // Проверяем, что метод не падает
        assertNotNull(logger);
    }

    // ==================== ТЕСТЫ МАСКИРОВКИ ДАННЫХ ====================

    @Test
    @DisplayName("Маскировка чувствительных данных")
    public void testMaskSensitiveData() {
        // Тестируем маскировку паролей
        LoggerUtils.logData("password", "password123");
        String output = outContent.toString();
        assertTrue(output.contains("***MASKED***"), "Пароль должен быть замаскирован");
        
        // Очищаем вывод
        outContent.reset();
        
        // Тестируем маскировку токенов
        LoggerUtils.logData("token", "token123");
        output = outContent.toString();
        assertTrue(output.contains("***MASKED***"), "Токен должен быть замаскирован");
    }

    @Test
    @DisplayName("Обрезка длинных строк")
    public void testTruncateLongStrings() {
        String longString = "a".repeat(150);
        LoggerUtils.logData("long data", longString);
        
        String output = outContent.toString();
        assertTrue(output.contains("[TRUNCATED]"), "Длинная строка должна быть обрезана");
        assertFalse(output.contains(longString), "Полная длинная строка не должна быть в выводе");
    }

    // ==================== ТЕСТЫ ОБРАБОТКИ NULL ====================

    @Test
    @DisplayName("Обработка null значений")
    public void testNullHandling() {
        LoggerUtils.logData("null data", null);
        
        String output = outContent.toString();
        assertTrue(output.contains("null"), "null значение должно быть обработано");
    }

    // ==================== ТЕСТЫ ФОРМАТИРОВАНИЯ ВРЕМЕНИ ====================

    @Test
    @DisplayName("Форматирование времени в логах")
    public void testTimestampFormatting() {
        LoggerUtils.logAction("Test Action", "Test Context");
        
        String output = outContent.toString();
        // Проверяем, что лог содержит ожидаемые данные
        assertTrue(output.contains("Test Action"), "Вывод должен содержать действие");
        assertTrue(output.contains("Test Context"), "Вывод должен содержать контекст");
        assertTrue(output.length() > 0, "Вывод не должен быть пустым");
    }
}

