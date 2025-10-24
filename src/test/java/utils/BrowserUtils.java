package utils;

import io.qameta.allure.Allure;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Утилиты для работы с разными браузерами
 */
public class BrowserUtils {
    
    public enum BrowserType {
        CHROME, FIREFOX, EDGE, SAFARI, REMOTE
    }
    
    // ==================== СОЗДАНИЕ ДРАЙВЕРОВ ====================
    
    /**
     * Создает WebDriver для указанного браузера
     */
    public static WebDriver createWebDriver(BrowserType browserType, boolean headless) {
        WebDriver driver = null;
        
        try {
            switch (browserType) {
                case CHROME:
                    driver = createChromeDriver(headless);
                    break;
                case FIREFOX:
                    driver = createFirefoxDriver(headless);
                    break;
                case EDGE:
                    driver = createEdgeDriver(headless);
                    break;
                case SAFARI:
                    driver = createSafariDriver(headless);
                    break;
                case REMOTE:
                    driver = createRemoteDriver(headless);
                    break;
                default:
                    throw new IllegalArgumentException("Неподдерживаемый тип браузера: " + browserType);
            }
            
            // Настраиваем таймауты
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
            driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));
            
            // Логируем создание драйвера
            LoggerUtils.logAction("WebDriver создан", "Browser: " + browserType + ", Headless: " + headless);
            Allure.addAttachment("Browser Info", "text/plain", 
                String.format("Browser: %s\nHeadless: %s\nDriver: %s", 
                    browserType, headless, driver.getClass().getSimpleName()));
            
            return driver;
            
        } catch (Exception e) {
            LoggerUtils.logError("Ошибка создания WebDriver для " + browserType, e);
            throw new RuntimeException("Не удалось создать WebDriver для " + browserType, e);
        }
    }
    
    // ==================== CHROME DRIVER ====================
    
    /**
     * Создает Chrome драйвер
     */
    private static WebDriver createChromeDriver(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        
        // Базовые настройки
        if (headless) {
            options.addArguments("--headless");
        }
        
        // Настройки для стабильности
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-plugins");
        options.addArguments("--disable-images");
        options.addArguments("--disable-javascript");
        options.addArguments("--disable-web-security");
        options.addArguments("--allow-running-insecure-content");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--ignore-ssl-errors");
        options.addArguments("--ignore-certificate-errors-spki-list");
        options.addArguments("--ignore-certificate-errors-ssl");
        
        // Настройки для отключения уведомлений
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-default-apps");
        options.addArguments("--disable-save-password-bubble");
        options.addArguments("--disable-single-click-autofill");
        options.addArguments("--disable-autofill-keyboard-accessory-view");
        options.addArguments("--disable-autofill-credit-card-assistant");
        
        // Настройки окна
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--start-maximized");
        
        // Настройки пользователя
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        
        // Настройки для тестирования
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--disable-features=VizDisplayCompositor");
        
        // Экспериментальные настройки
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);
        
        // Настройки для отключения логирования
        options.addArguments("--log-level=3");
        options.addArguments("--silent");
        
        return new ChromeDriver(options);
    }
    
    // ==================== FIREFOX DRIVER ====================
    
    /**
     * Создает Firefox драйвер
     */
    private static WebDriver createFirefoxDriver(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        
        // Базовые настройки
        if (headless) {
            options.addArguments("--headless");
        }
        
        // Настройки для стабильности
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-plugins");
        options.addArguments("--disable-images");
        options.addArguments("--disable-javascript");
        options.addArguments("--disable-web-security");
        options.addArguments("--allow-running-insecure-content");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--ignore-ssl-errors");
        
        // Настройки для отключения уведомлений
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-default-apps");
        
        // Настройки окна
        options.addArguments("--width=1920");
        options.addArguments("--height=1080");
        
        // Настройки пользователя
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:89.0) Gecko/20100101 Firefox/89.0");
        
        // Настройки для тестирования
        options.addArguments("--disable-blink-features=AutomationControlled");
        
        // Настройки для отключения логирования
        options.addArguments("--log-level=3");
        options.addArguments("--silent");
        
        return new FirefoxDriver(options);
    }
    
    // ==================== EDGE DRIVER ====================
    
    /**
     * Создает Edge драйвер
     */
    private static WebDriver createEdgeDriver(boolean headless) {
        EdgeOptions options = new EdgeOptions();
        
        // Базовые настройки
        if (headless) {
            options.addArguments("--headless");
        }
        
        // Настройки для стабильности
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-plugins");
        options.addArguments("--disable-images");
        options.addArguments("--disable-javascript");
        options.addArguments("--disable-web-security");
        options.addArguments("--allow-running-insecure-content");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--ignore-ssl-errors");
        
        // Настройки для отключения уведомлений
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-default-apps");
        options.addArguments("--disable-save-password-bubble");
        options.addArguments("--disable-single-click-autofill");
        options.addArguments("--disable-autofill-keyboard-accessory-view");
        options.addArguments("--disable-autofill-credit-card-assistant");
        
        // Настройки окна
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--start-maximized");
        
        // Настройки пользователя
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36 Edg/91.0.864.59");
        
        // Настройки для тестирования
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--disable-features=VizDisplayCompositor");
        
        // Экспериментальные настройки
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);
        
        // Настройки для отключения логирования
        options.addArguments("--log-level=3");
        options.addArguments("--silent");
        
        return new EdgeDriver(options);
    }
    
    // ==================== SAFARI DRIVER ====================
    
    /**
     * Создает Safari драйвер
     */
    private static WebDriver createSafariDriver(boolean headless) {
        SafariOptions options = new SafariOptions();
        
        // Safari не поддерживает headless режим
        if (headless) {
            LoggerUtils.logAction("Safari не поддерживает headless режим", "Используется обычный режим");
        }
        
        // Настройки для стабильности
        options.setAutomaticInspection(false);
        options.setAutomaticProfiling(false);
        
        return new SafariDriver(options);
    }
    
    // ==================== REMOTE DRIVER ====================
    
    /**
     * Создает Remote драйвер
     */
    private static WebDriver createRemoteDriver(boolean headless) {
        try {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            
            // Настройки для Chrome
            capabilities.setBrowserName("chrome");
            capabilities.setVersion("latest");
            capabilities.setCapability("platform", "ANY");
            capabilities.setCapability("headless", headless);
            
            // Настройки для стабильности
            capabilities.setCapability("no-sandbox", true);
            capabilities.setCapability("disable-dev-shm-usage", true);
            capabilities.setCapability("disable-gpu", true);
            capabilities.setCapability("disable-extensions", true);
            capabilities.setCapability("disable-plugins", true);
            capabilities.setCapability("disable-images", true);
            capabilities.setCapability("disable-javascript", true);
            capabilities.setCapability("disable-web-security", true);
            capabilities.setCapability("allow-running-insecure-content", true);
            capabilities.setCapability("ignore-certificate-errors", true);
            capabilities.setCapability("ignore-ssl-errors", true);
            
            // Настройки для отключения уведомлений
            capabilities.setCapability("disable-notifications", true);
            capabilities.setCapability("disable-popup-blocking", true);
            capabilities.setCapability("disable-default-apps", true);
            capabilities.setCapability("disable-save-password-bubble", true);
            capabilities.setCapability("disable-single-click-autofill", true);
            capabilities.setCapability("disable-autofill-keyboard-accessory-view", true);
            capabilities.setCapability("disable-autofill-credit-card-assistant", true);
            
            // Настройки окна
            capabilities.setCapability("window-size", "1920,1080");
            capabilities.setCapability("start-maximized", true);
            
            // Настройки пользователя
            capabilities.setCapability("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
            
            // Настройки для тестирования
            capabilities.setCapability("disable-blink-features", "AutomationControlled");
            capabilities.setCapability("disable-features", "VizDisplayCompositor");
            
            // Экспериментальные настройки
            Map<String, Object> experimentalOptions = new HashMap<>();
            experimentalOptions.put("excludeSwitches", new String[]{"enable-automation"});
            experimentalOptions.put("useAutomationExtension", false);
            capabilities.setCapability("goog:chromeOptions", experimentalOptions);
            
            // Настройки для отключения логирования
            capabilities.setCapability("log-level", 3);
            capabilities.setCapability("silent", true);
            
            // URL для Remote WebDriver (можно настроить)
            String remoteUrl = System.getProperty("remote.webdriver.url", "http://localhost:4444/wd/hub");
            
            return new RemoteWebDriver(new URL(remoteUrl), capabilities);
            
        } catch (MalformedURLException e) {
            LoggerUtils.logError("Ошибка создания Remote WebDriver", e);
            throw new RuntimeException("Не удалось создать Remote WebDriver", e);
        }
    }
    
    // ==================== УТИЛИТЫ ====================
    
    /**
     * Получает тип браузера из системных свойств
     */
    public static BrowserType getBrowserTypeFromProperties() {
        String browserType = System.getProperty("browser.type", "chrome").toUpperCase();
        try {
            return BrowserType.valueOf(browserType);
        } catch (IllegalArgumentException e) {
            LoggerUtils.logError("Неподдерживаемый тип браузера: " + browserType, e);
            return BrowserType.CHROME; // По умолчанию Chrome
        }
    }
    
    /**
     * Получает режим headless из системных свойств
     */
    public static boolean getHeadlessModeFromProperties() {
        return Boolean.parseBoolean(System.getProperty("ui.headless", "false"));
    }
    
    /**
     * Создает WebDriver с настройками из системных свойств
     */
    public static WebDriver createWebDriverFromProperties() {
        BrowserType browserType = getBrowserTypeFromProperties();
        boolean headless = getHeadlessModeFromProperties();
        
        return createWebDriver(browserType, headless);
    }
    
    /**
     * Получает информацию о браузере
     */
    public static String getBrowserInfo(WebDriver driver) {
        StringBuilder info = new StringBuilder();
        info.append("Browser: ").append(driver.getClass().getSimpleName()).append("\n");
        info.append("Version: ").append("Unknown").append("\n");
        info.append("Platform: ").append("Unknown").append("\n");
        info.append("Window Size: ").append(driver.manage().window().getSize()).append("\n");
        info.append("Current URL: ").append(driver.getCurrentUrl()).append("\n");
        info.append("Page Title: ").append(driver.getTitle()).append("\n");
        
        return info.toString();
    }
}
