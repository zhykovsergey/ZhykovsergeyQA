package utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

/**
 * Утилиты для надежных ожиданий элементов
 */
public class WaitUtils {
    
    private static final int DEFAULT_TIMEOUT = 10;
    private static final int POLLING_INTERVAL = 500;
    
    /**
     * Ожидание элемента с повторными попытками
     */
    public static WebElement waitForElementWithRetry(WebDriver driver, By locator, int maxRetries) {
        for (int i = 0; i < maxRetries; i++) {
            try {
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
                return wait.until(ExpectedConditions.elementToBeClickable(locator));
            } catch (TimeoutException e) {
                if (i == maxRetries - 1) {
                    throw new RuntimeException("Элемент не найден после " + maxRetries + " попыток: " + locator, e);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Прервано ожидание", ie);
                }
            }
        }
        return null;
    }
    
    /**
     * Ожидание элемента с кастомным таймаутом
     */
    public static WebElement waitForElement(WebDriver driver, By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    
    /**
     * Ожидание видимости элемента
     */
    public static WebElement waitForElementVisible(WebDriver driver, By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    
    /**
     * Ожидание исчезновения элемента
     */
    public static boolean waitForElementToDisappear(WebDriver driver, By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
    
    /**
     * Ожидание загрузки страницы
     */
    public static void waitForPageLoad(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        wait.until(webDriver -> 
            ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
    }
    
    /**
     * Ожидание с FluentWait для более гибкого контроля
     */
    public static <T> T waitForCondition(WebDriver driver, Function<WebDriver, T> condition, int timeoutSeconds) {
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeoutSeconds))
                .pollingEvery(Duration.ofMillis(POLLING_INTERVAL))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
        
        return wait.until(condition);
    }
    
    /**
     * Ожидание текста в элементе
     */
    public static boolean waitForTextInElement(WebDriver driver, By locator, String expectedText) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, expectedText));
    }
    
    /**
     * Ожидание кликабельности элемента
     */
    public static WebElement waitForElementClickable(WebDriver driver, By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    
    /**
     * Ожидание списка элементов
     */
    public static List<WebElement> waitForElements(WebDriver driver, By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }
    
    /**
     * Ожидание с кастомным сообщением об ошибке
     */
    public static WebElement waitForElementWithMessage(WebDriver driver, By locator, String errorMessage) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
            return wait.until(ExpectedConditions.elementToBeClickable(locator));
        } catch (TimeoutException e) {
            throw new RuntimeException(errorMessage + " (селектор: " + locator + ")", e);
        }
    }
}
