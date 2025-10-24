package utils;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

/**
 * Провайдер тестовых данных для различных типов тестов
 */
public class TestDataProvider {

    // ==================== API ТЕСТОВЫЕ ДАННЫЕ ====================

    /**
     * Данные для тестирования постов
     */
    public static Stream<Arguments> getPostIds() {
        return Stream.of(
            Arguments.of(1, "Post 1"),
            Arguments.of(2, "Post 2"),
            Arguments.of(3, "Post 3"),
            Arguments.of(4, "Post 4"),
            Arguments.of(5, "Post 5")
        );
    }

    /**
     * Данные для тестирования пользователей
     */
    public static Stream<Arguments> getUserIds() {
        return Stream.of(
            Arguments.of(1, "Leanne Graham"),
            Arguments.of(2, "Ervin Howell"),
            Arguments.of(3, "Clementine Bauch"),
            Arguments.of(4, "Patricia Lebsack"),
            Arguments.of(5, "Chelsey Dietrich")
        );
    }

    /**
     * Данные для негативных API тестов
     */
    public static Stream<Arguments> getInvalidPostIds() {
        return Stream.of(
            Arguments.of(999999, 404),
            Arguments.of(-1, 404),
            Arguments.of(0, 404),
            Arguments.of(1000000, 404)
        );
    }

    // ==================== UI ТЕСТОВЫЕ ДАННЫЕ ====================

    /**
     * Данные для тестирования логина на SauceDemo
     */
    public static Stream<Arguments> getSauceDemoLoginData() {
        return Stream.of(
            Arguments.of("standard_user", "secret_sauce", true, "Products"),
            Arguments.of("locked_out_user", "secret_sauce", false, "Login"),
            Arguments.of("problem_user", "secret_sauce", true, "Products"),
            Arguments.of("performance_glitch_user", "secret_sauce", true, "Products"),
            Arguments.of("invalid_user", "invalid_pass", false, "Login")
        );
    }

    /**
     * Данные для тестирования поиска в Google
     */
    public static Stream<Arguments> getGoogleSearchData() {
        return Stream.of(
            Arguments.of("Selenium WebDriver", "Selenium"),
            Arguments.of("Java programming", "Java"),
            Arguments.of("Test automation", "Test"),
            Arguments.of("API testing", "API"),
            Arguments.of("JUnit 5", "JUnit")
        );
    }

    /**
     * Данные для тестирования форм
     */
    public static Stream<Arguments> getFormData() {
        return Stream.of(
            Arguments.of("John", "Doe", "john.doe@example.com", "1234567890", "Valid form data"),
            Arguments.of("Jane", "Smith", "jane.smith@example.com", "0987654321", "Another valid form"),
            Arguments.of("", "Doe", "invalid-email", "", "Invalid form data"),
            Arguments.of("John", "", "john@", "123", "Partial invalid data")
        );
    }

    // ==================== E2E ТЕСТОВЫЕ ДАННЫЕ ====================

    /**
     * Данные для E2E тестов покупки
     */
    public static Stream<Arguments> getPurchaseData() {
        return Stream.of(
            Arguments.of("standard_user", "secret_sauce", "Sauce Labs Backpack", "John", "Doe", "12345"),
            Arguments.of("standard_user", "secret_sauce", "Sauce Labs Bike Light", "Jane", "Smith", "54321"),
            Arguments.of("standard_user", "secret_sauce", "Sauce Labs Bolt T-Shirt", "Bob", "Johnson", "67890")
        );
    }

    /**
     * Данные для тестирования API → UI flow
     */
    public static Stream<Arguments> getApiToUiData() {
        return Stream.of(
            Arguments.of(1, "standard_user", "secret_sauce"),
            Arguments.of(2, "problem_user", "secret_sauce"),
            Arguments.of(3, "performance_glitch_user", "secret_sauce")
        );
    }

    // ==================== ПРОИЗВОДИТЕЛЬНОСТЬ ТЕСТОВЫЕ ДАННЫЕ ====================

    /**
     * Данные для нагрузочного тестирования
     */
    public static Stream<Arguments> getLoadTestData() {
        return Stream.of(
            Arguments.of(10, 1000),  // 10 запросов, 1000ms timeout
            Arguments.of(50, 2000),  // 50 запросов, 2000ms timeout
            Arguments.of(100, 5000)  // 100 запросов, 5000ms timeout
        );
    }

    /**
     * Данные для тестирования таймаутов
     */
    public static Stream<Arguments> getTimeoutData() {
        return Stream.of(
            Arguments.of(1, 5000),   // 1 секунда, 5000ms max wait
            Arguments.of(5, 10000),  // 5 секунд, 10000ms max wait
            Arguments.of(10, 15000)  // 10 секунд, 15000ms max wait
        );
    }

    // ==================== ВАЛИДАЦИЯ ДАННЫХ ====================

    /**
     * Данные для тестирования валидации JSON
     */
    public static Stream<Arguments> getJsonValidationData() {
        return Stream.of(
            Arguments.of("{\"id\":1,\"title\":\"Test\"}", true),
            Arguments.of("{\"id\":\"invalid\",\"title\":\"Test\"}", false),
            Arguments.of("{\"title\":\"Test\"}", false), // missing required field
            Arguments.of("invalid json", false)
        );
    }

    /**
     * Данные для тестирования валидации email
     */
    public static Stream<Arguments> getEmailValidationData() {
        return Stream.of(
            Arguments.of("test@example.com", true),
            Arguments.of("user.name@domain.co.uk", true),
            Arguments.of("invalid-email", false),
            Arguments.of("@domain.com", false),
            Arguments.of("user@", false),
            Arguments.of("", false)
        );
    }

    // ==================== КОНФИГУРАЦИОННЫЕ ДАННЫЕ ====================

    /**
     * Данные для тестирования различных окружений
     */
    public static Stream<Arguments> getEnvironmentData() {
        return Stream.of(
            Arguments.of("test", "https://jsonplaceholder.typicode.com"),
            Arguments.of("staging", "https://staging-api.example.com"),
            Arguments.of("production", "https://api.example.com")
        );
    }

    /**
     * Данные для тестирования различных браузеров
     */
    public static Stream<Arguments> getBrowserData() {
        return Stream.of(
            Arguments.of("chrome", true),
            Arguments.of("firefox", true),
            Arguments.of("edge", true),
            Arguments.of("safari", false) // если не поддерживается
        );
    }

    // ==================== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ====================

    /**
     * Генерировать случайные данные для тестов
     */
    public static String generateRandomEmail() {
        return "test" + System.currentTimeMillis() + "@example.com";
    }

    public static String generateRandomName() {
        return "TestUser" + System.currentTimeMillis();
    }

    public static int generateRandomId() {
        return (int) (Math.random() * 1000) + 1;
    }

    /**
     * Получить данные для конкретного тестового сценария
     */
    public static Stream<Arguments> getDataForScenario(String scenarioName) {
        return switch (scenarioName.toLowerCase()) {
            case "login" -> getSauceDemoLoginData();
            case "search" -> getGoogleSearchData();
            case "purchase" -> getPurchaseData();
            case "api" -> getPostIds();
            case "validation" -> getEmailValidationData();
            default -> Stream.empty();
        };
    }
}


