package utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Результат валидации с детальной информацией об ошибках
 */
public class ValidationResult {
    
    private boolean valid;
    private List<String> errors;
    
    /**
     * Конструктор по умолчанию
     */
    public ValidationResult() {
        this.valid = true;
        this.errors = new ArrayList<>();
    }
    
    /**
     * Конструктор с начальным состоянием
     */
    public ValidationResult(boolean valid) {
        this.valid = valid;
        this.errors = new ArrayList<>();
    }
    
    /**
     * Создает успешный результат валидации
     */
    public static ValidationResult success() {
        return new ValidationResult(true);
    }
    
    /**
     * Создает результат с ошибкой
     */
    public static ValidationResult error(String errorMessage) {
        ValidationResult result = new ValidationResult(false);
        result.addError(errorMessage);
        return result;
    }
    
    /**
     * Проверяет, валидны ли данные
     */
    public boolean isValid() {
        return valid && errors.isEmpty();
    }
    
    /**
     * Добавляет ошибку
     */
    public void addError(String errorMessage) {
        this.valid = false;
        this.errors.add(errorMessage);
    }
    
    /**
     * Добавляет ошибки из другого результата
     */
    public void addErrors(ValidationResult other) {
        if (!other.isValid()) {
            this.valid = false;
            this.errors.addAll(other.getErrors());
        }
    }
    
    /**
     * Получает список ошибок
     */
    public List<String> getErrors() {
        return new ArrayList<>(errors);
    }
    
    /**
     * Получает первую ошибку
     */
    public String getErrorMessage() {
        if (errors.isEmpty()) {
            return null;
        }
        return errors.get(0);
    }
    
    /**
     * Получает все ошибки в виде строки
     */
    public String getAllErrorsAsString() {
        if (errors.isEmpty()) {
            return "Нет ошибок";
        }
        return String.join("; ", errors);
    }
    
    /**
     * Получает количество ошибок
     */
    public int getErrorCount() {
        return errors.size();
    }
    
    /**
     * Проверяет, есть ли ошибки
     */
    public boolean hasErrors() {
        return !errors.isEmpty();
    }
    
    /**
     * Очищает все ошибки
     */
    public void clearErrors() {
        this.errors.clear();
        this.valid = true;
    }
    
    /**
     * Создает копию результата
     */
    public ValidationResult copy() {
        ValidationResult copy = new ValidationResult(this.valid);
        copy.errors.addAll(this.errors);
        return copy;
    }
    
    @Override
    public String toString() {
        if (isValid()) {
            return "ValidationResult{valid=true, errors=[]}";
        } else {
            return "ValidationResult{valid=false, errors=" + errors + "}";
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        ValidationResult that = (ValidationResult) obj;
        
        if (valid != that.valid) return false;
        return errors != null ? errors.equals(that.errors) : that.errors == null;
    }
    
    @Override
    public int hashCode() {
        int result = (valid ? 1 : 0);
        result = 31 * result + (errors != null ? errors.hashCode() : 0);
        return result;
    }
}

