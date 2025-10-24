package utils;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Базовый класс для тестов с мониторингом и уведомлениями
 */
public abstract class MonitoredBaseTest {
    
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected long testStartTime;
    private String testName;
    private boolean testPassed = false;
    
    @BeforeEach
    @Step("Настройка мониторинга теста")
    void setupMonitoredTest() {
        testStartTime = System.currentTimeMillis();
        testName = getClass().getSimpleName();
        
        // Начинаем мониторинг
        MonitoringUtils.startTimer("test.execution");
        MonitoringUtils.startTimer("test.setup");
        MonitoringUtils.incrementCounter("tests.total");
        
        // Создаем WebDriver
        driver = BrowserUtils.createWebDriverFromProperties();
        wait = new WebDriverWait(driver, Duration.ofSeconds(Config.getTimeout()));
        
        // Начинаем отслеживание теста
        AllureReportUtils.startTest(testName);
        AllureReportUtils.addCustomMetric(testName, "Test Type", "Monitored Test");
        
        // Прикрепляем информацию о браузере
        String browserInfo = BrowserUtils.getBrowserInfo(driver);
        Allure.addAttachment("Browser Information", "text/plain", browserInfo);
        
        // Прикрепляем системные метрики
        MonitoringUtils.attachMetricsReport();
        
        // Логируем начало теста
        LoggerUtils.logTestStart(null, testName, "Мониторинг тест");
        AllureReportUtils.addTestStep(testName, "Setup", "Настройка мониторинга");
        
        // Останавливаем таймер настройки
        MonitoringUtils.stopTimer("test.setup");
    }

    @AfterEach
    @Step("Завершение мониторинга теста")
    void tearDownMonitoredTest() {
        if (driver != null) {
            try {
                // Начинаем таймер завершения
                MonitoringUtils.startTimer("test.teardown");
                
                // Определяем результат теста
                testPassed = true; // Предполагаем успех, если не было исключений
                
                // Останавливаем таймер выполнения теста
                long testDuration = MonitoringUtils.stopTimer("test.execution");
                
                // Обновляем счетчики
                if (testPassed) {
                    MonitoringUtils.incrementCounter("tests.passed");
                } else {
                    MonitoringUtils.incrementCounter("tests.failed");
                }
                
                // Вычисляем метрики
                long setupTime = MonitoringUtils.getTimerDuration("test.setup");
                long teardownTime = MonitoringUtils.stopTimer("test.teardown");
                
                // Устанавливаем гейджи
                MonitoringUtils.setGauge("test.success.rate", 
                    (double) MonitoringUtils.getCounterValue("tests.passed") / 
                    MonitoringUtils.getCounterValue("tests.total") * 100);
                MonitoringUtils.setGauge("test.failure.rate", 
                    (double) MonitoringUtils.getCounterValue("tests.failed") / 
                    MonitoringUtils.getCounterValue("tests.total") * 100);
                
                // Прикрепляем метрики к Allure
                AllureReportUtils.addCustomMetric(testName, "Test Duration", testDuration + " ms");
                AllureReportUtils.addCustomMetric(testName, "Setup Time", setupTime + " ms");
                AllureReportUtils.addCustomMetric(testName, "Teardown Time", teardownTime + " ms");
                AllureReportUtils.addCustomMetric(testName, "Test Passed", testPassed);
                
                // Прикрепляем финальные метрики
                MonitoringUtils.attachMetricsReport();
                
                // Делаем финальный скриншот
                ScreenshotUtils.attachScreenshot(driver, "Final Screenshot");
                
                // Прикрепляем информацию о странице
                ScreenshotUtils.attachPageSource(driver, "Final Page State");
                
                // Завершаем отслеживание теста
                AllureReportUtils.endTest(testName, 
                    testPassed ? io.qameta.allure.model.Status.PASSED : io.qameta.allure.model.Status.FAILED);
                
                // Логируем завершение теста
                LoggerUtils.logTestEnd(null, testName, testPassed, testDuration);
                AllureReportUtils.addTestStep(testName, "Teardown", "Завершение мониторинга");
                
                // Отправляем уведомления
                sendTestNotifications(testName, testPassed, testDuration);
                
            } catch (Exception e) {
                testPassed = false;
                MonitoringUtils.incrementCounter("tests.failed");
                AllureReportUtils.addTestError(testName, "Ошибка в tearDown: " + e.getMessage());
                LoggerUtils.logError("Ошибка в tearDown", e);
                Allure.addAttachment("Error in tearDown", 
                    "text/plain", 
                    "Error: " + e.getMessage());
                
                // Отправляем уведомление об ошибке
                sendErrorNotification(testName, e);
                
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
        MonitoringUtils.startTimer("page.load");
        WaitUtils.waitForPageLoad(driver);
        long loadTime = MonitoringUtils.stopTimer("page.load");
        
        // Обновляем метрики производительности
        MonitoringUtils.incrementCounter("ui.actions.total");
        MonitoringUtils.incrementCounter("ui.actions.success");
        MonitoringUtils.setGauge("ui.page.load.time.avg", loadTime);
        
        AllureReportUtils.addTestStep(testName, "Wait", "Ожидание загрузки страницы: " + loadTime + " ms");
    }
    
    /**
     * Ожидание элемента с повторными попытками
     */
    protected void waitForElementWithRetry(org.openqa.selenium.By locator, int maxRetries) {
        MonitoringUtils.startTimer("element.wait");
        WaitUtils.waitForElementWithRetry(driver, locator, maxRetries);
        long waitTime = MonitoringUtils.stopTimer("element.wait");
        
        // Обновляем метрики производительности
        MonitoringUtils.incrementCounter("ui.actions.total");
        MonitoringUtils.incrementCounter("ui.actions.success");
        MonitoringUtils.setGauge("ui.element.wait.time.avg", waitTime);
        
        AllureReportUtils.addTestStep(testName, "Wait", "Ожидание элемента с retry: " + locator + " (" + waitTime + " ms)");
    }
    
    /**
     * Ожидание элемента с кастомным таймаутом
     */
    protected void waitForElement(org.openqa.selenium.By locator, int timeoutSeconds) {
        MonitoringUtils.startTimer("element.wait");
        WaitUtils.waitForElement(driver, locator, timeoutSeconds);
        long waitTime = MonitoringUtils.stopTimer("element.wait");
        
        // Обновляем метрики производительности
        MonitoringUtils.incrementCounter("ui.actions.total");
        MonitoringUtils.incrementCounter("ui.actions.success");
        MonitoringUtils.setGauge("ui.element.wait.time.avg", waitTime);
        
        AllureReportUtils.addTestStep(testName, "Wait", "Ожидание элемента: " + locator + " (" + waitTime + " ms)");
    }
    
    /**
     * Ожидание видимости элемента
     */
    protected void waitForElementVisible(org.openqa.selenium.By locator) {
        MonitoringUtils.startTimer("element.wait");
        WaitUtils.waitForElementVisible(driver, locator);
        long waitTime = MonitoringUtils.stopTimer("element.wait");
        
        // Обновляем метрики производительности
        MonitoringUtils.incrementCounter("ui.actions.total");
        MonitoringUtils.incrementCounter("ui.actions.success");
        MonitoringUtils.setGauge("ui.element.wait.time.avg", waitTime);
        
        AllureReportUtils.addTestStep(testName, "Wait", "Ожидание видимости элемента: " + locator + " (" + waitTime + " ms)");
    }
    
    /**
     * Ожидание кликабельности элемента
     */
    protected void waitForElementClickable(org.openqa.selenium.By locator) {
        MonitoringUtils.startTimer("element.wait");
        WaitUtils.waitForElementClickable(driver, locator);
        long waitTime = MonitoringUtils.stopTimer("element.wait");
        
        // Обновляем метрики производительности
        MonitoringUtils.incrementCounter("ui.actions.total");
        MonitoringUtils.incrementCounter("ui.actions.success");
        MonitoringUtils.setGauge("ui.element.wait.time.avg", waitTime);
        
        AllureReportUtils.addTestStep(testName, "Wait", "Ожидание кликабельности элемента: " + locator + " (" + waitTime + " ms)");
    }

    /**
     * Вспомогательный метод для навигации
     */
    @Step("Переход на страницу: {url}")
    protected void navigateTo(String url) {
        MonitoringUtils.startTimer("navigation");
        AllureReportUtils.addTestStep(testName, "Navigation", "Переход на страницу: " + url);
        WebDriverUtils.openUrl(driver, url);
        waitForPageLoad();
        
        long navigationTime = MonitoringUtils.stopTimer("navigation");
        
        // Обновляем метрики производительности
        MonitoringUtils.incrementCounter("ui.actions.total");
        MonitoringUtils.incrementCounter("ui.actions.success");
        
        // Прикрепляем информацию о навигации
        Allure.addAttachment("Navigation Info", "text/plain", 
            "URL: " + driver.getCurrentUrl() + "\nTitle: " + driver.getTitle() + "\nTime: " + navigationTime + " ms");
    }

    /**
     * Вспомогательный метод для логирования действий
     */
    @Step("Выполнение действия: {action}")
    protected void logAction(String action) {
        AllureReportUtils.addTestStep(testName, "Action", action);
        LoggerUtils.logAction(action, testName);
        
        // Обновляем метрики
        MonitoringUtils.incrementCounter("ui.actions.total");
        MonitoringUtils.incrementCounter("ui.actions.success");
    }

    /**
     * Вспомогательный метод для прикрепления ошибок
     */
    protected void attachError(Throwable error) {
        testPassed = false;
        MonitoringUtils.incrementCounter("ui.actions.failed");
        AllureReportUtils.addTestError(testName, error.getMessage());
        LoggerUtils.logError("Ошибка в тесте " + testName, error);
        ScreenshotUtils.attachScreenshot(driver, "Error Screenshot");
        
        // Отправляем уведомление об ошибке
        sendErrorNotification(testName, error);
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
        
        // Отправляем уведомление о производительности
        sendPerformanceNotification(operation, duration, additionalMetrics);
    }
    
    /**
     * Вспомогательный метод для получения информации о браузере
     */
    protected String getBrowserInfo() {
        return BrowserUtils.getBrowserInfo(driver);
    }
    
    /**
     * Вспомогательный метод для получения имени теста
     */
    protected String getTestName() {
        return testName;
    }
    
    // ==================== УВЕДОМЛЕНИЯ ====================
    
    /**
     * Отправляет уведомления о результате теста
     */
    private void sendTestNotifications(String testName, boolean success, long duration) {
        if (!NotificationUtils.isNotificationsEnabled()) {
            return;
        }
        
        String details = String.format("Duration: %d ms, Browser: %s", duration, getBrowserInfo());
        
        // Slack уведомление
        String slackWebhook = NotificationUtils.getSlackWebhookUrl();
        if (!slackWebhook.isEmpty()) {
            NotificationUtils.sendTestResultNotification(slackWebhook, testName, success, details);
        }
        
        // Telegram уведомление
        String telegramToken = NotificationUtils.getTelegramBotToken();
        String telegramChatId = NotificationUtils.getTelegramChatId();
        if (!telegramToken.isEmpty() && !telegramChatId.isEmpty()) {
            NotificationUtils.sendTestResultTelegram(telegramToken, telegramChatId, testName, success, details);
        }
    }
    
    /**
     * Отправляет уведомление об ошибке
     */
    private void sendErrorNotification(String testName, Throwable error) {
        if (!NotificationUtils.isNotificationsEnabled()) {
            return;
        }
        
        String slackWebhook = NotificationUtils.getSlackWebhookUrl();
        if (!slackWebhook.isEmpty()) {
            NotificationUtils.sendErrorNotification(slackWebhook, testName, error.getMessage(), 
                java.util.Arrays.toString(error.getStackTrace()));
        }
    }
    
    /**
     * Отправляет уведомление о производительности
     */
    private void sendPerformanceNotification(String operation, long duration, java.util.Map<String, Object> metrics) {
        if (!NotificationUtils.isNotificationsEnabled()) {
            return;
        }
        
        String slackWebhook = NotificationUtils.getSlackWebhookUrl();
        if (!slackWebhook.isEmpty()) {
            NotificationUtils.sendPerformanceNotification(slackWebhook, operation, duration, metrics);
        }
    }
}
