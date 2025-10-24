package utils;

import io.restassured.module.jsv.JsonSchemaValidator;
import org.junit.jupiter.api.Assertions;

import java.io.InputStream;

/**
 * Утилита для валидации JSON схем
 */
public class SchemaValidator {
    
    /**
     * Валидировать JSON ответ против схемы
     * @param schemaPath путь к JSON схеме в resources
     * @return JsonSchemaValidator для использования в RestAssured
     */
    public static JsonSchemaValidator validateSchema(String schemaPath) {
        return JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath);
    }
    
    /**
     * Проверить, что схема существует
     * @param schemaPath путь к схеме
     * @return true если схема найдена
     */
    public static boolean schemaExists(String schemaPath) {
        try {
            InputStream schemaStream = SchemaValidator.class.getClassLoader()
                    .getResourceAsStream(schemaPath);
            return schemaStream != null;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Получить путь к схеме поста
     */
    public static String getPostSchemaPath() {
        return "schemas/post_schema.json";
    }
    
    /**
     * Получить путь к схеме пользователя
     */
    public static String getUserSchemaPath() {
        return "schemas/user_schema.json";
    }
    
    /**
     * Получить путь к схеме комментария
     */
    public static String getCommentSchemaPath() {
        return "schemas/comment_schema.json";
    }
}
