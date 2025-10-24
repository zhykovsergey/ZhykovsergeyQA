package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Расширенная утилита для работы с конфигурацией
 * Поддерживает системные свойства, переменные окружения и файлы конфигурации
 */
public class Config {
    private static Properties properties = new Properties();
    
    static {
        loadConfig();
    }
    
    private static void loadConfig() {
        try (InputStream input = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException e) {
            System.err.println("Ошибка загрузки конфигурации: " + e.getMessage());
        }
    }
    
    // ==================== API НАСТРОЙКИ ====================
    
    public static String getBaseUrl() {
        return getProperty("api.base.url", "https://jsonplaceholder.typicode.com");
    }
    
    public static int getConnectionTimeout() {
        return getIntProperty("api.connection.timeout", 30000);
    }
    
    public static int getSocketTimeout() {
        return getIntProperty("api.socket.timeout", 30000);
    }
    
    public static int getRetryCount() {
        return getIntProperty("api.retry.count", 3);
    }
    
    // ==================== UI НАСТРОЙКИ ====================
    
    public static boolean isHeadless() {
        return getBooleanProperty("ui.headless", false);
    }
    
    public static int getTimeout() {
        return getIntProperty("ui.timeout", 10);
    }
    
    public static int getPageLoadTimeout() {
        return getIntProperty("ui.page.load.timeout", 30);
    }
    
    public static String getBrowser() {
        return getProperty("ui.browser", "chrome");
    }
    
    public static String getWindowSize() {
        return getProperty("ui.window.size", "1920,1080");
    }
    
    public static boolean isScreenshotOnFailure() {
        return getBooleanProperty("ui.screenshot.on.failure", true);
    }
    
    // ==================== E2E НАСТРОЙКИ ====================
    
    public static String getSauceDemoUrl() {
        return getProperty("e2e.saucedemo.url", "https://www.saucedemo.com");
    }
    
    public static String getGoogleUrl() {
        return getProperty("e2e.google.url", "https://www.google.com");
    }
    
    public static String getYouTubeUrl() {
        return getProperty("e2e.youtube.url", "https://www.youtube.com");
    }
    
    // ==================== ALLURE НАСТРОЙКИ ====================
    
    public static String getAllureResultsDirectory() {
        return getProperty("allure.results.directory", "target/allure-results");
    }
    
    public static boolean isAllureEnabled() {
        return getBooleanProperty("allure.enabled", true);
    }
    
    // ==================== ОБЩИЕ НАСТРОЙКИ ====================
    
    public static String getEnvironment() {
        return getProperty("environment", "test");
    }
    
    public static boolean isDebugMode() {
        return getBooleanProperty("debug.mode", false);
    }
    
    public static int getThreadCount() {
        return getIntProperty("thread.count", 1);
    }
    
    // ==================== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ====================
    
    public static String get(String key) {
        return properties.getProperty(key);
    }
    
    public static String get(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Получить свойство с приоритетом: System Property > Environment Variable > Config File
     */
    private static String getProperty(String key, String defaultValue) {
        // 1. Проверяем системные свойства
        String systemProperty = System.getProperty(key);
        if (systemProperty != null) {
            return systemProperty;
        }
        
        // 2. Проверяем переменные окружения
        String envVariable = System.getenv(key.toUpperCase().replace(".", "_"));
        if (envVariable != null) {
            return envVariable;
        }
        
        // 3. Проверяем файл конфигурации
        return properties.getProperty(key, defaultValue);
    }
    
    private static int getIntProperty(String key, int defaultValue) {
        String value = getProperty(key, String.valueOf(defaultValue));
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            System.err.println("Неверное значение для " + key + ": " + value + ", используется значение по умолчанию: " + defaultValue);
            return defaultValue;
        }
    }
    
    private static boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = getProperty(key, String.valueOf(defaultValue));
        return Boolean.parseBoolean(value);
    }
    
    /**
     * Вывести все настройки (для отладки)
     */
    public static void printAllSettings() {
        System.out.println("=== КОНФИГУРАЦИЯ ПРОЕКТА ===");
        System.out.println("API Base URL: " + getBaseUrl());
        System.out.println("UI Headless: " + isHeadless());
        System.out.println("UI Timeout: " + getTimeout() + "s");
        System.out.println("Browser: " + getBrowser());
        System.out.println("Environment: " + getEnvironment());
        System.out.println("Debug Mode: " + isDebugMode());
        System.out.println("Thread Count: " + getThreadCount());
        System.out.println("========================");
    }
}
