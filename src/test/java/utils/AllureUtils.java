package utils;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Утилита для работы с Allure отчетностью
 * Предоставляет методы для attachments, steps, links и метрик
 */
public class AllureUtils {

    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // ==================== ATTACHMENTS ====================

    /**
     * Прикрепить скриншот к отчету
     */
    @Attachment(value = "Screenshot", type = "image/png")
    public static byte[] attachScreenshot(WebDriver driver) {
        if (driver instanceof TakesScreenshot) {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        }
        return new byte[0];
    }

    /**
     * Прикрепить скриншот с кастомным именем
     */
    public static void attachScreenshot(WebDriver driver, String name) {
        if (driver instanceof TakesScreenshot) {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(name, "image/png", new ByteArrayInputStream(screenshot), "png");
        }
    }

    /**
     * Прикрепить текст к отчету
     */
    @Attachment(value = "Text Attachment", type = "text/plain")
    public static String attachText(String text) {
        return text;
    }

    /**
     * Прикрепить JSON к отчету
     */
    @Attachment(value = "JSON Attachment", type = "application/json")
    public static String attachJson(String json) {
        return json;
    }

    /**
     * Прикрепить HTML к отчету
     */
    @Attachment(value = "HTML Attachment", type = "text/html")
    public static String attachHtml(String html) {
        return html;
    }

    /**
     * Прикрепить файл к отчету
     */
    public static void attachFile(String filePath, String name) {
        try {
            byte[] fileContent = Files.readAllBytes(Paths.get(filePath));
            String contentType = getContentType(filePath);
            Allure.addAttachment(name, contentType, new ByteArrayInputStream(fileContent), getFileExtension(filePath));
        } catch (IOException e) {
            Allure.addAttachment("Error reading file: " + filePath, "text/plain", e.getMessage());
        }
    }

    /**
     * Прикрепить страницу браузера (HTML + скриншот)
     */
    public static void attachPageSource(WebDriver driver, String name) {
        try {
            // Прикрепляем HTML страницы
            String pageSource = driver.getPageSource();
            Allure.addAttachment(name + " - Page Source", "text/html", pageSource);
            
            // Прикрепляем скриншот
            attachScreenshot(driver, name + " - Screenshot");
            
            // Прикрепляем URL
            Allure.addAttachment(name + " - URL", "text/plain", driver.getCurrentUrl());
            
        } catch (Exception e) {
            Allure.addAttachment("Error attaching page source", "text/plain", e.getMessage());
        }
    }

    // ==================== STEPS ====================

    /**
     * Создать шаг с автоматическим логированием
     */
    @Step("{stepName}")
    public static void step(String stepName) {
        // Метод для создания простых шагов
    }

    /**
     * Создать шаг с параметрами
     */
    @Step("{stepName}: {parameter}")
    public static void step(String stepName, String parameter) {
        // Метод для создания шагов с параметрами
    }

    /**
     * Создать шаг с результатом
     */
    @Step("{stepName}")
    public static <T> T step(String stepName, java.util.function.Supplier<T> action) {
        return action.get();
    }

    /**
     * Создать шаг навигации
     */
    @Step("Navigate to: {url}")
    public static void navigateStep(String url) {
        // Метод для логирования навигации
    }

    /**
     * Создать шаг клика
     */
    @Step("Click on element: {elementName}")
    public static void clickStep(String elementName) {
        // Метод для логирования кликов
    }

    /**
     * Создать шаг ввода текста
     */
    @Step("Enter text '{text}' into field: {fieldName}")
    public static void enterTextStep(String fieldName, String text) {
        // Метод для логирования ввода текста
    }

    /**
     * Создать шаг проверки
     */
    @Step("Verify: {verification}")
    public static void verifyStep(String verification) {
        // Метод для логирования проверок
    }

    // ==================== LINKS ====================

    /**
     * Добавить ссылку на issue
     */
    public static void addIssueLink(String issueId) {
        Allure.addAttachment("Issue Link", "text/plain", 
            "Issue: " + issueId + "\nURL: https://jira.example.com/browse/" + issueId);
    }

    /**
     * Добавить ссылку на test case
     */
    public static void addTestCaseLink(String testCaseId) {
        Allure.addAttachment("Test Case Link", "text/plain", 
            "Test Case: " + testCaseId + "\nURL: https://testmanagement.example.com/testcase/" + testCaseId);
    }

    /**
     * Добавить кастомную ссылку
     */
    public static void addCustomLink(String name, String url) {
        Allure.addAttachment("Custom Link", "text/plain", 
            "Name: " + name + "\nURL: " + url);
    }

    // ==================== METRICS ====================

    /**
     * Добавить метрику времени выполнения
     */
    public static void addExecutionTime(long startTime, long endTime) {
        long duration = endTime - startTime;
        Allure.addAttachment("Execution Time", "text/plain", 
            String.format("Duration: %d ms (%.2f seconds)", duration, duration / 1000.0));
    }

    /**
     * Добавить метрику памяти
     */
    public static void addMemoryMetrics() {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        
        String memoryInfo = String.format(
            "Total Memory: %d MB\nFree Memory: %d MB\nUsed Memory: %d MB\nMax Memory: %d MB",
            totalMemory / 1024 / 1024,
            freeMemory / 1024 / 1024,
            usedMemory / 1024 / 1024,
            runtime.maxMemory() / 1024 / 1024
        );
        
        Allure.addAttachment("Memory Metrics", "text/plain", memoryInfo);
    }

    /**
     * Добавить метрику браузера
     */
    public static void addBrowserMetrics(WebDriver driver) {
        try {
            String browserInfo = String.format(
                "Browser: %s\nURL: %s\nTitle: %s\nWindow Size: %s",
                driver.getClass().getSimpleName(),
                driver.getCurrentUrl(),
                driver.getTitle(),
                driver.manage().window().getSize().toString()
            );
            
            Allure.addAttachment("Browser Metrics", "text/plain", browserInfo);
        } catch (Exception e) {
            Allure.addAttachment("Browser Metrics Error", "text/plain", e.getMessage());
        }
    }

    // ==================== UTILITIES ====================

    /**
     * Получить текущий timestamp
     */
    public static String getCurrentTimestamp() {
        return LocalDateTime.now().format(TIMESTAMP_FORMAT);
    }

    /**
     * Добавить timestamp к attachment
     */
    public static void addTimestamp() {
        Allure.addAttachment("Timestamp", "text/plain", getCurrentTimestamp());
    }

    /**
     * Определить тип контента по расширению файла
     */
    private static String getContentType(String filePath) {
        String extension = getFileExtension(filePath).toLowerCase();
        switch (extension) {
            case "png": return "image/png";
            case "jpg":
            case "jpeg": return "image/jpeg";
            case "gif": return "image/gif";
            case "html": return "text/html";
            case "txt": return "text/plain";
            case "json": return "application/json";
            case "xml": return "application/xml";
            case "csv": return "text/csv";
            case "log": return "text/plain";
            default: return "application/octet-stream";
        }
    }

    /**
     * Получить расширение файла
     */
    private static String getFileExtension(String filePath) {
        int lastDotIndex = filePath.lastIndexOf('.');
        return lastDotIndex > 0 ? filePath.substring(lastDotIndex + 1) : "";
    }

    /**
     * Создать детальный отчет об ошибке
     */
    public static void attachErrorDetails(WebDriver driver, Throwable error) {
        // Прикрепляем скриншот
        attachScreenshot(driver, "Error Screenshot");
        
        // Прикрепляем HTML страницы
        try {
            String pageSource = driver.getPageSource();
            Allure.addAttachment("Error Page Source", "text/html", pageSource);
        } catch (Exception e) {
            Allure.addAttachment("Error Page Source", "text/plain", "Could not get page source: " + e.getMessage());
        }
        
        // Прикрепляем детали ошибки
        String errorDetails = String.format(
            "Error: %s\nMessage: %s\nStack Trace:\n%s\nURL: %s\nTimestamp: %s",
            error.getClass().getSimpleName(),
            error.getMessage(),
            getStackTrace(error),
            driver.getCurrentUrl(),
            getCurrentTimestamp()
        );
        
        Allure.addAttachment("Error Details", "text/plain", errorDetails);
    }

    /**
     * Получить stack trace как строку
     */
    private static String getStackTrace(Throwable throwable) {
        java.io.StringWriter sw = new java.io.StringWriter();
        java.io.PrintWriter pw = new java.io.PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }
}
