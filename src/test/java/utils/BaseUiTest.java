package utils;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Базовый класс для всех UI тестов
 * Содержит общую настройку WebDriver и автоматические скриншоты
 */
public abstract class BaseUiTest {

    protected WebDriver driver;
    protected WebDriverWait wait;
    private long testStartTime;

    @BeforeEach
    @Step("Настройка браузера")
    void setupDriver() {
        testStartTime = System.currentTimeMillis();
        
        driver = WebDriverUtils.createWebDriver(Config.isHeadless());
        wait = WebDriverUtils.createWebDriverWait(driver, Config.getTimeout());
        
        // Прикрепляем информацию о браузере к Allure
        Allure.addAttachment("Browser Info", "text/plain", 
            "Browser: " + driver.getClass().getSimpleName());
        Allure.addAttachment("Memory Metrics", "text/plain", 
            "Free Memory: " + Runtime.getRuntime().freeMemory() + " bytes\n" +
            "Total Memory: " + Runtime.getRuntime().totalMemory() + " bytes\n" +
            "Max Memory: " + Runtime.getRuntime().maxMemory() + " bytes");
        Allure.addAttachment("Test Timestamp", "text/plain", 
            "Test started at: " + java.time.LocalDateTime.now());
        
        // Прикрепляем конфигурацию
        String configInfo = String.format(
            "Browser: %s\nHeadless: %s\nTimeout: %ds\nEnvironment: %s\nDebug Mode: %s",
            driver.getClass().getSimpleName(),
            Config.isHeadless(),
            Config.getTimeout(),
            Config.getEnvironment(),
            Config.isDebugMode()
        );
        Allure.addAttachment("Test Configuration", "text/plain", configInfo);
    }

    @AfterEach
    @Step("Закрытие браузера")
    void tearDownDriver() {
        if (driver != null) {
            try {
                // Прикрепляем метрики времени выполнения
                long testEndTime = System.currentTimeMillis();
                long executionTime = testEndTime - testStartTime;
                Allure.addAttachment("Execution Time", "text/plain", 
                    "Test execution time: " + executionTime + " ms");
                
                // Делаем финальный скриншот
                try {
                    byte[] screenshot = ((org.openqa.selenium.TakesScreenshot) driver).getScreenshotAs(org.openqa.selenium.OutputType.BYTES);
                    Allure.addAttachment("Final Screenshot", "image/png", new java.io.ByteArrayInputStream(screenshot), "png");
                } catch (Exception e) {
                    Allure.addAttachment("Screenshot Error", "text/plain", "Failed to take screenshot: " + e.getMessage());
                }
                
                // Прикрепляем информацию о странице
                try {
                    String pageSource = driver.getPageSource();
                    Allure.addAttachment("Final Page State", "text/html", pageSource);
                } catch (Exception e) {
                    Allure.addAttachment("Page Source Error", "text/plain", "Failed to get page source: " + e.getMessage());
                }
                
                // Прикрепляем финальные метрики
                Allure.addAttachment("Memory Metrics", "text/plain", 
                    "Free Memory: " + Runtime.getRuntime().freeMemory() + " bytes\n" +
                    "Total Memory: " + Runtime.getRuntime().totalMemory() + " bytes\n" +
                    "Max Memory: " + Runtime.getRuntime().maxMemory() + " bytes");
                Allure.addAttachment("Test Timestamp", "text/plain", 
                    "Test ended at: " + java.time.LocalDateTime.now());
                
            } catch (Exception e) {
                Allure.addAttachment("Error in tearDown", 
                    "text/plain", 
                    "Error: " + e.getMessage());
            } finally {
                driver.quit();
            }
        }
    }

    /**
     * Вспомогательный метод для ожидания загрузки страницы
     */
    @Step("Ожидание загрузки страницы")
    protected void waitForPageLoad() {
        wait.until(webDriver -> 
            ((org.openqa.selenium.JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
    }
    
    /**
     * Ожидание элемента с повторными попытками
     */
    protected void waitForElementWithRetry(org.openqa.selenium.By locator, int maxRetries) {
        for (int i = 0; i < maxRetries; i++) {
            try {
                wait.until(org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated(locator));
                return;
            } catch (Exception e) {
                if (i == maxRetries - 1) throw e;
                try { Thread.sleep(1000); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
            }
        }
    }
    
    /**
     * Ожидание элемента с кастомным таймаутом
     */
    protected void waitForElement(org.openqa.selenium.By locator, int timeoutSeconds) {
        WebDriverWait customWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(timeoutSeconds));
        customWait.until(org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated(locator));
    }
    
    /**
     * Ожидание видимости элемента
     */
    protected void waitForElementVisible(org.openqa.selenium.By locator) {
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(locator));
    }
    
    /**
     * Ожидание кликабельности элемента
     */
    protected void waitForElementClickable(org.openqa.selenium.By locator) {
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Вспомогательный метод для навигации
     */
    @Step("Переход на страницу: {url}")
    protected void navigateTo(String url) {
        driver.get(url);
        waitForPageLoad();
        
        // Прикрепляем информацию о навигации
        Allure.addAttachment("Navigation Info", "text/plain", 
            "URL: " + driver.getCurrentUrl() + "\nTitle: " + driver.getTitle());
    }

    /**
     * Вспомогательный метод для логирования действий
     */
    @Step("Выполнение действия: {action}")
    protected void logAction(String action) {
        Allure.addAttachment("Action Log", "text/plain", 
            "Action: " + action + "\nTimestamp: " + java.time.LocalDateTime.now());
    }

    /**
     * Вспомогательный метод для прикрепления ошибок
     */
    protected void attachError(Throwable error) {
        try {
            byte[] screenshot = ((org.openqa.selenium.TakesScreenshot) driver).getScreenshotAs(org.openqa.selenium.OutputType.BYTES);
            Allure.addAttachment("Error Screenshot", "image/png", new java.io.ByteArrayInputStream(screenshot), "png");
        } catch (Exception e) {
            Allure.addAttachment("Screenshot Error", "text/plain", "Failed to take error screenshot: " + e.getMessage());
        }
        Allure.addAttachment("Error Details", "text/plain", 
            "Error: " + error.getClass().getSimpleName() + "\n" +
            "Message: " + error.getMessage() + "\n" +
            "Stack Trace: " + java.util.Arrays.toString(error.getStackTrace()));
    }
}


