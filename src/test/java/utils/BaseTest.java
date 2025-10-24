package utils;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

/**
 * Базовый класс для всех тестов
 */
public class BaseTest {
    
    @BeforeAll
    public static void setup() {
        // Настройка базового URL
        String baseUrl = Config.get("base.url", "https://jsonplaceholder.typicode.com");
        RestAssured.baseURI = baseUrl;
        
        // Настройка фильтров для Allure
        RestAssured.filters(new AllureRestAssured());
        
        System.out.println("✅ Настроен базовый URL: " + baseUrl);
    }
}
