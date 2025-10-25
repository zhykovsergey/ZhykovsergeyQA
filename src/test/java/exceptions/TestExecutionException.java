package exceptions;

/**
 * Базовое исключение для тестов
 */
public class TestExecutionException extends RuntimeException {
    
    public TestExecutionException(String message) {
        super(message);
    }
    
    public TestExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public TestExecutionException(Throwable cause) {
        super(cause);
    }
}

