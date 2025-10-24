package utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для маркировки тестов уникальными ID
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TestTag {
    /**
     * Уникальный ID теста
     */
    String id();
    
    /**
     * Описание теста
     */
    String description() default "";
    
    /**
     * Категория теста (API, UI, etc.)
     */
    String category() default "";
    
    /**
     * Приоритет теста (1-5, где 1 - высший)
     */
    int priority() default 3;
}
