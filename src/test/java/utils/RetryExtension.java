package utils;

import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Extension для retry механизма при падении тестов
 */
public class RetryExtension implements TestExecutionExceptionHandler {

    private static final AtomicInteger retryCount = new AtomicInteger(0);

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        RetryOnFailure retryAnnotation = context.getRequiredTestMethod().getAnnotation(RetryOnFailure.class);
        
        if (retryAnnotation != null) {
            int currentAttempt = retryCount.incrementAndGet();
            int maxAttempts = retryAnnotation.maxAttempts();
            
            Allure.addAttachment("Retry Attempt", "text/plain", 
                String.format("Попытка %d из %d\nОшибка: %s", currentAttempt, maxAttempts, throwable.getMessage()));
            
            if (currentAttempt < maxAttempts) {
                // Вычисляем задержку с экспоненциальным ростом
                long delay = calculateDelay(retryAnnotation, currentAttempt);
                
                Allure.addAttachment("Retry Info", "text/plain", 
                    String.format("Тест упал, повторяем попытку %d из %d через %d мс", 
                        currentAttempt + 1, maxAttempts, delay));
                
                // Ждем перед повторной попыткой
                Thread.sleep(delay);
                
                // Не пробрасываем исключение, позволяем тесту повториться
                return;
            } else {
                Allure.addAttachment("Retry Failed", "text/plain", 
                    String.format("Все %d попыток исчерпаны. Тест падает окончательно.", maxAttempts));
            }
        }
        
        // Пробрасываем исключение, если retry не настроен или попытки исчерпаны
        throw throwable;
    }

    /**
     * Вычисляет задержку с экспоненциальным ростом
     */
    private long calculateDelay(RetryOnFailure annotation, int attempt) {
        long baseDelay = annotation.delayMs();
        
        if (annotation.exponentialBackoff()) {
            // Экспоненциальная задержка: baseDelay * 2^(attempt-1)
            return baseDelay * (long) Math.pow(2, attempt - 1);
        } else {
            // Линейная задержка
            return baseDelay;
        }
    }


    /**
     * Аннотация для настройки retry механизма
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RetryOnFailure {
        int maxAttempts() default 3;
        long delayMs() default 1000;
        boolean exponentialBackoff() default true;
        String description() default "Retry on failure";
    }

}

