package performance;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import utils.BaseApiTest;
import utils.TestTag;
import utils.AssertUtils;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;

/**
 * Тесты производительности API
 */
@Epic("Performance Testing")
@Feature("API Performance Tests")
public class PerformanceTest extends BaseApiTest {

    @Test
    @TestTag(id = "PERF_001", description = "Тест времени ответа API", category = "Performance", priority = 1)
    @Story("API Response Time")
    @DisplayName("Проверка времени ответа API")
    @Description("Проверяем, что API отвечает в приемлемое время")
    public void testApiResponseTime() {
        long startTime = System.currentTimeMillis();
        
        given()
            .when()
                .get("/posts")
            .then()
                .statusCode(200)
                .time(lessThan(2000L)); // Менее 2 секунд
        
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;
        
        AssertUtils.assertTrue(responseTime < 2000L, 
            "Время ответа API должно быть менее 2 секунд, фактическое: " + responseTime + "ms");
        
        // Прикрепляем метрики производительности
        Allure.addAttachment("Performance Metrics", "text/plain",
            String.format("Response Time: %d ms\nExpected: < 2000 ms\nStatus: %s",
                responseTime, responseTime < 2000L ? "PASS" : "FAIL"));
    }

    @Test
    @TestTag(id = "PERF_002", description = "Тест нагрузки API", category = "Performance", priority = 2)
    @Story("API Load Testing")
    @DisplayName("Проверка API под нагрузкой")
    @Description("Проверяем производительность API при множественных запросах")
    public void testApiLoadPerformance() {
        int numberOfRequests = 10;
        long totalTime = 0;
        long maxTime = 0;
        long minTime = Long.MAX_VALUE;
        
        for (int i = 0; i < numberOfRequests; i++) {
            long startTime = System.currentTimeMillis();
            
            given()
                .when()
                    .get("/posts/" + (i % 10 + 1))
                .then()
                    .statusCode(200);
            
            long endTime = System.currentTimeMillis();
            long requestTime = endTime - startTime;
            
            totalTime += requestTime;
            maxTime = Math.max(maxTime, requestTime);
            minTime = Math.min(minTime, requestTime);
        }
        
        long averageTime = totalTime / numberOfRequests;
        
        // Проверяем, что среднее время ответа приемлемое
        AssertUtils.assertTrue(averageTime < 1500L, 
            "Среднее время ответа должно быть менее 1.5 секунд, фактическое: " + averageTime + "ms");
        
        // Прикрепляем детальные метрики
        String metrics = String.format(
            "Load Test Results:\n" +
            "Number of Requests: %d\n" +
            "Total Time: %d ms\n" +
            "Average Time: %d ms\n" +
            "Max Time: %d ms\n" +
            "Min Time: %d ms\n" +
            "Requests per Second: %.2f",
            numberOfRequests, totalTime, averageTime, maxTime, minTime,
            (double) numberOfRequests / (totalTime / 1000.0)
        );
        
        Allure.addAttachment("Load Test Metrics", "text/plain", metrics);
    }

    @Test
    @TestTag(id = "PERF_003", description = "Тест памяти", category = "Performance", priority = 3)
    @Story("Memory Performance")
    @DisplayName("Проверка использования памяти")
    @Description("Проверяем использование памяти во время выполнения тестов")
    public void testMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        
        // Сбор мусора перед измерением
        System.gc();
        
        long initialMemory = runtime.totalMemory() - runtime.freeMemory();
        
        // Выполняем тестовые операции
        for (int i = 0; i < 100; i++) {
            given()
                .when()
                    .get("/posts/" + (i % 10 + 1))
                .then()
                    .statusCode(200);
        }
        
        long finalMemory = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = finalMemory - initialMemory;
        
        // Проверяем, что использование памяти разумное (менее 50MB)
        long maxMemoryUsage = 50 * 1024 * 1024; // 50MB
        AssertUtils.assertTrue(memoryUsed < maxMemoryUsage, 
            "Использование памяти должно быть менее 50MB, фактическое: " + (memoryUsed / 1024 / 1024) + "MB");
        
        // Прикрепляем метрики памяти
        String memoryMetrics = String.format(
            "Memory Usage Metrics:\n" +
            "Initial Memory: %d bytes (%.2f MB)\n" +
            "Final Memory: %d bytes (%.2f MB)\n" +
            "Memory Used: %d bytes (%.2f MB)\n" +
            "Max Available Memory: %d bytes (%.2f MB)\n" +
            "Free Memory: %d bytes (%.2f MB)",
            initialMemory, initialMemory / 1024.0 / 1024.0,
            finalMemory, finalMemory / 1024.0 / 1024.0,
            memoryUsed, memoryUsed / 1024.0 / 1024.0,
            runtime.maxMemory(), runtime.maxMemory() / 1024.0 / 1024.0,
            runtime.freeMemory(), runtime.freeMemory() / 1024.0 / 1024.0
        );
        
        Allure.addAttachment("Memory Metrics", "text/plain", memoryMetrics);
    }

    @Test
    @TestTag(id = "PERF_004", description = "Тест параллельных запросов", category = "Performance", priority = 2)
    @Story("Concurrent Requests")
    @DisplayName("Проверка API при параллельных запросах")
    @Description("Проверяем производительность API при одновременных запросах")
    public void testConcurrentRequests() {
        int numberOfThreads = 5;
        int requestsPerThread = 10;
        
        Thread[] threads = new Thread[numberOfThreads];
        long[] threadTimes = new long[numberOfThreads];
        
        for (int i = 0; i < numberOfThreads; i++) {
            final int threadIndex = i;
            threads[i] = new Thread(() -> {
                long startTime = System.currentTimeMillis();
                
                for (int j = 0; j < requestsPerThread; j++) {
                    given()
                        .when()
                            .get("/posts/" + (j % 10 + 1))
                        .then()
                            .statusCode(200);
                }
                
                long endTime = System.currentTimeMillis();
                threadTimes[threadIndex] = endTime - startTime;
            });
        }
        
        // Запускаем все потоки одновременно
        long testStartTime = System.currentTimeMillis();
        for (Thread thread : threads) {
            thread.start();
        }
        
        // Ждем завершения всех потоков
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        long testEndTime = System.currentTimeMillis();
        
        long totalTestTime = testEndTime - testStartTime;
        long totalRequests = numberOfThreads * requestsPerThread;
        
        // Прикрепляем метрики параллельных запросов
        StringBuilder metrics = new StringBuilder();
        metrics.append("Concurrent Test Results:\n");
        metrics.append(String.format("Number of Threads: %d\n", numberOfThreads));
        metrics.append(String.format("Requests per Thread: %d\n", requestsPerThread));
        metrics.append(String.format("Total Requests: %d\n", totalRequests));
        metrics.append(String.format("Total Test Time: %d ms\n", totalTestTime));
        metrics.append(String.format("Requests per Second: %.2f\n", (double) totalRequests / (totalTestTime / 1000.0)));
        metrics.append("\nThread Performance:\n");
        
        for (int i = 0; i < numberOfThreads; i++) {
            metrics.append(String.format("Thread %d: %d ms\n", i + 1, threadTimes[i]));
        }
        
        Allure.addAttachment("Concurrent Test Metrics", "text/plain", metrics.toString());
        
        // Проверяем, что общее время выполнения разумное
        AssertUtils.assertTrue(totalTestTime < 10000L, 
            "Общее время выполнения параллельных запросов должно быть менее 10 секунд, фактическое: " + totalTestTime + "ms");
    }
}
