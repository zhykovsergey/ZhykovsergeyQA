package ui;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import pages.*;
import utils.*;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.*;

/**
 * UI тесты для SauceDemo приложения
 */
@Epic("UI Testing")
@Feature("SauceDemo UI Tests")
@ExtendWith(RetryExtension.class)
public class SauceDemoUiTest extends BaseUiTest {

    private SauceDemoLoginPage loginPage;
    private SauceDemoProductsPage productsPage;
    private SauceDemoCartPage cartPage;

    @BeforeEach
    void setUp() {
        step("Инициализация страниц", () -> {
            loginPage = new SauceDemoLoginPage(driver);
            productsPage = new SauceDemoProductsPage(driver);
            cartPage = new SauceDemoCartPage(driver);
        });
    }

    @Test
    @TestTag(id = "UI_001", description = "Успешный логин в SauceDemo", category = "UI", priority = 1)
    @Story("Login Tests")
    @DisplayName("Успешный логин в SauceDemo")
    @Description("Проверяем успешный логин с валидными данными")
    @Severity(SeverityLevel.CRITICAL)
    public void testSuccessfulLogin() {
        step("Открываем страницу логина", () -> {
            driver.get("https://www.saucedemo.com");
        });

        step("Вводим валидные данные для логина", () -> {
            loginPage.enterUsername("standard_user");
            loginPage.enterPassword("secret_sauce");
        });

        step("Нажимаем кнопку логина", () -> {
            loginPage.clickLoginButton();
        });

        step("Проверяем, что мы на странице продуктов", () -> {
            assertTrue(productsPage.areProductsDisplayed(), "Страница продуктов должна отображаться");
            assertTrue(productsPage.isCartDisplayed(), "Корзина должна отображаться");
        });
    }

    @Test
    @TestTag(id = "UI_002", description = "Неуспешный логин с неверными данными", category = "UI", priority = 2)
    @Story("Login Tests")
    @DisplayName("Неуспешный логин с неверными данными")
    @Description("Проверяем поведение при вводе неверных данных")
    @Severity(SeverityLevel.NORMAL)
    public void testFailedLogin() {
        step("Открываем страницу логина", () -> {
            driver.get("https://www.saucedemo.com");
        });

        step("Вводим неверные данные для логина", () -> {
            loginPage.enterUsername("invalid_user");
            loginPage.enterPassword("wrong_password");
        });

        step("Нажимаем кнопку логина", () -> {
            loginPage.clickLoginButton();
        });

        step("Проверяем, что отображается сообщение об ошибке", () -> {
            assertTrue(loginPage.isErrorMessageDisplayed(), "Сообщение об ошибке должно отображаться");
            assertTrue(loginPage.getErrorMessage().contains("Username and password do not match"), 
                "Сообщение об ошибке должно содержать правильный текст");
        });
    }

    @Test
    @TestTag(id = "UI_003", description = "Добавление товара в корзину", category = "UI", priority = 1)
    @Story("Shopping Tests")
    @DisplayName("Добавление товара в корзину")
    @Description("Проверяем добавление товара в корзину")
    @Severity(SeverityLevel.CRITICAL)
    public void testAddToCart() {
        step("Логинимся в приложение", () -> {
            driver.get("https://www.saucedemo.com");
            loginPage.enterUsername("standard_user");
            loginPage.enterPassword("secret_sauce");
            loginPage.clickLoginButton();
        });

        step("Добавляем первый товар в корзину", () -> {
            String productName = productsPage.getFirstProductName();
            productsPage.addFirstProductToCart();
            
            // Проверяем, что товар добавлен в корзину
            assertTrue(productsPage.isCartBadgeDisplayed(), "Значок корзины должен отображаться");
        });

        step("Переходим в корзину", () -> {
            productsPage.clickCartButton();
        });

        step("Проверяем, что товар в корзине", () -> {
            assertTrue(cartPage.isProductInCart(), "Товар должен быть в корзине");
        });
    }

    @Test
    @TestTag(id = "UI_004", description = "Удаление товара из корзины", category = "UI", priority = 2)
    @Story("Shopping Tests")
    @DisplayName("Удаление товара из корзины")
    @Description("Проверяем удаление товара из корзины")
    @Severity(SeverityLevel.NORMAL)
    public void testRemoveFromCart() {
        step("Логинимся и добавляем товар в корзину", () -> {
            driver.get("https://www.saucedemo.com");
            loginPage.enterUsername("standard_user");
            loginPage.enterPassword("secret_sauce");
            loginPage.clickLoginButton();
            productsPage.addFirstProductToCart();
            productsPage.clickCartButton();
        });

        step("Удаляем товар из корзины", () -> {
            cartPage.removeFirstProduct();
        });

        step("Проверяем, что корзина пуста", () -> {
            assertTrue(cartPage.isCartEmpty(), "Корзина должна быть пуста");
        });
    }

    @Test
    @TestTag(id = "UI_005", description = "Проверка отображения товаров", category = "UI", priority = 3)
    @Story("Product Display Tests")
    @DisplayName("Проверка отображения товаров")
    @Description("Проверяем корректное отображение товаров на странице")
    @Severity(SeverityLevel.MINOR)
    public void testProductDisplay() {
        step("Логинимся в приложение", () -> {
            driver.get("https://www.saucedemo.com");
            loginPage.enterUsername("standard_user");
            loginPage.enterPassword("secret_sauce");
            loginPage.clickLoginButton();
        });

        step("Проверяем отображение товаров", () -> {
            assertTrue(productsPage.areProductsDisplayed(), "Товары должны отображаться");
            assertTrue(productsPage.getProductCount() > 0, "Должно быть больше 0 товаров");
        });

        step("Проверяем информацию о первом товаре", () -> {
            String productName = productsPage.getFirstProductName();
            String productPrice = productsPage.getFirstProductPrice();
            
            assertNotNull(productName, "Название товара не должно быть null");
            assertNotNull(productPrice, "Цена товара не должна быть null");
            assertFalse(productName.isEmpty(), "Название товара не должно быть пустым");
            assertFalse(productPrice.isEmpty(), "Цена товара не должна быть пустой");
        });
    }
}
