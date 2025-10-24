package exceptions;

import org.openqa.selenium.By;

/**
 * Исключение для случаев, когда элемент не найден
 */
public class ElementNotFoundException extends TestExecutionException {
    
    private final By locator;
    
    public ElementNotFoundException(By locator) {
        super("Элемент не найден: " + locator);
        this.locator = locator;
    }
    
    public ElementNotFoundException(By locator, String message) {
        super("Элемент не найден: " + locator + ". " + message);
        this.locator = locator;
    }
    
    public ElementNotFoundException(By locator, Throwable cause) {
        super("Элемент не найден: " + locator, cause);
        this.locator = locator;
    }
    
    public By getLocator() {
        return locator;
    }
}
