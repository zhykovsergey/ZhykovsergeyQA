package ui;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.*;
import utils.TestTag;
import utils.WebDriverUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Тесты валидации форм
 */
@Epic("UI Testing")
@Feature("Form Validation Tests")
public class FormValidationTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    @Step("Настройка браузера")
    public void setUp() {
        driver = WebDriverUtils.createWebDriver();
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
    @TestTag(id = "FORM_001", description = "Валидация пустых полей формы", category = "UI", priority = 1)
    @Story("Form Validation")
    @DisplayName("Проверка валидации пустых полей")
    @Description("Проверяем, что форма требует заполнения всех полей")
    public void testEmptyFieldsValidation() {
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
        
        // Пытаемся продолжить с пустыми полями
        checkoutPage.continueCheckout();
        
        // Проверяем, что появилась ошибка
        assertThat("Должна появиться ошибка валидации", checkoutPage.isErrorMessageDisplayed(), is(true));
        assertThat("Сообщение об ошибке не должно быть пустым", checkoutPage.getErrorMessage(), not(emptyString()));
    }

    @Test
    @TestTag(id = "FORM_002", description = "Валидация частично заполненной формы", category = "UI", priority = 1)
    @Story("Form Validation")
    @DisplayName("Проверка валидации частично заполненной формы")
    @Description("Проверяем валидацию при заполнении только некоторых полей")
    public void testPartiallyFilledFormValidation() {
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
        
        // Заполняем только имя
        checkoutPage.enterFirstName("John").continueCheckout();
        
        // Проверяем, что появилась ошибка
        assertThat("Должна появиться ошибка валидации", checkoutPage.isErrorMessageDisplayed(), is(true));
    }

    @ParameterizedTest
    @CsvSource({
        "John, Doe, 12345",
        "Jane, Smith, 67890",
        "Test, User, 00000"
    })
    @TestTag(id = "FORM_003", description = "Параметризованный тест валидных данных", category = "UI", priority = 2)
    @Story("Form Validation")
    @DisplayName("Проверка валидных данных формы")
    @Description("Проверяем, что форма принимает различные валидные данные")
    public void testValidFormData(String firstName, String lastName, String postalCode) {
        // Создаем Page Objects
        SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
        SauceDemoProductsPage productsPage = new SauceDemoProductsPage(driver);
        SauceDemoCartPage cartPage = new SauceDemoCartPage(driver);
        SauceDemoCheckoutPage checkoutPage = new SauceDemoCheckoutPage(driver);
        
        // Логинимся и переходим к оформлению заказа
        loginPage.openLoginPage().login("standard_user", "secret_sauce");
        productsPage.addFirstProductToCart().openCart();
        cartPage.proceedToCheckout();
        
        // Заполняем форму валидными данными
        checkoutPage.fillCheckoutForm(firstName, lastName, postalCode);
        
        // Проверяем, что ошибок нет
        assertThat("Ошибок не должно быть", checkoutPage.isErrorMessageDisplayed(), is(false));
        
        // Продолжаем оформление
        checkoutPage.continueCheckout();
        
        // Проверяем, что мы перешли на следующий шаг
        assertThat("URL должен измениться", driver.getCurrentUrl(), not(containsString("checkout-step-one")));
    }

    @Test
    @TestTag(id = "FORM_004", description = "Тест специальных символов в форме", category = "UI", priority = 3)
    @Story("Form Validation")
    @DisplayName("Проверка специальных символов в форме")
    @Description("Проверяем, как форма обрабатывает специальные символы")
    public void testSpecialCharactersInForm() {
        // Создаем Page Objects
        SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
        SauceDemoProductsPage productsPage = new SauceDemoProductsPage(driver);
        SauceDemoCartPage cartPage = new SauceDemoCartPage(driver);
        SauceDemoCheckoutPage checkoutPage = new SauceDemoCheckoutPage(driver);
        
        // Логинимся и переходим к оформлению заказа
        loginPage.openLoginPage().login("standard_user", "secret_sauce");
        productsPage.addFirstProductToCart().openCart();
        cartPage.proceedToCheckout();
        
        // Заполняем форму данными со специальными символами
        checkoutPage.fillCheckoutForm("José", "O'Connor", "123-45");
        
        // Проверяем, что ошибок нет
        assertThat("Ошибок не должно быть", checkoutPage.isErrorMessageDisplayed(), is(false));
    }

    @Test
    @TestTag(id = "FORM_005", description = "Тест длинных значений в форме", category = "UI", priority = 3)
    @Story("Form Validation")
    @DisplayName("Проверка длинных значений в форме")
    @Description("Проверяем, как форма обрабатывает длинные значения")
    public void testLongValuesInForm() {
        // Создаем Page Objects
        SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
        SauceDemoProductsPage productsPage = new SauceDemoProductsPage(driver);
        SauceDemoCartPage cartPage = new SauceDemoCartPage(driver);
        SauceDemoCheckoutPage checkoutPage = new SauceDemoCheckoutPage(driver);
        
        // Логинимся и переходим к оформлению заказа
        loginPage.openLoginPage().login("standard_user", "secret_sauce");
        productsPage.addFirstProductToCart().openCart();
        cartPage.proceedToCheckout();
        
        // Создаем длинные значения
        String longFirstName = "A".repeat(100);
        String longLastName = "B".repeat(100);
        String longPostalCode = "1".repeat(20);
        
        // Заполняем форму длинными значениями
        checkoutPage.fillCheckoutForm(longFirstName, longLastName, longPostalCode);
        
        // Проверяем, что форма принимает длинные значения
        assertThat("Ошибок не должно быть", checkoutPage.isErrorMessageDisplayed(), is(false));
    }

    @Test
    @TestTag(id = "FORM_006", description = "Тест пробелов в форме", category = "UI", priority = 3)
    @Story("Form Validation")
    @DisplayName("Проверка пробелов в форме")
    @Description("Проверяем, как форма обрабатывает пробелы")
    public void testSpacesInForm() {
        // Создаем Page Objects
        SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
        SauceDemoProductsPage productsPage = new SauceDemoProductsPage(driver);
        SauceDemoCartPage cartPage = new SauceDemoCartPage(driver);
        SauceDemoCheckoutPage checkoutPage = new SauceDemoCheckoutPage(driver);
        
        // Логинимся и переходим к оформлению заказа
        loginPage.openLoginPage().login("standard_user", "secret_sauce");
        productsPage.addFirstProductToCart().openCart();
        cartPage.proceedToCheckout();
        
        // Заполняем форму значениями с пробелами
        checkoutPage.fillCheckoutForm("  John  ", "  Doe  ", "  12345  ");
        
        // Проверяем, что форма принимает значения с пробелами
        assertThat("Ошибок не должно быть", checkoutPage.isErrorMessageDisplayed(), is(false));
    }

    @Test
    @TestTag(id = "FORM_007", description = "Тест отмены заполнения формы", category = "UI", priority = 2)
    @Story("Form Validation")
    @DisplayName("Проверка отмены заполнения формы")
    @Description("Проверяем возможность отмены заполнения формы")
    public void testCancelFormFilling() {
        // Создаем Page Objects
        SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
        SauceDemoProductsPage productsPage = new SauceDemoProductsPage(driver);
        SauceDemoCartPage cartPage = new SauceDemoCartPage(driver);
        SauceDemoCheckoutPage checkoutPage = new SauceDemoCheckoutPage(driver);
        
        // Логинимся и переходим к оформлению заказа
        loginPage.openLoginPage().login("standard_user", "secret_sauce");
        productsPage.addFirstProductToCart().openCart();
        cartPage.proceedToCheckout();
        
        // Начинаем заполнять форму
        checkoutPage.enterFirstName("John");
        
        // Отменяем заполнение
        checkoutPage.cancelCheckout();
        
        // Проверяем, что мы вернулись в корзину
        assertThat("Страница корзины должна загрузиться", cartPage.isCartPageLoaded(), is(true));
    }
}
