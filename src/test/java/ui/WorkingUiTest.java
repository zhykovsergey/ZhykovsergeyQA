package ui;

import io.qameta.allure.*;
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

import static org.junit.jupiter.api.Assertions.*;

@Epic("UI Testing")
@Feature("Working UI Tests")
@Story("Stable UI Tests with SauceDemo")
public class WorkingUiTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
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
    @DisplayName("Открыть главную страницу SauceDemo")
    @Description("Тест открытия главной страницы SauceDemo")
    @Severity(SeverityLevel.CRITICAL)
    void testOpenSauceDemoHomePage() {
        driver.get("https://www.saucedemo.com");
        
        // Проверяем, что страница загрузилась
        WebElement loginButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("login-button")));
        assertTrue(loginButton.isDisplayed(), "Login button should be visible");
        
        // Проверяем заголовок страницы
        String pageTitle = driver.getTitle();
        assertEquals("Swag Labs", pageTitle, "Page title should be 'Swag Labs'");
        
        // Screenshot attachment removed to avoid compilation issues
    }

    @Test
    @DisplayName("Проверить элементы на странице входа")
    @Description("Тест проверки наличия всех элементов на странице входа")
    @Severity(SeverityLevel.CRITICAL)
    void testLoginPageElements() {
        driver.get("https://www.saucedemo.com");
        
        // Проверяем наличие поля username
        WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("user-name")));
        assertTrue(usernameField.isDisplayed(), "Username field should be visible");
        
        // Проверяем наличие поля password
        WebElement passwordField = driver.findElement(By.id("password"));
        assertTrue(passwordField.isDisplayed(), "Password field should be visible");
        
        // Проверяем наличие кнопки входа
        WebElement loginButton = driver.findElement(By.id("login-button"));
        assertTrue(loginButton.isDisplayed(), "Login button should be visible");
        
        // Screenshot attachment removed to avoid compilation issues
    }

    @Test
    @DisplayName("Попытка входа с неверными данными")
    @Description("Тест входа с неверными учетными данными")
    @Severity(SeverityLevel.NORMAL)
    void testLoginWithInvalidCredentials() {
        driver.get("https://www.saucedemo.com");
        
        // Вводим неверные данные
        WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("user-name")));
        usernameField.sendKeys("invalid_user");
        
        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("invalid_password");
        
        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();
        
        // Проверяем, что появилось сообщение об ошибке
        WebElement errorMessage = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-test='error']")));
        assertTrue(errorMessage.isDisplayed(), "Error message should be visible");
        
        String errorText = errorMessage.getText();
        assertTrue(errorText.contains("Username and password do not match"), 
            "Error message should contain 'Username and password do not match'");
        
        // Screenshot attachment removed to avoid compilation issues
    }

    @Test
    @DisplayName("Вход с правильными данными")
    @Description("Тест входа с правильными учетными данными")
    @Severity(SeverityLevel.CRITICAL)
    void testLoginWithValidCredentials() {
        driver.get("https://www.saucedemo.com");
        
        // Вводим правильные данные
        WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("user-name")));
        usernameField.sendKeys("standard_user");
        
        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("secret_sauce");
        
        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();
        
        // Проверяем, что мы попали на страницу продуктов
        WebElement productsTitle = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("title")));
        assertTrue(productsTitle.isDisplayed(), "Products title should be visible");
        
        String titleText = productsTitle.getText();
        assertEquals("Products", titleText, "Title should be 'Products'");
        
        // Screenshot attachment removed to avoid compilation issues
    }

    @Test
    @DisplayName("Проверка навигации по страницам")
    @Description("Тест навигации между страницами")
    @Severity(SeverityLevel.NORMAL)
    void testPageNavigation() {
        driver.get("https://www.saucedemo.com");
        
        // Входим в систему
        WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("user-name")));
        usernameField.sendKeys("standard_user");
        
        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("secret_sauce");
        
        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();
        
        // Проверяем, что мы на странице продуктов
        WebElement productsTitle = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("title")));
        assertEquals("Products", productsTitle.getText(), "Should be on products page");
        
        // Проверяем наличие кнопки корзины
        WebElement cartButton = driver.findElement(By.className("shopping_cart_link"));
        assertTrue(cartButton.isDisplayed(), "Cart button should be visible");
        
        // Screenshot attachment removed to avoid compilation issues
    }
}
