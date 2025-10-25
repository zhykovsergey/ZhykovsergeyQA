package unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import utils.ValidationUtils;
import utils.ValidationResult;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit тесты для ValidationUtils
 */
@DisplayName("Тесты валидации")
public class ValidationUtilsTest {

    // ==================== ТЕСТЫ EMAIL ВАЛИДАЦИИ ====================

    @Test
    @DisplayName("Валидация корректного email")
    public void testValidEmail() {
        assertTrue(ValidationUtils.isValidEmail("test@example.com"));
        assertTrue(ValidationUtils.isValidEmail("user.name@domain.co.uk"));
        assertTrue(ValidationUtils.isValidEmail("user+tag@example.org"));
    }

    @Test
    @DisplayName("Валидация некорректного email")
    public void testInvalidEmail() {
        assertFalse(ValidationUtils.isValidEmail("invalid-email"));
        assertFalse(ValidationUtils.isValidEmail("@example.com"));
        assertFalse(ValidationUtils.isValidEmail("test@"));
        assertFalse(ValidationUtils.isValidEmail(""));
        assertFalse(ValidationUtils.isValidEmail(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "test@example.com",
        "user.name@domain.co.uk",
        "user+tag@example.org",
        "test123@test-domain.com"
    })
    @DisplayName("Параметризованные тесты валидных email")
    public void testValidEmailsParameterized(String email) {
        ValidationResult result = ValidationUtils.validateEmail(email);
        assertTrue(result.isValid(), "Email должен быть валидным: " + email);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "invalid-email",
        "@example.com",
        "test@",
        ""
    })
    @DisplayName("Параметризованные тесты невалидных email")
    public void testInvalidEmailsParameterized(String email) {
        ValidationResult result = ValidationUtils.validateEmail(email);
        assertFalse(result.isValid(), "Email должен быть невалидным: " + email);
        assertTrue(result.hasErrors(), "Должны быть ошибки для: " + email);
    }

    // ==================== ТЕСТЫ ТЕЛЕФОН ВАЛИДАЦИИ ====================

    @Test
    @DisplayName("Валидация корректного телефона")
    public void testValidPhone() {
        assertTrue(ValidationUtils.isValidPhone("123-456-7890"));
        assertTrue(ValidationUtils.isValidPhone("(123) 456-7890"));
        assertTrue(ValidationUtils.isValidPhone("+1234567890"));
    }

    @Test
    @DisplayName("Валидация некорректного телефона")
    public void testInvalidPhone() {
        assertFalse(ValidationUtils.isValidPhone("invalid-phone"));
        assertFalse(ValidationUtils.isValidPhone("abc"));
        assertFalse(ValidationUtils.isValidPhone(""));
        assertFalse(ValidationUtils.isValidPhone(null));
    }

    // ==================== ТЕСТЫ URL ВАЛИДАЦИИ ====================

    @Test
    @DisplayName("Валидация корректного URL")
    public void testValidUrl() {
        assertTrue(ValidationUtils.isValidUrl("https://example.com"));
        assertTrue(ValidationUtils.isValidUrl("http://test.org"));
        assertTrue(ValidationUtils.isValidUrl("ftp://files.example.com"));
    }

    @Test
    @DisplayName("Валидация некорректного URL")
    public void testInvalidUrl() {
        assertFalse(ValidationUtils.isValidUrl("invalid-url"));
        assertFalse(ValidationUtils.isValidUrl("example.com"));
        assertFalse(ValidationUtils.isValidUrl(""));
        assertFalse(ValidationUtils.isValidUrl(null));
    }

    // ==================== ТЕСТЫ USERNAME ВАЛИДАЦИИ ====================

    @Test
    @DisplayName("Валидация корректного username")
    public void testValidUsername() {
        assertTrue(ValidationUtils.isValidUsername("testuser"));
        assertTrue(ValidationUtils.isValidUsername("user123"));
        assertTrue(ValidationUtils.isValidUsername("user_name"));
        assertTrue(ValidationUtils.isValidUsername("user-name"));
    }

    @Test
    @DisplayName("Валидация некорректного username")
    public void testInvalidUsername() {
        assertFalse(ValidationUtils.isValidUsername("ab")); // слишком короткий
        assertFalse(ValidationUtils.isValidUsername("a".repeat(21))); // слишком длинный
        assertFalse(ValidationUtils.isValidUsername("user@name")); // недопустимые символы
        assertFalse(ValidationUtils.isValidUsername(""));
        assertFalse(ValidationUtils.isValidUsername(null));
    }

    // ==================== ТЕСТЫ СТРОК ВАЛИДАЦИИ ====================

    @Test
    @DisplayName("Валидация непустой строки")
    public void testValidateNotEmpty() {
        ValidationResult result = ValidationUtils.validateNotEmpty("test", "Test Field");
        assertTrue(result.isValid());
        
        result = ValidationUtils.validateNotEmpty("", "Test Field");
        assertFalse(result.isValid());
        assertTrue(result.getErrorMessage().contains("не может быть пустым"));
        
        result = ValidationUtils.validateNotEmpty(null, "Test Field");
        assertFalse(result.isValid());
        assertTrue(result.getErrorMessage().contains("не может быть null"));
    }

    @Test
    @DisplayName("Валидация длины строки")
    public void testValidateLength() {
        ValidationResult result = ValidationUtils.validateLength("test", "Test Field", 2, 10);
        assertTrue(result.isValid());
        
        result = ValidationUtils.validateLength("a", "Test Field", 2, 10);
        assertFalse(result.isValid());
        assertTrue(result.getErrorMessage().contains("минимум 2"));
        
        result = ValidationUtils.validateLength("a".repeat(11), "Test Field", 2, 10);
        assertFalse(result.isValid());
        assertTrue(result.getErrorMessage().contains("максимум 10 символов"));
    }

    // ==================== ТЕСТЫ ЧИСЕЛ ВАЛИДАЦИИ ====================

    @Test
    @DisplayName("Валидация положительного числа")
    public void testValidatePositiveNumber() {
        ValidationResult result = ValidationUtils.validatePositiveNumber(5, "Test Number");
        assertTrue(result.isValid());
        
        result = ValidationUtils.validatePositiveNumber(0, "Test Number");
        assertFalse(result.isValid());
        assertTrue(result.getErrorMessage().contains("положительным числом"));
        
        result = ValidationUtils.validatePositiveNumber(-1, "Test Number");
        assertFalse(result.isValid());
        assertTrue(result.getErrorMessage().contains("положительным числом"));
    }

    @Test
    @DisplayName("Валидация числа в диапазоне")
    public void testValidateNumberRange() {
        ValidationResult result = ValidationUtils.validateNumberRange(5, "Test Number", 1, 10);
        assertTrue(result.isValid());
        
        result = ValidationUtils.validateNumberRange(0, "Test Number", 1, 10);
        assertFalse(result.isValid());
        assertTrue(result.getErrorMessage().contains("диапазоне от 1 до 10"));
        
        result = ValidationUtils.validateNumberRange(11, "Test Number", 1, 10);
        assertFalse(result.isValid());
        assertTrue(result.getErrorMessage().contains("диапазоне от 1 до 10"));
    }

    // ==================== ТЕСТЫ МАССИВОВ ВАЛИДАЦИИ ====================

    @Test
    @DisplayName("Валидация непустого массива")
    public void testValidateNotEmptyArray() {
        String[] validArray = {"item1", "item2"};
        ValidationResult result = ValidationUtils.validateNotEmptyArray(validArray, "Test Array");
        assertTrue(result.isValid());
        
        String[] emptyArray = {};
        result = ValidationUtils.validateNotEmptyArray(emptyArray, "Test Array");
        assertFalse(result.isValid());
        assertTrue(result.getErrorMessage().contains("не может быть пустым"));
        
        result = ValidationUtils.validateNotEmptyArray(null, "Test Array");
        assertFalse(result.isValid());
        assertTrue(result.getErrorMessage().contains("не может быть null"));
    }

    // ==================== ТЕСТЫ КОМПЛЕКСНОЙ ВАЛИДАЦИИ ====================

    @Test
    @DisplayName("Валидация пользователя - валидные данные")
    public void testValidateUserValid() {
        ValidationResult result = ValidationUtils.validateUser(
            "John Doe",
            "johndoe",
            "john@example.com",
            "123-456-7890",
            "https://example.com"
        );
        
        assertTrue(result.isValid(), "Пользователь должен быть валидным");
    }

    @Test
    @DisplayName("Валидация пользователя - невалидные данные")
    public void testValidateUserInvalid() {
        ValidationResult result = ValidationUtils.validateUser(
            "", // невалидное имя
            "ab", // невалидный username
            "invalid-email", // невалидный email
            "invalid-phone", // невалидный телефон
            "invalid-url" // невалидный URL
        );
        
        assertFalse(result.isValid(), "Пользователь должен быть невалидным");
        assertTrue(result.getErrorCount() > 1, "Должно быть несколько ошибок");
    }

    @Test
    @DisplayName("Валидация поста - валидные данные")
    public void testValidatePostValid() {
        ValidationResult result = ValidationUtils.validatePost(
            1,
            "Test Post Title",
            "This is a test post body"
        );
        
        assertTrue(result.isValid(), "Пост должен быть валидным");
    }

    @Test
    @DisplayName("Валидация поста - невалидные данные")
    public void testValidatePostInvalid() {
        ValidationResult result = ValidationUtils.validatePost(
            -1, // невалидный userId
            "", // невалидный заголовок
            "" // невалидное тело
        );
        
        assertFalse(result.isValid(), "Пост должен быть невалидным");
        assertTrue(result.getErrorCount() > 1, "Должно быть несколько ошибок");
    }

    // ==================== ТЕСТЫ ValidationResult ====================

    @Test
    @DisplayName("ValidationResult - успешный результат")
    public void testValidationResultSuccess() {
        ValidationResult result = ValidationResult.success();
        assertTrue(result.isValid());
        assertFalse(result.hasErrors());
        assertEquals(0, result.getErrorCount());
    }

    @Test
    @DisplayName("ValidationResult - результат с ошибкой")
    public void testValidationResultError() {
        ValidationResult result = ValidationResult.error("Test error");
        assertFalse(result.isValid());
        assertTrue(result.hasErrors());
        assertEquals(1, result.getErrorCount());
        assertEquals("Test error", result.getErrorMessage());
    }

    @Test
    @DisplayName("ValidationResult - добавление ошибок")
    public void testValidationResultAddErrors() {
        ValidationResult result = new ValidationResult();
        result.addError("Error 1");
        result.addError("Error 2");
        
        assertFalse(result.isValid());
        assertEquals(2, result.getErrorCount());
        assertTrue(result.getAllErrorsAsString().contains("Error 1"));
        assertTrue(result.getAllErrorsAsString().contains("Error 2"));
    }
}

