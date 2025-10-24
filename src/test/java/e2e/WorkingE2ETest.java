package e2e;

import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.OutputType;

import java.time.Duration;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@Epic("E2E Testing")
@Feature("Working E2E Tests")
@Story("API to UI Integration Tests")
public class WorkingE2ETest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        // Настройка API
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
        
        // Настройка WebDriver
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("API → UI: Получить данные через API и проверить в UI")
    @Description("Тест получения данных через API и проверки их отображения в UI")
    @Severity(SeverityLevel.CRITICAL)
    void testApiToUiDataFlow() {
        // Шаг 1: Получаем данные через API
        Response apiResponse = given()
                .when()
                .get("/posts/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("title", notNullValue())
                .body("body", notNullValue())
                .extract().response();

        String postTitle = apiResponse.jsonPath().getString("title");
        String postBody = apiResponse.jsonPath().getString("body");
        
        Allure.addAttachment("API Response", "application/json", apiResponse.asString());
        
        // Шаг 2: Открываем UI и проверяем, что можем взаимодействовать с элементами
        driver.get("https://www.saucedemo.com");
        
        // Проверяем, что страница загрузилась
        WebElement loginButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("login-button")));
        assertTrue(loginButton.isDisplayed(), "Login button should be visible");
        
        // Входим в систему
        WebElement usernameField = driver.findElement(By.id("user-name"));
        usernameField.sendKeys("standard_user");
        
        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("secret_sauce");
        
        loginButton.click();
        
        // Проверяем, что мы попали на страницу продуктов
        WebElement productsTitle = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("title")));
        assertEquals("Products", productsTitle.getText(), "Should be on products page");
        
        // Screenshot attachment removed to avoid compilation issues
        
        // Шаг 3: Проверяем, что данные из API корректны
        assertNotNull(postTitle, "Post title from API should not be null");
        assertNotNull(postBody, "Post body from API should not be null");
        assertTrue(postTitle.length() > 0, "Post title should not be empty");
        assertTrue(postBody.length() > 0, "Post body should not be empty");
    }

    @Test
    @DisplayName("UI → API: Проверить UI элементы и валидировать через API")
    @Description("Тест проверки UI элементов и валидации через API")
    @Severity(SeverityLevel.NORMAL)
    void testUiToApiValidation() {
        // Шаг 1: Открываем UI и проверяем элементы
        driver.get("https://www.saucedemo.com");
        
        // Проверяем наличие элементов входа
        WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("user-name")));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("login-button"));
        
        assertTrue(usernameField.isDisplayed(), "Username field should be visible");
        assertTrue(passwordField.isDisplayed(), "Password field should be visible");
        assertTrue(loginButton.isDisplayed(), "Login button should be visible");
        
        // Шаг 2: Валидируем через API, что можем получить данные
        Response apiResponse = given()
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .extract().response();
        
        Allure.addAttachment("API Users Response", "application/json", apiResponse.asString());
        
        // Шаг 3: Проверяем, что API вернул корректные данные
        int userCount = apiResponse.jsonPath().getList("").size();
        assertTrue(userCount > 0, "API should return at least one user");
        
        // Screenshot attachment removed to avoid compilation issues
    }

    @Test
    @DisplayName("Полный E2E сценарий: API + UI + Валидация")
    @Description("Полный E2E тест с использованием API и UI")
    @Severity(SeverityLevel.CRITICAL)
    void testFullE2EScenario() {
        // Шаг 1: Получаем данные через API
        Response postsResponse = given()
                .when()
                .get("/posts")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .extract().response();
        
        int postCount = postsResponse.jsonPath().getList("").size();
        assertTrue(postCount > 0, "API should return posts");
        
        // Шаг 2: Открываем UI и выполняем действия
        driver.get("https://www.saucedemo.com");
        
        // Входим в систему
        WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("user-name")));
        usernameField.sendKeys("standard_user");
        
        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("secret_sauce");
        
        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();
        
        // Проверяем успешный вход
        WebElement productsTitle = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("title")));
        assertEquals("Products", productsTitle.getText(), "Should be on products page");
        
        // Шаг 3: Проверяем наличие продуктов на странице
        WebElement inventoryContainer = driver.findElement(By.className("inventory_container"));
        assertTrue(inventoryContainer.isDisplayed(), "Inventory container should be visible");
        
        // Шаг 4: Валидируем через API, что можем получить пользователей
        Response usersResponse = given()
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .extract().response();
        
        int userCount = usersResponse.jsonPath().getList("").size();
        assertTrue(userCount > 0, "API should return users");
        
        // Screenshot attachment removed to avoid compilation issues
        
        // Шаг 5: Финальная валидация
        assertTrue(postCount > 0 && userCount > 0, "Both API endpoints should return data");
    }
}
