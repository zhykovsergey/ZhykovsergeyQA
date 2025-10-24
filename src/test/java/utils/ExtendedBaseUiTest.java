package utils;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Расширенный базовый класс для UI тестов с поддержкой разных браузеров
 */
public abstract class ExtendedBaseUiTest {
    
    protected WebDriver driver;
    protected WebDriverWait wait;
    private long testStartTime;
    private String testName;
    private BrowserUtils.BrowserType browserType;
    
    @BeforeEach
    @Step("Настройка расширенного браузера")
    void setupExtendedDriver() {
        testStartTime = System.currentTimeMillis();
        testName = getClass().getSimpleName();
        
        // Получаем тип браузера из системных свойств
        browserType = BrowserUtils.getBrowserTypeFromProperties();
        boolean headless = BrowserUtils.getHeadlessModeFromProperties();
        
        // Создаем WebDriver
        driver = BrowserUtils.createWebDriver(browserType, headless);
        wait = new WebDriverWait(driver, Duration.ofSeconds(Config.getTimeout()));
        
        // Начинаем отслеживание теста
        AllureReportUtils.startTest(testName);
        AllureReportUtils.addCustomMetric(testName, "Browser Type", browserType.toString());
        AllureReportUtils.addCustomMetric(testName, "Headless Mode", headless);
        
        // Прикрепляем информацию о браузере к Allure
        AllureReportUtils.attachEnvironmentInfo();
        AllureReportUtils.attachConfigurationInfo();
        
        // Прикрепляем информацию о браузере
        String browserInfo = BrowserUtils.getBrowserInfo(driver);
        Allure.addAttachment("Browser Information", "text/plain", browserInfo);
        
        // Прикрепляем конфигурацию
        String configInfo = String.format(
            "Browser: %s\nHeadless: %s\nTimeout: %ds\nEnvironment: %s\nDebug Mode: %s\nTest Name: %s",
            browserType,
            headless,
            Config.getTimeout(),
            Config.getEnvironment(),
            Config.isDebugMode(),
            testName
        );
        Allure.addAttachment("Test Configuration", "text/plain", configInfo);
        
        // Логируем начало теста
        LoggerUtils.logTestStart(null, testName, "Расширенный UI тест");
        AllureReportUtils.addTestStep(testName, "Setup", "Настройка браузера " + browserType);
    }

    @AfterEach
    @Step("Закрытие расширенного браузера")
    void tearDownExtendedDriver() {
        if (driver != null) {
            try {
                // Прикрепляем финальную информацию о браузере
                String finalBrowserInfo = BrowserUtils.getBrowserInfo(driver);
                Allure.addAttachment("Final Browser State", "text/plain", finalBrowserInfo);
                
                // Прикрепляем метрики времени выполнения
                long testEndTime = System.currentTimeMillis();
                long testDuration = testEndTime - testStartTime;
                
                AllureReportUtils.addCustomMetric(testName, "Test Duration", testDuration + " ms");
                AllureReportUtils.addCustomMetric(testName, "Browser Type", browserType.toString());
                
                // Делаем финальный скриншот
                ScreenshotUtils.attachScreenshot(driver, "Final Screenshot");
                
                // Прикрепляем информацию о странице
                ScreenshotUtils.attachPageSource(driver, "Final Page State");
                
                // Завершаем отслеживание теста
                AllureReportUtils.endTest(testName, io.qameta.allure.model.Status.PASSED);
                
                // Логируем завершение теста
                LoggerUtils.logTestEnd(null, testName, true, testDuration);
                AllureReportUtils.addTestStep(testName, "Teardown", "Закрытие браузера " + browserType);
                
            } catch (Exception e) {
                AllureReportUtils.addTestError(testName, "Ошибка в tearDown: " + e.getMessage());
                LoggerUtils.logError("Ошибка в tearDown", e);
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
        AllureReportUtils.addTestStep(testName, "Wait", "Ожидание загрузки страницы");
    }
    
    /**
     * Ожидание элемента с повторными попытками
     */
    protected void waitForElementWithRetry(org.openqa.selenium.By locator, int maxRetries) {
        WaitUtils.waitForElementWithRetry(driver, locator, maxRetries);
        AllureReportUtils.addTestStep(testName, "Wait", "Ожидание элемента с retry: " + locator);
    }
    
    /**
     * Ожидание элемента с кастомным таймаутом
     */
    protected void waitForElement(org.openqa.selenium.By locator, int timeoutSeconds) {
        WaitUtils.waitForElement(driver, locator, timeoutSeconds);
        AllureReportUtils.addTestStep(testName, "Wait", "Ожидание элемента: " + locator);
    }
    
    /**
     * Ожидание видимости элемента
     */
    protected void waitForElementVisible(org.openqa.selenium.By locator) {
        WaitUtils.waitForElementVisible(driver, locator);
        AllureReportUtils.addTestStep(testName, "Wait", "Ожидание видимости элемента: " + locator);
    }
    
    /**
     * Ожидание кликабельности элемента
     */
    protected void waitForElementClickable(org.openqa.selenium.By locator) {
        WaitUtils.waitForElementClickable(driver, locator);
        AllureReportUtils.addTestStep(testName, "Wait", "Ожидание кликабельности элемента: " + locator);
    }

    /**
     * Вспомогательный метод для навигации
     */
    @Step("Переход на страницу: {url}")
    protected void navigateTo(String url) {
        AllureReportUtils.addTestStep(testName, "Navigation", "Переход на страницу: " + url);
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
        AllureReportUtils.addTestStep(testName, "Action", action);
        LoggerUtils.logAction(action, testName);
    }

    /**
     * Вспомогательный метод для прикрепления ошибок
     */
    protected void attachError(Throwable error) {
        AllureReportUtils.addTestError(testName, error.getMessage());
        LoggerUtils.logError("Ошибка в тесте " + testName, error);
        ScreenshotUtils.attachScreenshot(driver, "Error Screenshot");
    }
    
    /**
     * Вспомогательный метод для прикрепления данных
     */
    protected void attachData(String dataName, String data) {
        AllureReportUtils.attachTestData(dataName, data);
        AllureReportUtils.addTestStep(testName, "Data", "Прикреплены данные: " + dataName);
    }
    
    /**
     * Вспомогательный метод для прикрепления JSON данных
     */
    protected void attachJsonData(String dataName, String jsonData) {
        AllureReportUtils.attachJsonData(dataName, jsonData);
        AllureReportUtils.addTestStep(testName, "JSON Data", "Прикреплены JSON данные: " + dataName);
    }
    
    /**
     * Вспомогательный метод для прикрепления метрик производительности
     */
    protected void attachPerformanceMetrics(String operation, long duration, java.util.Map<String, Object> additionalMetrics) {
        AllureReportUtils.attachPerformanceMetrics(operation, duration, additionalMetrics);
        AllureReportUtils.addTestStep(testName, "Performance", "Метрики производительности: " + operation);
    }
    
    /**
     * Вспомогательный метод для получения информации о браузере
     */
    protected String getBrowserInfo() {
        return BrowserUtils.getBrowserInfo(driver);
    }
    
    /**
     * Вспомогательный метод для получения типа браузера
     */
    protected BrowserUtils.BrowserType getBrowserType() {
        return browserType;
    }
    
    /**
     * Вспомогательный метод для получения имени теста
     */
    protected String getTestName() {
        return testName;
    }
}
