package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Утилиты для работы с WebDriver
 */
public class WebDriverUtils {

    /**
     * Создать WebDriver с настройками
     */
    public static WebDriver createWebDriver(boolean headless) {
        String browser = Config.getBrowser().toLowerCase();
        
        // Отладочная информация
        System.out.println("🔧 WebDriverUtils: Создаем WebDriver для браузера: " + browser);
        System.out.println("🔧 WebDriverUtils: Headless режим: " + headless);
        System.out.println("🔧 WebDriverUtils: Системное свойство ui.browser: " + System.getProperty("ui.browser"));
        System.out.println("🔧 WebDriverUtils: Переменная окружения UI_BROWSER: " + System.getenv("UI_BROWSER"));
        
        switch (browser) {
            case "chrome":
                System.out.println("🔧 WebDriverUtils: Создаем Chrome WebDriver");
                return createChromeDriver(headless);
            case "firefox":
                System.out.println("🔧 WebDriverUtils: Создаем Firefox WebDriver");
                return createFirefoxDriver(headless);
            case "edge":
                System.out.println("🔧 WebDriverUtils: Создаем Edge WebDriver");
                return createEdgeDriver(headless);
            default:
                System.out.println("🔧 WebDriverUtils: Неизвестный браузер '" + browser + "', используем Chrome по умолчанию");
                return createChromeDriver(headless);
        }
    }

    /**
     * Создать WebDriver с настройками по умолчанию
     */
    public static WebDriver createWebDriver() {
        return createWebDriver(false);
    }

    /**
     * Создать Chrome WebDriver
     */
    private static WebDriver createChromeDriver(boolean headless) {
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        if (headless) {
            options.addArguments("--headless");
        }
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-logging");
        options.addArguments("--disable-web-security");
        options.addArguments("--allow-running-insecure-content");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--ignore-ssl-errors");
        options.addArguments("--ignore-certificate-errors-spki-list");
        options.addArguments("--ignore-certificate-errors-spki-list");
        options.addArguments("--ignore-ssl-errors");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--allow-running-insecure-content");
        options.addArguments("--disable-web-security");
        options.addArguments("--disable-features=VizDisplayCompositor");
        
        // Дополнительные аргументы для стабильности в CI/CD
        options.addArguments("--disable-background-timer-throttling");
        options.addArguments("--disable-backgrounding-occluded-windows");
        options.addArguments("--disable-renderer-backgrounding");
        options.addArguments("--disable-features=TranslateUI");
        options.addArguments("--disable-ipc-flooding-protection");
        options.addArguments("--remote-debugging-port=0");
        options.addArguments("--disable-background-networking");
        options.addArguments("--disable-sync");
        options.addArguments("--disable-default-apps");
        options.addArguments("--disable-component-extensions-with-background-pages");
        options.addArguments("--disable-background-mode");
        options.addArguments("--disable-client-side-phishing-detection");
        options.addArguments("--disable-hang-monitor");
        options.addArguments("--disable-prompt-on-repost");
        options.addArguments("--disable-domain-reliability");
        options.addArguments("--disable-features=VizDisplayCompositor,AudioServiceOutOfProcess");
        options.addArguments("--force-color-profile=srgb");
        options.addArguments("--metrics-recording-only");
        options.addArguments("--no-first-run");
        options.addArguments("--safebrowsing-disable-auto-update");
        options.addArguments("--enable-automation");
        options.addArguments("--password-store=basic");
        options.addArguments("--use-mock-keychain");
        
        // Отключаем логи
        System.setProperty("webdriver.chrome.silentOutput", "true");
        System.setProperty("org.slf4j.simpleLogger.log.org.openqa.selenium", "ERROR");
        
        return new ChromeDriver(options);
    }

    /**
     * Создать Firefox WebDriver
     */
    private static WebDriver createFirefoxDriver(boolean headless) {
        WebDriverManager.firefoxdriver().setup();
        
        FirefoxOptions options = new FirefoxOptions();
        if (headless) {
            options.addArguments("--headless");
        }
        options.addArguments("--width=1920");
        options.addArguments("--height=1080");
        
        // Дополнительные аргументы для стабильности в CI/CD
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-logging");
        options.addArguments("--disable-web-security");
        options.addArguments("--allow-running-insecure-content");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--ignore-ssl-errors");
        options.addArguments("--disable-background-timer-throttling");
        options.addArguments("--disable-backgrounding-occluded-windows");
        options.addArguments("--disable-renderer-backgrounding");
        options.addArguments("--disable-features=TranslateUI");
        options.addArguments("--disable-ipc-flooding-protection");
        options.addArguments("--disable-background-networking");
        options.addArguments("--disable-sync");
        options.addArguments("--disable-default-apps");
        options.addArguments("--disable-background-mode");
        options.addArguments("--disable-hang-monitor");
        options.addArguments("--disable-prompt-on-repost");
        options.addArguments("--disable-domain-reliability");
        options.addArguments("--no-first-run");
        options.addArguments("--enable-automation");
        
        // Отключаем логи
        System.setProperty("webdriver.firefox.silentOutput", "true");
        System.setProperty("org.slf4j.simpleLogger.log.org.openqa.selenium", "ERROR");
        
        return new FirefoxDriver(options);
    }

    /**
     * Создать Edge WebDriver
     */
    private static WebDriver createEdgeDriver(boolean headless) {
        WebDriverManager.edgedriver().setup();
        
        EdgeOptions options = new EdgeOptions();
        if (headless) {
            options.addArguments("--headless");
        }
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");
        
        return new EdgeDriver(options);
    }

    /**
     * Создать WebDriverWait
     */
    public static WebDriverWait createWebDriverWait(WebDriver driver) {
        return createWebDriverWait(driver, Config.getTimeout());
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
     * Выполнить JavaScript
     */
    public static Object executeJavaScript(WebDriver driver, String script, Object... args) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return js.executeScript(script, args);
    }

    /**
     * Прокрутить вниз
     */
    public static void scrollDown(WebDriver driver) {
        executeJavaScript(driver, "window.scrollTo(0, document.body.scrollHeight);");
    }

    /**
     * Прокрутить вверх
     */
    public static void scrollUp(WebDriver driver) {
        executeJavaScript(driver, "window.scrollTo(0, 0);");
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
     * Очистить cookies
     */
    public static void clearCookies(WebDriver driver) {
        driver.manage().deleteAllCookies();
    }

    /**
     * Ждать элемент
     */
    public static void waitForElementVisible(WebDriver driver, By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Ждать элемент (по умолчанию)
     */
    public static void waitForElementVisible(WebDriver driver, By locator) {
        waitForElementVisible(driver, locator, Config.getTimeout());
    }

    /**
     * Ждать кликабельность элемента
     */
    public static void waitForElementClickable(WebDriver driver, By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Ждать кликабельность элемента (по умолчанию)
     */
    public static void waitForElementClickable(WebDriver driver, By locator) {
        waitForElementClickable(driver, locator, Config.getTimeout());
    }

    /**
     * Ждать загрузки страницы
     */
    public static void waitForPageLoad(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Config.getPageLoadTimeout()));
        wait.until(webDriver -> executeJavaScript(webDriver, "return document.readyState").equals("complete"));
    }

    /**
     * Получить информацию о браузере
     */
    public static Map<String, Object> getBrowserInfo(WebDriver driver) {
        Map<String, Object> info = new HashMap<>();
        info.put("browser", driver.getClass().getSimpleName());
        info.put("title", driver.getTitle());
        info.put("url", driver.getCurrentUrl());
        info.put("windowSize", driver.manage().window().getSize());
        return info;
    }

    /**
     * Ждать URL содержит текст
     */
    public static void waitForUrlContains(WebDriver driver, String urlPart, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.urlContains(urlPart));
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
}