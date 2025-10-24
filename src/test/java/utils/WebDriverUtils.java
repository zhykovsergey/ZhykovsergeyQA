package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Утилитарный класс для настройки WebDriver
 */
public class WebDriverUtils {

    private static final int DEFAULT_TIMEOUT_SECONDS = 10;

    /**
     * Создать WebDriver с видимым браузером
     */
    public static WebDriver createWebDriver() {
        return createWebDriver(false);
    }

    /**
     * Создать WebDriver с настройками
     * @param headless true для headless режима, false для видимого браузера
     */
    public static WebDriver createWebDriver(boolean headless) {
        // Подавление логов Selenium
        System.setProperty("webdriver.chrome.silentOutput", "true");
        System.setProperty("org.slf4j.simpleLogger.log.org.openqa.selenium", "ERROR");
        
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        
        // Базовые настройки
        if (headless) {
            options.addArguments("--headless=new");
        }
        
        // Обязательные настройки для стабильности
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--start-maximized");
        
        // Отключение сохранения паролей через preferences
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);
        
        // Отключение уведомлений и инфобаров
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-extensions");
        
        // Обход детекции автоматизации
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);
        
        WebDriver driver = new ChromeDriver(options);
        
        // Неявное ожидание
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        
        return driver;
    }

    /**
     * Создать WebDriverWait
     */
    public static WebDriverWait createWebDriverWait(WebDriver driver) {
        return createWebDriverWait(driver, DEFAULT_TIMEOUT_SECONDS);
    }

    /**
     * Создать WebDriverWait с кастомным таймаутом
     */
    public static WebDriverWait createWebDriverWait(WebDriver driver, int timeoutSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }

    /**
     * Открыть URL
     */
    public static void openUrl(WebDriver driver, String url) {
        driver.get(url);
    }

    /**
     * Получить заголовок страницы
     */
    public static String getPageTitle(WebDriver driver) {
        return driver.getTitle();
    }

    /**
     * Получить текущий URL
     */
    public static String getCurrentUrl(WebDriver driver) {
        return driver.getCurrentUrl();
    }

    /**
     * Ожидание загрузки страницы
     */
    public static void waitForPageLoad(WebDriver driver) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("return document.readyState").equals("complete");
        } catch (Exception e) {
            // Игнорируем ошибки JavaScript
        }
    }

    /**
     * Ожидание элемента с retry механизмом
     */
    public static void waitForElement(WebDriver driver, org.openqa.selenium.By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Ожидание кликабельности элемента
     */
    public static void waitForElementClickable(WebDriver driver, org.openqa.selenium.By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Ожидание видимости элемента
     */
    public static void waitForElementVisible(WebDriver driver, org.openqa.selenium.By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Выполнить JavaScript
     */
    public static Object executeJavaScript(WebDriver driver, String script, Object... args) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return js.executeScript(script, args);
    }

    /**
     * Прокрутить страницу вниз
     */
    public static void scrollDown(WebDriver driver) {
        executeJavaScript(driver, "window.scrollBy(0, 500);");
    }

    /**
     * Прокрутить страницу вверх
     */
    public static void scrollUp(WebDriver driver) {
        executeJavaScript(driver, "window.scrollBy(0, -500);");
    }

    /**
     * Прокрутить к элементу
     */
    public static void scrollToElement(WebDriver driver, org.openqa.selenium.WebElement element) {
        executeJavaScript(driver, "arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Очистить cookies
     */
    public static void clearCookies(WebDriver driver) {
        driver.manage().deleteAllCookies();
    }

    /**
     * Очистить localStorage
     */
    public static void clearLocalStorage(WebDriver driver) {
        executeJavaScript(driver, "localStorage.clear();");
    }

    /**
     * Очистить sessionStorage
     */
    public static void clearSessionStorage(WebDriver driver) {
        executeJavaScript(driver, "sessionStorage.clear();");
    }

    /**
     * Получить размер окна
     */
    public static org.openqa.selenium.Dimension getWindowSize(WebDriver driver) {
        return driver.manage().window().getSize();
    }

    /**
     * Установить размер окна
     */
    public static void setWindowSize(WebDriver driver, int width, int height) {
        driver.manage().window().setSize(new org.openqa.selenium.Dimension(width, height));
    }

    /**
     * Максимизировать окно
     */
    public static void maximizeWindow(WebDriver driver) {
        driver.manage().window().maximize();
    }

    /**
     * Проверить, загружена ли страница
     */
    public static boolean isPageLoaded(WebDriver driver) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            return js.executeScript("return document.readyState").equals("complete");
        } catch (Exception e) {
            return false;
        }
    }
}
