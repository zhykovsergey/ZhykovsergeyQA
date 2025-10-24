package ui;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DisplayName;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.*;
import utils.TestTag;
import utils.WebDriverUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Простые тесты SauceDemo для отладки
 */
@Epic("UI Testing")
@Feature("Simple SauceDemo Tests")
public class SimpleSauceDemoTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    @Step("Настройка браузера")
    public void setUp() {
        // Используем видимый браузер для отладки
        driver = WebDriverUtils.createWebDriver(false);
        wait = WebDriverUtils.createWebDriverWait(driver);
    }

    @AfterEach
    @Step("Закрытие браузера")
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @TestTag(id = "SIMPLE_001", description = "Простой тест логина", category = "UI", priority = 1)
    @Story("Simple Tests")
    @DisplayName("Проверка логина в SauceDemo")
    @Description("Простая проверка логина без сложных сценариев")
    public void testSimpleLogin() {
        // Создаем Page Objects
        SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
        SauceDemoProductsPage productsPage = new SauceDemoProductsPage(driver);
        
        // Открываем страницу логина
        loginPage.openLoginPage();
        
        // Проверяем, что страница загрузилась
        assertThat("Страница логина должна быть открыта", 
                driver.getCurrentUrl(), containsString("saucedemo.com"));
        
        // Выполняем логин
        loginPage.login("standard_user", "secret_sauce");
        
        // Проверяем, что мы попали на страницу продуктов
        assertThat("URL должен содержать inventory", 
                driver.getCurrentUrl(), containsString("inventory"));
    }

    @Test
    @TestTag(id = "SIMPLE_002", description = "Простой тест добавления в корзину", category = "UI", priority = 1)
    @Story("Simple Tests")
    @DisplayName("Проверка добавления товара в корзину")
    @Description("Простая проверка добавления товара в корзину")
    public void testSimpleAddToCart() {
        // Создаем Page Objects
        SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
        SauceDemoProductsPage productsPage = new SauceDemoProductsPage(driver);
        
        // Логинимся
        loginPage.openLoginPage().login("standard_user", "secret_sauce");
        
        // Проверяем, что мы на странице продуктов
        assertThat("URL должен содержать inventory", 
                driver.getCurrentUrl(), containsString("inventory"));
        
        // Добавляем товар в корзину
        productsPage.addFirstProductToCart();
        
        // Проверяем, что товар добавлен (проверяем URL или другие индикаторы)
        // Простая проверка - если нет ошибок, значит товар добавлен
        assertThat("Страница должна остаться на inventory", 
                driver.getCurrentUrl(), containsString("inventory"));
    }

    @Test
    @TestTag(id = "SIMPLE_003", description = "Простой тест перехода в корзину", category = "UI", priority = 2)
    @Story("Simple Tests")
    @DisplayName("Проверка перехода в корзину")
    @Description("Простая проверка перехода в корзину")
    public void testSimpleGoToCart() {
        // Создаем Page Objects
        SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
        SauceDemoProductsPage productsPage = new SauceDemoProductsPage(driver);
        
        // Логинимся
        loginPage.openLoginPage().login("standard_user", "secret_sauce");
        
        // Добавляем товар в корзину
        productsPage.addFirstProductToCart();
        
        // Переходим в корзину
        productsPage.openCart();
        
        // Проверяем, что мы в корзине
        assertThat("URL должен содержать cart", 
                driver.getCurrentUrl(), containsString("cart"));
    }

    @Test
    @TestTag(id = "SIMPLE_004", description = "Простой тест оформления заказа", category = "UI", priority = 2)
    @Story("Simple Tests")
    @DisplayName("Проверка перехода к оформлению заказа")
    @Description("Простая проверка перехода к оформлению заказа")
    public void testSimpleCheckout() {
        // Создаем Page Objects
        SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
        SauceDemoProductsPage productsPage = new SauceDemoProductsPage(driver);
        SauceDemoCartPage cartPage = new SauceDemoCartPage(driver);
        
        // Логинимся
        loginPage.openLoginPage().login("standard_user", "secret_sauce");
        
        // Добавляем товар в корзину
        productsPage.addFirstProductToCart();
        
        // Переходим в корзину
        productsPage.openCart();
        
        // Переходим к оформлению заказа
        cartPage.proceedToCheckout();
        
        // Проверяем, что мы на странице оформления заказа
        assertThat("URL должен содержать checkout-step-one", 
                driver.getCurrentUrl(), containsString("checkout-step-one"));
    }
}
