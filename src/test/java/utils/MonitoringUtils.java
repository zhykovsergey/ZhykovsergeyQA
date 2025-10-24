package utils;

import io.qameta.allure.Allure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.ThreadMXBean;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Утилиты для мониторинга и сбора метрик
 */
public class MonitoringUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(MonitoringUtils.class);
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    
    // ==================== МЕТРИКИ ====================
    
    private static final Map<String, AtomicLong> counters = new ConcurrentHashMap<>();
    private static final Map<String, Long> timers = new ConcurrentHashMap<>();
    private static final Map<String, Object> gauges = new ConcurrentHashMap<>();
    
    // ==================== СЧЕТЧИКИ ====================
    
    /**
     * Увеличивает счетчик
     */
    public static void incrementCounter(String name) {
        counters.computeIfAbsent(name, k -> new AtomicLong(0)).incrementAndGet();
        logger.debug("Counter '{}' incremented to {}", name, counters.get(name).get());
    }
    
    /**
     * Увеличивает счетчик на указанное значение
     */
    public static void incrementCounter(String name, long value) {
        counters.computeIfAbsent(name, k -> new AtomicLong(0)).addAndGet(value);
        logger.debug("Counter '{}' incremented by {} to {}", name, value, counters.get(name).get());
    }
    
    /**
     * Получает значение счетчика
     */
    public static long getCounterValue(String name) {
        return counters.getOrDefault(name, new AtomicLong(0)).get();
    }
    
    /**
     * Сбрасывает счетчик
     */
    public static void resetCounter(String name) {
        counters.remove(name);
        logger.debug("Counter '{}' reset", name);
    }
    
    // ==================== ТАЙМЕРЫ ====================
    
    /**
     * Начинает таймер
     */
    public static void startTimer(String name) {
        timers.put(name + "_start", System.currentTimeMillis());
        logger.debug("Timer '{}' started", name);
    }
    
    /**
     * Останавливает таймер и возвращает продолжительность
     */
    public static long stopTimer(String name) {
        Long startTime = timers.get(name + "_start");
        if (startTime == null) {
            logger.warn("Timer '{}' was not started", name);
            return 0;
        }
        
        long duration = System.currentTimeMillis() - startTime;
        timers.put(name + "_duration", duration);
        timers.remove(name + "_start");
        
        logger.debug("Timer '{}' stopped. Duration: {} ms", name, duration);
        return duration;
    }
    
    /**
     * Получает продолжительность таймера
     */
    public static long getTimerDuration(String name) {
        return timers.getOrDefault(name + "_duration", 0L);
    }
    
    /**
     * Сбрасывает таймер
     */
    public static void resetTimer(String name) {
        timers.remove(name + "_start");
        timers.remove(name + "_duration");
        logger.debug("Timer '{}' reset", name);
    }
    
    // ==================== ГЕЙДЖИ ====================
    
    /**
     * Устанавливает значение гейджа
     */
    public static void setGauge(String name, Object value) {
        gauges.put(name, value);
        logger.debug("Gauge '{}' set to {}", name, value);
    }
    
    /**
     * Получает значение гейджа
     */
    public static Object getGaugeValue(String name) {
        return gauges.get(name);
    }
    
    /**
     * Сбрасывает гейдж
     */
    public static void resetGauge(String name) {
        gauges.remove(name);
        logger.debug("Gauge '{}' reset", name);
    }
    
    // ==================== СИСТЕМНЫЕ МЕТРИКИ ====================
    
    /**
     * Собирает системные метрики
     */
    public static Map<String, Object> collectSystemMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        // Метрики памяти
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        metrics.put("heap.used", memoryBean.getHeapMemoryUsage().getUsed());
        metrics.put("heap.max", memoryBean.getHeapMemoryUsage().getMax());
        metrics.put("nonheap.used", memoryBean.getNonHeapMemoryUsage().getUsed());
        metrics.put("nonheap.max", memoryBean.getNonHeapMemoryUsage().getMax());
        
        // Метрики потоков
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        metrics.put("threads.count", threadBean.getThreadCount());
        metrics.put("threads.daemon", threadBean.getDaemonThreadCount());
        metrics.put("threads.peak", threadBean.getPeakThreadCount());
        
        // Метрики времени выполнения
        metrics.put("uptime", ManagementFactory.getRuntimeMXBean().getUptime());
        
        // Метрики процессора
        metrics.put("processors", Runtime.getRuntime().availableProcessors());
        
        // Метрики времени
        metrics.put("timestamp", getCurrentTimestamp());
        
        return metrics;
    }
    
    /**
     * Собирает метрики JVM
     */
    public static Map<String, Object> collectJVMMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        Runtime runtime = Runtime.getRuntime();
        metrics.put("jvm.total.memory", runtime.totalMemory());
        metrics.put("jvm.free.memory", runtime.freeMemory());
        metrics.put("jvm.max.memory", runtime.maxMemory());
        metrics.put("jvm.used.memory", runtime.totalMemory() - runtime.freeMemory());
        
        // Метрики сборщика мусора
        ManagementFactory.getGarbageCollectorMXBeans().forEach(gc -> {
            metrics.put("gc." + gc.getName() + ".count", gc.getCollectionCount());
            metrics.put("gc." + gc.getName() + ".time", gc.getCollectionTime());
        });
        
        return metrics;
    }
    
    // ==================== МЕТРИКИ ТЕСТОВ ====================
    
    /**
     * Собирает метрики тестов
     */
    public static Map<String, Object> collectTestMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        // Счетчики тестов
        metrics.put("tests.total", getCounterValue("tests.total"));
        metrics.put("tests.passed", getCounterValue("tests.passed"));
        metrics.put("tests.failed", getCounterValue("tests.failed"));
        metrics.put("tests.skipped", getCounterValue("tests.skipped"));
        
        // Таймеры тестов
        metrics.put("test.execution.time", getTimerDuration("test.execution"));
        metrics.put("test.setup.time", getTimerDuration("test.setup"));
        metrics.put("test.teardown.time", getTimerDuration("test.teardown"));
        
        // Гейджи тестов
        metrics.put("test.success.rate", getGaugeValue("test.success.rate"));
        metrics.put("test.failure.rate", getGaugeValue("test.failure.rate"));
        
        return metrics;
    }
    
    // ==================== МЕТРИКИ ПРОИЗВОДИТЕЛЬНОСТИ ====================
    
    /**
     * Собирает метрики производительности
     */
    public static Map<String, Object> collectPerformanceMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        // Метрики API
        metrics.put("api.requests.total", getCounterValue("api.requests.total"));
        metrics.put("api.requests.success", getCounterValue("api.requests.success"));
        metrics.put("api.requests.failed", getCounterValue("api.requests.failed"));
        metrics.put("api.response.time.avg", getGaugeValue("api.response.time.avg"));
        metrics.put("api.response.time.max", getGaugeValue("api.response.time.max"));
        metrics.put("api.response.time.min", getGaugeValue("api.response.time.min"));
        
        // Метрики UI
        metrics.put("ui.actions.total", getCounterValue("ui.actions.total"));
        metrics.put("ui.actions.success", getCounterValue("ui.actions.success"));
        metrics.put("ui.actions.failed", getCounterValue("ui.actions.failed"));
        metrics.put("ui.page.load.time.avg", getGaugeValue("ui.page.load.time.avg"));
        metrics.put("ui.element.wait.time.avg", getGaugeValue("ui.element.wait.time.avg"));
        
        return metrics;
    }
    
    // ==================== ОТЧЕТЫ ====================
    
    /**
     * Генерирует отчет по метрикам
     */
    public static String generateMetricsReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== METRICS REPORT ===\n");
        report.append("Timestamp: ").append(getCurrentTimestamp()).append("\n\n");
        
        // Системные метрики
        report.append("--- SYSTEM METRICS ---\n");
        Map<String, Object> systemMetrics = collectSystemMetrics();
        for (Map.Entry<String, Object> entry : systemMetrics.entrySet()) {
            report.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        
        // Метрики JVM
        report.append("\n--- JVM METRICS ---\n");
        Map<String, Object> jvmMetrics = collectJVMMetrics();
        for (Map.Entry<String, Object> entry : jvmMetrics.entrySet()) {
            report.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        
        // Метрики тестов
        report.append("\n--- TEST METRICS ---\n");
        Map<String, Object> testMetrics = collectTestMetrics();
        for (Map.Entry<String, Object> entry : testMetrics.entrySet()) {
            report.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        
        // Метрики производительности
        report.append("\n--- PERFORMANCE METRICS ---\n");
        Map<String, Object> performanceMetrics = collectPerformanceMetrics();
        for (Map.Entry<String, Object> entry : performanceMetrics.entrySet()) {
            report.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        
        // Счетчики
        report.append("\n--- COUNTERS ---\n");
        for (Map.Entry<String, AtomicLong> entry : counters.entrySet()) {
            report.append(entry.getKey()).append(": ").append(entry.getValue().get()).append("\n");
        }
        
        // Таймеры
        report.append("\n--- TIMERS ---\n");
        for (Map.Entry<String, Long> entry : timers.entrySet()) {
            if (entry.getKey().endsWith("_duration")) {
                report.append(entry.getKey().replace("_duration", "")).append(": ").append(entry.getValue()).append(" ms\n");
            }
        }
        
        // Гейджи
        report.append("\n--- GAUGES ---\n");
        for (Map.Entry<String, Object> entry : gauges.entrySet()) {
            report.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        
        return report.toString();
    }
    
    /**
     * Прикрепляет отчет по метрикам к Allure
     */
    public static void attachMetricsReport() {
        String report = generateMetricsReport();
        Allure.addAttachment("Metrics Report", "text/plain", report);
        logger.info("Metrics report attached to Allure");
    }
    
    // ==================== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ====================
    
    /**
     * Получает текущий timestamp
     */
    private static String getCurrentTimestamp() {
        return LocalDateTime.now().format(TIMESTAMP_FORMATTER);
    }
    
    /**
     * Очищает все метрики
     */
    public static void clearAllMetrics() {
        counters.clear();
        timers.clear();
        gauges.clear();
        logger.info("All metrics cleared");
    }
    
    /**
     * Экспортирует метрики в JSON
     */
    public static String exportMetricsToJson() {
        Map<String, Object> allMetrics = new HashMap<>();
        
        // Счетчики
        Map<String, Long> countersMap = new HashMap<>();
        for (Map.Entry<String, AtomicLong> entry : counters.entrySet()) {
            countersMap.put(entry.getKey(), entry.getValue().get());
        }
        allMetrics.put("counters", countersMap);
        
        // Таймеры
        Map<String, Long> timersMap = new HashMap<>();
        for (Map.Entry<String, Long> entry : timers.entrySet()) {
            if (entry.getKey().endsWith("_duration")) {
                timersMap.put(entry.getKey().replace("_duration", ""), entry.getValue());
            }
        }
        allMetrics.put("timers", timersMap);
        
        // Гейджи
        allMetrics.put("gauges", new HashMap<>(gauges));
        
        // Системные метрики
        allMetrics.put("system", collectSystemMetrics());
        
        // Метрики JVM
        allMetrics.put("jvm", collectJVMMetrics());
        
        // Метрики тестов
        allMetrics.put("tests", collectTestMetrics());
        
        // Метрики производительности
        allMetrics.put("performance", collectPerformanceMetrics());
        
        return convertToJson(allMetrics);
    }
    
    /**
     * Конвертирует объект в JSON строку
     */
    private static String convertToJson(Map<String, Object> data) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        
        boolean first = true;
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (!first) {
                json.append(",\n");
            }
            json.append("  \"").append(entry.getKey()).append("\": ");
            
            Object value = entry.getValue();
            if (value instanceof String) {
                json.append("\"").append(value).append("\"");
            } else if (value instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> mapValue = (Map<String, Object>) value;
                json.append(convertToJson(mapValue));
            } else {
                json.append(value);
            }
            
            first = false;
        }
        
        json.append("\n}");
        return json.toString();
    }
}
