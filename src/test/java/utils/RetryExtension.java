package utils;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Расширение для повторного выполнения тестов при падении
 */
public class RetryExtension implements TestExecutionExceptionHandler, BeforeEachCallback, AfterEachCallback {

    private static final String RETRY_COUNT_KEY = "retryCount";

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        AtomicInteger retryCount = getRetryCount(context);
        int maxAttempts = getMaxAttempts(context);
        long delayMs = getDelayMs(context);

        if (retryCount.get() < maxAttempts) {
            retryCount.incrementAndGet();
            context.getStore(ExtensionContext.Namespace.create(RetryExtension.class))
                .put(RETRY_COUNT_KEY, retryCount);

            System.out.printf("Test failed, retrying (%d/%d) in %d ms...%n", 
                retryCount.get(), maxAttempts, delayMs);

            try {
                Thread.sleep(delayMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }

            // Не выбрасываем исключение, позволяем тесту повториться
            return;
        }

        // Если исчерпаны все попытки, выбрасываем исключение
        throw throwable;
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        // Сбрасываем счетчик попыток для каждого теста
        context.getStore(ExtensionContext.Namespace.create(RetryExtension.class))
            .put(RETRY_COUNT_KEY, new AtomicInteger(0));
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        // Очищаем данные после теста
        context.getStore(ExtensionContext.Namespace.create(RetryExtension.class))
            .remove(RETRY_COUNT_KEY);
    }

    private AtomicInteger getRetryCount(ExtensionContext context) {
        return context.getStore(ExtensionContext.Namespace.create(RetryExtension.class))
            .getOrComputeIfAbsent(RETRY_COUNT_KEY, key -> new AtomicInteger(0), AtomicInteger.class);
    }

    private int getMaxAttempts(ExtensionContext context) {
        RetryOnFailure annotation = context.getRequiredTestMethod().getAnnotation(RetryOnFailure.class);
        if (annotation != null) {
            return annotation.maxAttempts();
        }
        return 3; // По умолчанию
    }

    private long getDelayMs(ExtensionContext context) {
        RetryOnFailure annotation = context.getRequiredTestMethod().getAnnotation(RetryOnFailure.class);
        if (annotation != null) {
            long baseDelay = annotation.delayMs();
            if (annotation.exponentialBackoff()) {
                AtomicInteger retryCount = getRetryCount(context);
                return baseDelay * (long) Math.pow(2, retryCount.get());
            }
            return baseDelay;
        }
        return 1000; // По умолчанию 1 секунда
    }

    /**
     * Аннотация для настройки повторных попыток
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RetryOnFailure {
        int maxAttempts() default 3;
        long delayMs() default 1000;
        boolean exponentialBackoff() default false;
    }
}