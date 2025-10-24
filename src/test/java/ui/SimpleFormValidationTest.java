package ui;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import pages.SauceDemoLoginPage;
import pages.SauceDemoProductsPage;
import pages.SauceDemoCartPage;
import pages.SauceDemoCheckoutPage;
import utils.TestTag;
import utils.WebDriverUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Простой тест валидации форм
 */
@Epic("UI Testing")
@Feature("Simple Form Validation Tests")
public class SimpleFormValidationTest {

    private WebDriver driver;

    @BeforeEach
    @Step("Настройка браузера")
    public void setUp() {
        driver = WebDriverUtils.createWebDriver(false); // Видимый браузер
    }

    @AfterEach
    @Step("Закрытие браузера")
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @TestTag(id = "SIMPLE_FORM_001", description = "Простой тест валидации формы", category = "UI", priority = 1)
    @Story("Simple Form Validation")
    @DisplayName("Проверка валидации формы checkout")
    @Description("Простой тест для проверки валидации формы")
    public void testSimpleFormValidation() {
        // Создаем Page Objects
        SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
        SauceDemoProductsPage productsPage = new SauceDemoProductsPage(driver);
        SauceDemoCartPage cartPage = new SauceDemoCartPage(driver);
        SauceDemoCheckoutPage checkoutPage = new SauceDemoCheckoutPage(driver);
        
        // Логинимся
        loginPage.openLoginPage().login("standard_user", "secret_sauce");
        
        // Ждем загрузки страницы продуктов
        WebDriverUtils.waitForPageLoad(driver);
        
        // Добавляем товар в корзину
        productsPage.addFirstProductToCart();
        
        // Ждем обновления счетчика корзины
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Переходим в корзину
        productsPage.openCart();
        
        // Ждем загрузки страницы корзины
        WebDriverUtils.waitForPageLoad(driver);
        
        // Переходим к оформлению заказа
        cartPage.proceedToCheckout();
        
        // Ждем загрузки страницы checkout
        WebDriverUtils.waitForPageLoad(driver);
        
        // Проверяем, что мы на странице checkout
        String currentUrl = driver.getCurrentUrl();
        assertThat("URL должен содержать 'checkout'", currentUrl, containsString("checkout"));
        
        // Проверяем, что форма загружена
        assertThat("Страница checkout должна быть загружена", checkoutPage.isCheckoutPageLoaded(), is(true));
        
        System.out.println("✅ Тест валидации формы прошел успешно");
    }

    @Test
    @TestTag(id = "SIMPLE_FORM_002", description = "Тест заполнения формы", category = "UI", priority = 2)
    @Story("Simple Form Validation")
    @DisplayName("Проверка заполнения формы")
    @Description("Тест заполнения формы валидными данными")
    public void testFillFormWithValidData() {
        // Создаем Page Objects
        SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
        SauceDemoProductsPage productsPage = new SauceDemoProductsPage(driver);
        SauceDemoCartPage cartPage = new SauceDemoCartPage(driver);
        SauceDemoCheckoutPage checkoutPage = new SauceDemoCheckoutPage(driver);
        
        // Логинимся
        loginPage.openLoginPage().login("standard_user", "secret_sauce");
        
        // Ждем загрузки страницы продуктов
        WebDriverUtils.waitForPageLoad(driver);
        
        // Добавляем товар в корзину
        productsPage.addFirstProductToCart();
        
        // Ждем обновления счетчика корзины
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Переходим в корзину
        productsPage.openCart();
        
        // Ждем загрузки страницы корзины
        WebDriverUtils.waitForPageLoad(driver);
        
        // Переходим к оформлению заказа
        cartPage.proceedToCheckout();
        
        // Ждем загрузки страницы checkout
        WebDriverUtils.waitForPageLoad(driver);
        
        // Заполняем форму валидными данными
        checkoutPage.enterFirstName("John")
                   .enterLastName("Doe")
                   .enterPostalCode("12345");
        
        // Продолжаем checkout
        checkoutPage.continueCheckout();
        
        // Ждем загрузки следующей страницы
        WebDriverUtils.waitForPageLoad(driver);
        
        // Проверяем, что мы перешли на страницу overview
        String currentUrl = driver.getCurrentUrl();
        assertThat("URL должен содержать 'checkout-step-two'", currentUrl, containsString("checkout-step-two"));
        
        System.out.println("✅ Тест заполнения формы прошел успешно");
    }
}
