package exceptions;

/**
 * Исключение для ошибок подключения к API
 */
public class ApiConnectionException extends RuntimeException {
    
    private final String endpoint;
    private final int statusCode;
    private final String responseBody;
    private final long responseTime;
    
    public ApiConnectionException(String message, String endpoint, int statusCode) {
        super(message);
        this.endpoint = endpoint;
        this.statusCode = statusCode;
        this.responseBody = null;
        this.responseTime = 0;
    }
    
    public ApiConnectionException(String message, String endpoint, int statusCode, String responseBody) {
        super(message);
        this.endpoint = endpoint;
        this.statusCode = statusCode;
        this.responseBody = responseBody;
        this.responseTime = 0;
    }
    
    public ApiConnectionException(String message, String endpoint, int statusCode, String responseBody, long responseTime) {
        super(message);
        this.endpoint = endpoint;
        this.statusCode = statusCode;
        this.responseBody = responseBody;
        this.responseTime = responseTime;
    }
    
    public ApiConnectionException(String message, String endpoint, Throwable cause) {
        super(message, cause);
        this.endpoint = endpoint;
        this.statusCode = 0;
        this.responseBody = null;
        this.responseTime = 0;
    }
    
    public String getEndpoint() {
        return endpoint;
    }
    
    public int getStatusCode() {
        return statusCode;
    }
    
    public String getResponseBody() {
        return responseBody;
    }
    
    public long getResponseTime() {
        return responseTime;
    }
    
    @Override
    public String toString() {
        return String.format(
            "ApiConnectionException{message='%s', endpoint='%s', statusCode=%d, responseTime=%dms}",
            getMessage(), endpoint, statusCode, responseTime
        );
    }
}
