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
 * Regression тесты - проверка полного функционала приложения
 */
@Epic("UI Testing")
@Feature("Regression Tests")
public class RegressionUiTest {

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
    @TestTag(id = "REGRESSION_001", description = "Полный e-commerce сценарий", category = "UI", priority = 1)
    @Story("E-commerce Flow")
    @DisplayName("Полный сценарий покупки товара")
    @Description("Проверяем полный процесс от логина до оформления заказа")
    public void testFullEcommerceFlow() {
        // Создаем Page Objects
        SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
        SauceDemoProductsPage productsPage = new SauceDemoProductsPage(driver);
        SauceDemoCartPage cartPage = new SauceDemoCartPage(driver);
        SauceDemoCheckoutPage checkoutPage = new SauceDemoCheckoutPage(driver);
        
        // Шаг 1: Логин
        loginPage.openLoginPage().login("standard_user", "secret_sauce");
        assertThat("Логин должен быть успешным", productsPage.isProductsPageLoaded(), is(true));
        
        // Шаг 2: Добавление товара в корзину
        String productName = productsPage.getFirstProductName();
        String productPrice = productsPage.getFirstProductPrice();
        productsPage.addFirstProductToCart();
        
        // Проверяем, что товар добавлен
        assertThat("Товар должен быть в корзине", productsPage.getCartItemCount(), equalTo("1"));
        
        // Шаг 3: Переход в корзину
        productsPage.openCart();
        
        // Проверяем, что мы в корзине
        assertThat("URL должен содержать cart", 
                driver.getCurrentUrl(), containsString("cart"));
        
        // Шаг 4: Переход к оформлению заказа
        cartPage.proceedToCheckout();
        
        // Проверяем, что мы на странице оформления заказа
        assertThat("URL должен содержать checkout-step-one", 
                driver.getCurrentUrl(), containsString("checkout-step-one"));
        
        // Проверяем, что поля формы отображаются
        assertThat("Поля формы должны отображаться", checkoutPage.areFormFieldsDisplayed(), is(true));
    }

    @Test
    @TestTag(id = "REGRESSION_002", description = "Тест формы с валидными данными", category = "UI", priority = 1)
    @Story("Form Validation")
    @DisplayName("Заполнение формы валидными данными")
    @Description("Проверяем, что форма принимает корректные данные")
    public void testFormWithValidData() {
        // Создаем Page Objects
        SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
        SauceDemoProductsPage productsPage = new SauceDemoProductsPage(driver);
        SauceDemoCartPage cartPage = new SauceDemoCartPage(driver);
        SauceDemoCheckoutPage checkoutPage = new SauceDemoCheckoutPage(driver);
        
        // Логинимся и добавляем товар в корзину
        loginPage.openLoginPage().login("standard_user", "secret_sauce");
        productsPage.addFirstProductToCart().openCart();
        cartPage.proceedToCheckout();
        
        // Заполняем форму валидными данными
        checkoutPage.fillCheckoutForm("John", "Doe", "12345");
        
        // Проверяем, что ошибок нет
        assertThat("Ошибок не должно быть", checkoutPage.isErrorMessageDisplayed(), is(false));
        
        // Продолжаем оформление
        checkoutPage.continueCheckout();
        
        // Проверяем, что мы перешли на следующий шаг
        assertThat("URL должен измениться", driver.getCurrentUrl(), not(containsString("checkout-step-one")));
    }

    @Test
    @TestTag(id = "REGRESSION_003", description = "Тест формы с невалидными данными", category = "UI", priority = 2)
    @Story("Form Validation")
    @DisplayName("Заполнение формы невалидными данными")
    @Description("Проверяем валидацию формы с пустыми полями")
    public void testFormWithInvalidData() {
        // Создаем Page Objects
        SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
        SauceDemoProductsPage productsPage = new SauceDemoProductsPage(driver);
        SauceDemoCartPage cartPage = new SauceDemoCartPage(driver);
        SauceDemoCheckoutPage checkoutPage = new SauceDemoCheckoutPage(driver);
        
        // Логинимся и добавляем товар в корзину
        loginPage.openLoginPage().login("standard_user", "secret_sauce");
        productsPage.addFirstProductToCart().openCart();
        cartPage.proceedToCheckout();
        
        // Пытаемся продолжить с пустыми полями
        checkoutPage.continueCheckout();
        
        // Проверяем, что появилась ошибка
        assertThat("Должна появиться ошибка", checkoutPage.isErrorMessageDisplayed(), is(true));
        assertThat("Сообщение об ошибке не должно быть пустым", checkoutPage.getErrorMessage(), not(emptyString()));
    }

    @Test
    @TestTag(id = "REGRESSION_004", description = "Тест удаления товара из корзины", category = "UI", priority = 2)
    @Story("Cart Management")
    @DisplayName("Удаление товара из корзины")
    @Description("Проверяем функционал удаления товаров из корзины")
    public void testRemoveItemFromCart() {
        // Создаем Page Objects
        SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
        SauceDemoProductsPage productsPage = new SauceDemoProductsPage(driver);
        SauceDemoCartPage cartPage = new SauceDemoCartPage(driver);
        
        // Логинимся и добавляем товар в корзину
        loginPage.openLoginPage().login("standard_user", "secret_sauce");
        productsPage.addFirstProductToCart();
        
        // Проверяем, что товар добавлен
        assertThat("Товар должен быть в корзине", productsPage.getCartItemCount(), equalTo("1"));
        
        // Переходим в корзину
        productsPage.openCart();
        assertThat("Страница корзины должна загрузиться", cartPage.isCartPageLoaded(), is(true));
        assertThat("Товар должен отображаться в корзине", cartPage.areItemsDisplayed(), is(true));
        
        // Удаляем товар из корзины
        cartPage.removeFirstItem();
        
        // Проверяем, что корзина пуста
        assertThat("Корзина должна быть пуста", cartPage.isCartEmpty(), is(true));
    }

    @Test
    @TestTag(id = "REGRESSION_005", description = "Тест возврата к покупкам", category = "UI", priority = 2)
    @Story("Navigation")
    @DisplayName("Возврат к покупкам из корзины")
    @Description("Проверяем навигацию между корзиной и каталогом")
    public void testReturnToShopping() {
        // Создаем Page Objects
        SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
        SauceDemoProductsPage productsPage = new SauceDemoProductsPage(driver);
        SauceDemoCartPage cartPage = new SauceDemoCartPage(driver);
        
        // Логинимся и добавляем товар в корзину
        loginPage.openLoginPage().login("standard_user", "secret_sauce");
        productsPage.addFirstProductToCart().openCart();
        
        // Проверяем, что мы в корзине
        assertThat("Страница корзины должна загрузиться", cartPage.isCartPageLoaded(), is(true));
        
        // Возвращаемся к покупкам
        cartPage.continueShopping();
        
        // Проверяем, что мы вернулись на страницу продуктов
        assertThat("Страница продуктов должна загрузиться", productsPage.isProductsPageLoaded(), is(true));
        assertThat("Продукты должны отображаться", productsPage.areProductsDisplayed(), is(true));
    }

    @Test
    @TestTag(id = "REGRESSION_006", description = "Тест множественных товаров", category = "UI", priority = 3)
    @Story("Multiple Items")
    @DisplayName("Добавление нескольких товаров в корзину")
    @Description("Проверяем работу с несколькими товарами")
    public void testMultipleItemsInCart() {
        // Создаем Page Objects
        SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
        SauceDemoProductsPage productsPage = new SauceDemoProductsPage(driver);
        SauceDemoCartPage cartPage = new SauceDemoCartPage(driver);
        
        // Логинимся
        loginPage.openLoginPage().login("standard_user", "secret_sauce");
        
        // Добавляем несколько товаров (если доступно)
        int initialCartCount = Integer.parseInt(productsPage.getCartItemCount());
        
        // Добавляем первый товар
        productsPage.addFirstProductToCart();
        assertThat("Количество товаров должно увеличиться", 
                Integer.parseInt(productsPage.getCartItemCount()), equalTo(initialCartCount + 1));
        
        // Переходим в корзину
        productsPage.openCart();
        assertThat("Страница корзины должна загрузиться", cartPage.isCartPageLoaded(), is(true));
        assertThat("Товары должны отображаться в корзине", cartPage.areItemsDisplayed(), is(true));
    }

    @Test
    @TestTag(id = "REGRESSION_007", description = "Тест отмены оформления заказа", category = "UI", priority = 3)
    @Story("Checkout Cancellation")
    @DisplayName("Отмена оформления заказа")
    @Description("Проверяем возможность отмены оформления заказа")
    public void testCancelCheckout() {
        // Создаем Page Objects
        SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
        SauceDemoProductsPage productsPage = new SauceDemoProductsPage(driver);
        SauceDemoCartPage cartPage = new SauceDemoCartPage(driver);
        SauceDemoCheckoutPage checkoutPage = new SauceDemoCheckoutPage(driver);
        
        // Логинимся и добавляем товар в корзину
        loginPage.openLoginPage().login("standard_user", "secret_sauce");
        productsPage.addFirstProductToCart().openCart();
        cartPage.proceedToCheckout();
        
        // Проверяем, что мы на странице оформления заказа
        assertThat("Страница оформления заказа должна загрузиться", checkoutPage.isCheckoutPageLoaded(), is(true));
        
        // Отменяем оформление заказа
        checkoutPage.cancelCheckout();
        
        // Проверяем, что мы вернулись в корзину
        assertThat("Страница корзины должна загрузиться", cartPage.isCartPageLoaded(), is(true));
    }
}
