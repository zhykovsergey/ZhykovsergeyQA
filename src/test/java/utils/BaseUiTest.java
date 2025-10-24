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
        AllureUtils.addBrowserMetrics(driver);
        AllureUtils.addMemoryMetrics();
        AllureUtils.addTimestamp();
        
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
                AllureUtils.addExecutionTime(testStartTime, testEndTime);
                
                // Делаем финальный скриншот
                AllureUtils.attachScreenshot(driver, "Final Screenshot");
                
                // Прикрепляем информацию о странице
                AllureUtils.attachPageSource(driver, "Final Page State");
                
                // Прикрепляем финальные метрики
                AllureUtils.addMemoryMetrics();
                AllureUtils.addTimestamp();
                
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
        WaitUtils.waitForPageLoad(driver);
    }
    
    /**
     * Ожидание элемента с повторными попытками
     */
    protected void waitForElementWithRetry(org.openqa.selenium.By locator, int maxRetries) {
        WaitUtils.waitForElementWithRetry(driver, locator, maxRetries);
    }
    
    /**
     * Ожидание элемента с кастомным таймаутом
     */
    protected void waitForElement(org.openqa.selenium.By locator, int timeoutSeconds) {
        WaitUtils.waitForElement(driver, locator, timeoutSeconds);
    }
    
    /**
     * Ожидание видимости элемента
     */
    protected void waitForElementVisible(org.openqa.selenium.By locator) {
        WaitUtils.waitForElementVisible(driver, locator);
    }
    
    /**
     * Ожидание кликабельности элемента
     */
    protected void waitForElementClickable(org.openqa.selenium.By locator) {
        WaitUtils.waitForElementClickable(driver, locator);
    }

    /**
     * Вспомогательный метод для навигации
     */
    @Step("Переход на страницу: {url}")
    protected void navigateTo(String url) {
        AllureUtils.navigateStep(url);
        WebDriverUtils.openUrl(driver, url);
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
            "Action: " + action + "\nTimestamp: " + AllureUtils.getCurrentTimestamp());
    }

    /**
     * Вспомогательный метод для прикрепления ошибок
     */
    protected void attachError(Throwable error) {
        AllureUtils.attachErrorDetails(driver, error);
    }
}


