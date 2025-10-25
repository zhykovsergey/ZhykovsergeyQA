package utils;

import java.util.regex.Pattern;

/**
 * Утилиты для валидации входных данных
 */
public class ValidationUtils {
    
    // Регулярные выражения для валидации
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );
    
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^\\+?[1-9]\\d{1,14}$|^\\d{3}-\\d{3}-\\d{4}$|^\\(\\d{3}\\)\\s?\\d{3}-\\d{4}$"
    );
    
    private static final Pattern URL_PATTERN = Pattern.compile(
        "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$"
    );
    
    private static final Pattern USERNAME_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._-]{3,20}$"
    );
    
    // ==================== ВАЛИДАЦИЯ EMAIL ====================
    
    /**
     * Валидация email адреса
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
    
    /**
     * Валидация email с детальным сообщением об ошибке
     */
    public static ValidationResult validateEmail(String email) {
        if (email == null) {
            return ValidationResult.error("Email не может быть null");
        }
        
        if (email.trim().isEmpty()) {
            return ValidationResult.error("Email не может быть пустым");
        }
        
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            return ValidationResult.error("Email имеет неверный формат: " + email);
        }
        
        return ValidationResult.success();
    }
    
    // ==================== ВАЛИДАЦИЯ ТЕЛЕФОНА ====================
    
    /**
     * Валидация номера телефона
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone.trim()).matches();
    }
    
    /**
     * Валидация телефона с детальным сообщением об ошибке
     */
    public static ValidationResult validatePhone(String phone) {
        if (phone == null) {
            return ValidationResult.error("Телефон не может быть null");
        }
        
        if (phone.trim().isEmpty()) {
            return ValidationResult.error("Телефон не может быть пустым");
        }
        
        if (!PHONE_PATTERN.matcher(phone.trim()).matches()) {
            return ValidationResult.error("Телефон имеет неверный формат: " + phone);
        }
        
        return ValidationResult.success();
    }
    
    // ==================== ВАЛИДАЦИЯ URL ====================
    
    /**
     * Валидация URL
     */
    public static boolean isValidUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return false;
        }
        return URL_PATTERN.matcher(url.trim()).matches();
    }
    
    /**
     * Валидация URL с детальным сообщением об ошибке
     */
    public static ValidationResult validateUrl(String url) {
        if (url == null) {
            return ValidationResult.error("URL не может быть null");
        }
        
        if (url.trim().isEmpty()) {
            return ValidationResult.error("URL не может быть пустым");
        }
        
        if (!URL_PATTERN.matcher(url.trim()).matches()) {
            return ValidationResult.error("URL имеет неверный формат: " + url);
        }
        
        return ValidationResult.success();
    }
    
    // ==================== ВАЛИДАЦИЯ USERNAME ====================
    
    /**
     * Валидация имени пользователя
     */
    public static boolean isValidUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        return USERNAME_PATTERN.matcher(username.trim()).matches();
    }
    
    /**
     * Валидация username с детальным сообщением об ошибке
     */
    public static ValidationResult validateUsername(String username) {
        if (username == null) {
            return ValidationResult.error("Username не может быть null");
        }
        
        if (username.trim().isEmpty()) {
            return ValidationResult.error("Username не может быть пустым");
        }
        
        if (username.length() < 3) {
            return ValidationResult.error("Username должен содержать минимум 3 символа");
        }
        
        if (username.length() > 20) {
            return ValidationResult.error("Username должен содержать максимум 20 символов");
        }
        
        if (!USERNAME_PATTERN.matcher(username.trim()).matches()) {
            return ValidationResult.error("Username содержит недопустимые символы: " + username);
        }
        
        return ValidationResult.success();
    }
    
    // ==================== ВАЛИДАЦИЯ СТРОК ====================
    
    /**
     * Валидация непустой строки
     */
    public static ValidationResult validateNotEmpty(String value, String fieldName) {
        if (value == null) {
            return ValidationResult.error(fieldName + " не может быть null");
        }
        
        if (value.trim().isEmpty()) {
            return ValidationResult.error(fieldName + " не может быть пустым");
        }
        
        return ValidationResult.success();
    }
    
    /**
     * Валидация длины строки
     */
    public static ValidationResult validateLength(String value, String fieldName, int minLength, int maxLength) {
        ValidationResult notEmptyResult = validateNotEmpty(value, fieldName);
        if (!notEmptyResult.isValid()) {
            return notEmptyResult;
        }
        
        int length = value.trim().length();
        if (length < minLength) {
            return ValidationResult.error(fieldName + " должен содержать минимум " + minLength + " символов");
        }
        
        if (length > maxLength) {
            return ValidationResult.error(fieldName + " должен содержать максимум " + maxLength + " символов");
        }
        
        return ValidationResult.success();
    }
    
    // ==================== ВАЛИДАЦИЯ ЧИСЕЛ ====================
    
    /**
     * Валидация положительного числа
     */
    public static ValidationResult validatePositiveNumber(int value, String fieldName) {
        if (value <= 0) {
            return ValidationResult.error(fieldName + " должен быть положительным числом");
        }
        return ValidationResult.success();
    }
    
    /**
     * Валидация числа в диапазоне
     */
    public static ValidationResult validateNumberRange(int value, String fieldName, int min, int max) {
        if (value < min || value > max) {
            return ValidationResult.error(fieldName + " должен быть в диапазоне от " + min + " до " + max);
        }
        return ValidationResult.success();
    }
    
    // ==================== ВАЛИДАЦИЯ МАССИВОВ ====================
    
    /**
     * Валидация непустого массива
     */
    public static ValidationResult validateNotEmptyArray(Object[] array, String fieldName) {
        if (array == null) {
            return ValidationResult.error(fieldName + " не может быть null");
        }
        
        if (array.length == 0) {
            return ValidationResult.error(fieldName + " не может быть пустым");
        }
        
        return ValidationResult.success();
    }
    
    // ==================== КОМПЛЕКСНАЯ ВАЛИДАЦИЯ ====================
    
    /**
     * Валидация всех полей пользователя
     */
    public static ValidationResult validateUser(String name, String username, String email, String phone, String website) {
        ValidationResult result = new ValidationResult();
        
        // Валидируем имя
        ValidationResult nameResult = validateLength(name, "Имя", 2, 50);
        if (!nameResult.isValid()) {
            result.addError(nameResult.getErrorMessage());
        }
        
        // Валидируем username
        ValidationResult usernameResult = validateUsername(username);
        if (!usernameResult.isValid()) {
            result.addError(usernameResult.getErrorMessage());
        }
        
        // Валидируем email
        ValidationResult emailResult = validateEmail(email);
        if (!emailResult.isValid()) {
            result.addError(emailResult.getErrorMessage());
        }
        
        // Валидируем телефон (опционально)
        if (phone != null && !phone.trim().isEmpty()) {
            ValidationResult phoneResult = validatePhone(phone);
            if (!phoneResult.isValid()) {
                result.addError(phoneResult.getErrorMessage());
            }
        }
        
        // Валидируем website (опционально)
        if (website != null && !website.trim().isEmpty()) {
            ValidationResult websiteResult = validateUrl(website);
            if (!websiteResult.isValid()) {
                result.addError(websiteResult.getErrorMessage());
            }
        }
        
        return result;
    }
    
    /**
     * Валидация всех полей поста
     */
    public static ValidationResult validatePost(int userId, String title, String body) {
        ValidationResult result = new ValidationResult();
        
        // Валидируем userId
        ValidationResult userIdResult = validatePositiveNumber(userId, "User ID");
        if (!userIdResult.isValid()) {
            result.addError(userIdResult.getErrorMessage());
        }
        
        // Валидируем заголовок
        ValidationResult titleResult = validateLength(title, "Заголовок", 1, 200);
        if (!titleResult.isValid()) {
            result.addError(titleResult.getErrorMessage());
        }
        
        // Валидируем тело поста
        ValidationResult bodyResult = validateLength(body, "Тело поста", 1, 1000);
        if (!bodyResult.isValid()) {
            result.addError(bodyResult.getErrorMessage());
        }
        
        return result;
    }
}

