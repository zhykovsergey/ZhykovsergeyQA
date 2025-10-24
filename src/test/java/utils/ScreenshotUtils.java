package utils;

import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.JavascriptExecutor;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Утилиты для создания скриншотов и прикрепления к Allure отчетам
 */
public class ScreenshotUtils {

    /**
     * Создать скриншот и прикрепить к Allure отчету
     */
    public static void attachScreenshot(WebDriver driver, String name) {
        try {
            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            byte[] screenshot = takesScreenshot.getScreenshotAs(OutputType.BYTES);
            
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            String fullName = name + " - " + timestamp;
            
            Allure.addAttachment(fullName, "image/png", new ByteArrayInputStream(screenshot), "png");
            
        } catch (Exception e) {
            Allure.addAttachment("Screenshot Error", 
                "text/plain", 
                "Failed to take screenshot: " + e.getMessage());
        }
    }

    /**
     * Создать скриншот с автоматическим именем
     */
    public static void attachScreenshot(WebDriver driver) {
        String currentUrl = driver.getCurrentUrl();
        String pageName = currentUrl.replaceAll("[^a-zA-Z0-9]", "_");
        attachScreenshot(driver, "Screenshot_" + pageName);
    }

    /**
     * Прикрепить информацию о странице к отчету
     */
    public static void attachPageInfo(WebDriver driver) {
        try {
            String pageInfo = String.format(
                "URL: %s\nTitle: %s\nPage Source Length: %d characters",
                driver.getCurrentUrl(),
                driver.getTitle(),
                driver.getPageSource().length()
            );
            
            Allure.addAttachment("Page Information", "text/plain", pageInfo);
            
        } catch (Exception e) {
            Allure.addAttachment("Page Info Error", 
                "text/plain", 
                "Failed to get page info: " + e.getMessage());
        }
    }

    /**
     * Прикрепить HTML код страницы
     */
    public static void attachPageSource(WebDriver driver, String name) {
        try {
            String pageSource = driver.getPageSource();
            Allure.addAttachment(name, "text/html", pageSource);
            
        } catch (Exception e) {
            Allure.addAttachment("Page Source Error", 
                "text/plain", 
                "Failed to get page source: " + e.getMessage());
        }
    }

    /**
     * Прикрепить информацию о консоли браузера
     */
    public static void attachConsoleLogs(WebDriver driver) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            String consoleLogs = (String) js.executeScript(
                "return JSON.stringify(console.logs || []);"
            );
            
            Allure.addAttachment("Console Logs", "application/json", consoleLogs);
            
        } catch (Exception e) {
            Allure.addAttachment("Console Logs Error", 
                "text/plain", 
                "Failed to get console logs: " + e.getMessage());
        }
    }

    /**
     * Создать полный дамп страницы (скриншот + HTML + информация)
     */
    public static void attachFullPageDump(WebDriver driver, String name) {
        attachScreenshot(driver, name + "_Screenshot");
        attachPageSource(driver, name + "_HTML");
        attachPageInfo(driver);
        attachConsoleLogs(driver);
    }
}


