package exceptions;

/**
 * Исключение для API тестов
 */
public class ApiTestException extends TestExecutionException {
    
    private final int statusCode;
    private final String endpoint;
    
    public ApiTestException(String message) {
        super(message);
        this.statusCode = -1;
        this.endpoint = "";
    }
    
    public ApiTestException(String message, int statusCode) {
        super(message + " (Status: " + statusCode + ")");
        this.statusCode = statusCode;
        this.endpoint = "";
    }
    
    public ApiTestException(String message, int statusCode, String endpoint) {
        super(message + " (Status: " + statusCode + ", Endpoint: " + endpoint + ")");
        this.statusCode = statusCode;
        this.endpoint = endpoint;
    }
    
    public ApiTestException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = -1;
        this.endpoint = "";
    }
    
    public int getStatusCode() {
        return statusCode;
    }
    
    public String getEndpoint() {
        return endpoint;
    }
}
