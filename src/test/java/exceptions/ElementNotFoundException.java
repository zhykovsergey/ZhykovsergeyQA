package exceptions;

import org.openqa.selenium.By;

/**
 * Исключение для случаев, когда элемент не найден на странице
 */
public class ElementNotFoundException extends RuntimeException {
    
    private final By locator;
    private final String pageName;
    private final long timeoutSeconds;
    
    public ElementNotFoundException(String message, By locator, String pageName) {
        super(message);
        this.locator = locator;
        this.pageName = pageName;
        this.timeoutSeconds = 0;
    }
    
    public ElementNotFoundException(String message, By locator, String pageName, long timeoutSeconds) {
        super(message);
        this.locator = locator;
        this.pageName = pageName;
        this.timeoutSeconds = timeoutSeconds;
    }
    
    public ElementNotFoundException(String message, By locator, String pageName, Throwable cause) {
        super(message, cause);
        this.locator = locator;
        this.pageName = pageName;
        this.timeoutSeconds = 0;
    }
    
    public By getLocator() {
        return locator;
    }
    
    public String getPageName() {
        return pageName;
    }
    
    public long getTimeoutSeconds() {
        return timeoutSeconds;
    }
    
    @Override
    public String toString() {
        return String.format(
            "ElementNotFoundException{message='%s', locator='%s', pageName='%s', timeoutSeconds=%d}",
            getMessage(), locator, pageName, timeoutSeconds
        );
    }
}