package utils;

import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Утилиты для улучшенных Allure отчетов
 */
public class AllureReportUtils {
    
    private static final Map<String, TestMetrics> testMetrics = new ConcurrentHashMap<>();
    private static final Map<String, List<String>> testSteps = new ConcurrentHashMap<>();
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    
    // ==================== МЕТРИКИ ТЕСТОВ ====================
    
    /**
     * Класс для хранения метрик теста
     */
    public static class TestMetrics {
        private final String testName;
        private final long startTime;
        private long endTime;
        private Status status;
        private final List<String> errors = new ArrayList<>();
        private final Map<String, Object> customMetrics = new HashMap<>();
        
        public TestMetrics(String testName) {
            this.testName = testName;
            this.startTime = System.currentTimeMillis();
            this.status = Status.PASSED;
        }
        
        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }
        
        public void setStatus(Status status) {
            this.status = status;
        }
        
        public void addError(String error) {
            this.errors.add(error);
            this.status = Status.FAILED;
        }
        
        public void addCustomMetric(String key, Object value) {
            this.customMetrics.put(key, value);
        }
        
        public long getDuration() {
            return endTime - startTime;
        }
        
        public String getTestName() { return testName; }
        public long getStartTime() { return startTime; }
        public long getEndTime() { return endTime; }
        public Status getStatus() { return status; }
        public List<String> getErrors() { return new ArrayList<>(errors); }
        public Map<String, Object> getCustomMetrics() { return new HashMap<>(customMetrics); }
    }
    
    // ==================== УПРАВЛЕНИЕ МЕТРИКАМИ ====================
    
    /**
     * Начинает отслеживание теста
     */
    public static void startTest(String testName) {
        TestMetrics metrics = new TestMetrics(testName);
        testMetrics.put(testName, metrics);
        testSteps.put(testName, new ArrayList<>());
        
        Allure.addAttachment("Test Start", "text/plain", 
            String.format("Test: %s\nStart Time: %s", testName, getCurrentTimestamp()));
    }
    
    /**
     * Завершает отслеживание теста
     */
    public static void endTest(String testName, Status status) {
        TestMetrics metrics = testMetrics.get(testName);
        if (metrics != null) {
            metrics.setEndTime(System.currentTimeMillis());
            metrics.setStatus(status);
            
            // Прикрепляем финальные метрики
            attachTestMetrics(metrics);
        }
    }
    
    /**
     * Добавляет ошибку к тесту
     */
    public static void addTestError(String testName, String error) {
        TestMetrics metrics = testMetrics.get(testName);
        if (metrics != null) {
            metrics.addError(error);
        }
    }
    
    /**
     * Добавляет кастомную метрику к тесту
     */
    public static void addCustomMetric(String testName, String key, Object value) {
        TestMetrics metrics = testMetrics.get(testName);
        if (metrics != null) {
            metrics.addCustomMetric(key, value);
        }
    }
    
    // ==================== УПРАВЛЕНИЕ ШАГАМИ ====================
    
    /**
     * Добавляет шаг к тесту
     */
    public static void addTestStep(String testName, String stepName, String stepDescription) {
        List<String> steps = testSteps.get(testName);
        if (steps != null) {
            String stepInfo = String.format("[%s] %s: %s", getCurrentTimestamp(), stepName, stepDescription);
            steps.add(stepInfo);
            
            Allure.addAttachment("Test Step", "text/plain", stepInfo);
        }
    }
    
    /**
     * Получает все шаги теста
     */
    public static List<String> getTestSteps(String testName) {
        return testSteps.getOrDefault(testName, new ArrayList<>());
    }
    
    // ==================== ПРИКРЕПЛЕНИЕ МЕТРИК ====================
    
    /**
     * Прикрепляет метрики теста к отчету
     */
    private static void attachTestMetrics(TestMetrics metrics) {
        StringBuilder metricsInfo = new StringBuilder();
        metricsInfo.append("=== TEST METRICS ===\n");
        metricsInfo.append("Test Name: ").append(metrics.getTestName()).append("\n");
        metricsInfo.append("Status: ").append(metrics.getStatus()).append("\n");
        metricsInfo.append("Duration: ").append(metrics.getDuration()).append(" ms\n");
        metricsInfo.append("Start Time: ").append(formatTimestamp(metrics.getStartTime())).append("\n");
        metricsInfo.append("End Time: ").append(formatTimestamp(metrics.getEndTime())).append("\n");
        
        if (!metrics.getErrors().isEmpty()) {
            metricsInfo.append("Errors:\n");
            for (String error : metrics.getErrors()) {
                metricsInfo.append("  - ").append(error).append("\n");
            }
        }
        
        if (!metrics.getCustomMetrics().isEmpty()) {
            metricsInfo.append("Custom Metrics:\n");
            for (Map.Entry<String, Object> entry : metrics.getCustomMetrics().entrySet()) {
                metricsInfo.append("  ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
        }
        
        Allure.addAttachment("Test Metrics", "text/plain", metricsInfo.toString());
    }
    
    // ==================== СТАТИСТИКА ТЕСТОВ ====================
    
    /**
     * Генерирует статистику по всем тестам
     */
    public static void generateTestStatistics() {
        StringBuilder statistics = new StringBuilder();
        statistics.append("=== TEST STATISTICS ===\n");
        statistics.append("Total Tests: ").append(testMetrics.size()).append("\n");
        
        long totalDuration = 0;
        int passedTests = 0;
        int failedTests = 0;
        int skippedTests = 0;
        
        for (TestMetrics metrics : testMetrics.values()) {
            totalDuration += metrics.getDuration();
            
            switch (metrics.getStatus()) {
                case PASSED:
                    passedTests++;
                    break;
                case FAILED:
                    failedTests++;
                    break;
                case SKIPPED:
                    skippedTests++;
                    break;
                case BROKEN:
                    failedTests++;
                    break;
            }
        }
        
        statistics.append("Passed: ").append(passedTests).append("\n");
        statistics.append("Failed: ").append(failedTests).append("\n");
        statistics.append("Skipped: ").append(skippedTests).append("\n");
        statistics.append("Total Duration: ").append(totalDuration).append(" ms\n");
        statistics.append("Average Duration: ").append(testMetrics.size() > 0 ? totalDuration / testMetrics.size() : 0).append(" ms\n");
        
        Allure.addAttachment("Test Statistics", "text/plain", statistics.toString());
    }
    
    // ==================== УПРАВЛЕНИЕ ОКРУЖЕНИЕМ ====================
    
    /**
     * Прикрепляет информацию об окружении
     */
    public static void attachEnvironmentInfo() {
        StringBuilder envInfo = new StringBuilder();
        envInfo.append("=== ENVIRONMENT INFO ===\n");
        envInfo.append("Java Version: ").append(System.getProperty("java.version")).append("\n");
        envInfo.append("OS Name: ").append(System.getProperty("os.name")).append("\n");
        envInfo.append("OS Version: ").append(System.getProperty("os.version")).append("\n");
        envInfo.append("OS Architecture: ").append(System.getProperty("os.arch")).append("\n");
        envInfo.append("User Name: ").append(System.getProperty("user.name")).append("\n");
        envInfo.append("User Directory: ").append(System.getProperty("user.dir")).append("\n");
        envInfo.append("Available Processors: ").append(Runtime.getRuntime().availableProcessors()).append("\n");
        envInfo.append("Total Memory: ").append(Runtime.getRuntime().totalMemory() / 1024 / 1024).append(" MB\n");
        envInfo.append("Free Memory: ").append(Runtime.getRuntime().freeMemory() / 1024 / 1024).append(" MB\n");
        envInfo.append("Max Memory: ").append(Runtime.getRuntime().maxMemory() / 1024 / 1024).append(" MB\n");
        
        Allure.addAttachment("Environment Info", "text/plain", envInfo.toString());
    }
    
    /**
     * Прикрепляет информацию о конфигурации
     */
    public static void attachConfigurationInfo() {
        StringBuilder configInfo = new StringBuilder();
        configInfo.append("=== CONFIGURATION INFO ===\n");
        configInfo.append("Maven Version: ").append(System.getProperty("maven.version", "Unknown")).append("\n");
        configInfo.append("Project Version: ").append(System.getProperty("project.version", "Unknown")).append("\n");
        configInfo.append("Build Timestamp: ").append(getCurrentTimestamp()).append("\n");
        configInfo.append("Test Environment: ").append(System.getProperty("test.environment", "Local")).append("\n");
        configInfo.append("Headless Mode: ").append(System.getProperty("ui.headless", "false")).append("\n");
        configInfo.append("API Base URL: ").append(System.getProperty("api.base.url", "https://jsonplaceholder.typicode.com")).append("\n");
        
        Allure.addAttachment("Configuration Info", "text/plain", configInfo.toString());
    }
    
    // ==================== УПРАВЛЕНИЕ ДАННЫМИ ====================
    
    /**
     * Прикрепляет тестовые данные
     */
    public static void attachTestData(String dataName, String data) {
        Allure.addAttachment("Test Data: " + dataName, "text/plain", data);
    }
    
    /**
     * Прикрепляет JSON данные
     */
    public static void attachJsonData(String dataName, String jsonData) {
        Allure.addAttachment("JSON Data: " + dataName, "application/json", jsonData);
    }
    
    /**
     * Прикрепляет XML данные
     */
    public static void attachXmlData(String dataName, String xmlData) {
        Allure.addAttachment("XML Data: " + dataName, "application/xml", xmlData);
    }
    
    // ==================== УПРАВЛЕНИЕ ПРОИЗВОДИТЕЛЬНОСТЬЮ ====================
    
    /**
     * Прикрепляет метрики производительности
     */
    public static void attachPerformanceMetrics(String operation, long duration, Map<String, Object> additionalMetrics) {
        StringBuilder perfInfo = new StringBuilder();
        perfInfo.append("=== PERFORMANCE METRICS ===\n");
        perfInfo.append("Operation: ").append(operation).append("\n");
        perfInfo.append("Duration: ").append(duration).append(" ms\n");
        perfInfo.append("Timestamp: ").append(getCurrentTimestamp()).append("\n");
        
        if (additionalMetrics != null && !additionalMetrics.isEmpty()) {
            perfInfo.append("Additional Metrics:\n");
            for (Map.Entry<String, Object> entry : additionalMetrics.entrySet()) {
                perfInfo.append("  ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
        }
        
        Allure.addAttachment("Performance: " + operation, "text/plain", perfInfo.toString());
    }
    
    // ==================== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ====================
    
    /**
     * Получает текущий timestamp
     */
    private static String getCurrentTimestamp() {
        return LocalDateTime.now().format(TIMESTAMP_FORMATTER);
    }
    
    /**
     * Форматирует timestamp
     */
    private static String formatTimestamp(long timestamp) {
        return LocalDateTime.ofInstant(
            java.time.Instant.ofEpochMilli(timestamp),
            java.time.ZoneId.systemDefault()
        ).format(TIMESTAMP_FORMATTER);
    }
    
    /**
     * Очищает все метрики
     */
    public static void clearAllMetrics() {
        testMetrics.clear();
        testSteps.clear();
    }
    
    /**
     * Получает все метрики тестов
     */
    public static Map<String, TestMetrics> getAllTestMetrics() {
        return new HashMap<>(testMetrics);
    }
}
