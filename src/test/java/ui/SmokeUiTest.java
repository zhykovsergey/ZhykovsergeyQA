package ui;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DisplayName;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.GoogleSearchPage;
import pages.SauceDemoLoginPage;
import pages.SauceDemoProductsPage;
import utils.TestTag;
import utils.WebDriverUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Smoke тесты - проверка основных функций приложения
 */
@Epic("UI Testing")
@Feature("Smoke Tests")
public class SmokeUiTest {

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
    @TestTag(id = "SMOKE_001", description = "Smoke тест Google поиска", category = "UI", priority = 1)
    @Story("Smoke Tests")
    @DisplayName("Проверка основного функционала Google")
    @Description("Проверяем, что Google открывается и поиск работает")
    public void testGoogleSearchSmoke() {
        // Создаем Page Object
        GoogleSearchPage googlePage = new GoogleSearchPage(driver);
        
        // Открываем Google
        googlePage.openGoogle();
        
        // Проверяем, что страница загрузилась
        assertThat("Google должен быть открыт", googlePage.getCurrentUrl(), containsString("google.com"));
        
        // Выполняем поиск
        googlePage.searchFor("Selenium WebDriver");
        
        // Проверяем результаты
        assertThat("Результаты поиска должны отображаться", googlePage.areSearchResultsDisplayed(), is(true));
        assertThat("Заголовок должен содержать поисковый запрос", googlePage.getPageTitle(), containsString("Selenium WebDriver"));
    }

    @Test
    @TestTag(id = "SMOKE_002", description = "Smoke тест логина SauceDemo", category = "UI", priority = 1)
    @Story("Smoke Tests")
    @DisplayName("Проверка основного функционала логина")
    @Description("Проверяем, что логин в SauceDemo работает")
    public void testSauceDemoLoginSmoke() {
        // Создаем Page Objects
        SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
        SauceDemoProductsPage productsPage = new SauceDemoProductsPage(driver);
        
        // Открываем страницу логина
        loginPage.openLoginPage();
        
        // Проверяем, что страница загрузилась
        assertThat("Страница логина должна быть открыта", loginPage.getCurrentUrl(), containsString("saucedemo.com"));
        assertThat("Логотип должен отображаться", loginPage.isLogoDisplayed(), is(true));
        assertThat("Поля ввода должны отображаться", loginPage.areInputFieldsDisplayed(), is(true));
        
        // Выполняем логин
        loginPage.login("standard_user", "secret_sauce");
        
        // Проверяем, что мы попали на страницу продуктов
        assertThat("Страница продуктов должна загрузиться", productsPage.isProductsPageLoaded(), is(true));
        assertThat("Продукты должны отображаться", productsPage.areProductsDisplayed(), is(true));
        assertThat("Корзина должна отображаться", productsPage.isCartDisplayed(), is(true));
    }

    @Test
    @TestTag(id = "SMOKE_003", description = "Smoke тест добавления товара в корзину", category = "UI", priority = 2)
    @Story("Smoke Tests")
    @DisplayName("Проверка добавления товара в корзину")
    @Description("Проверяем основной e-commerce функционал")
    public void testAddToCartSmoke() {
        // Создаем Page Objects
        SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
        SauceDemoProductsPage productsPage = new SauceDemoProductsPage(driver);
        
        // Логинимся
        loginPage.openLoginPage().login("standard_user", "secret_sauce");
        
        // Проверяем, что мы на странице продуктов
        assertThat("Страница продуктов должна загрузиться", productsPage.isProductsPageLoaded(), is(true));
        
        // Получаем информацию о первом продукте
        String firstProductName = productsPage.getFirstProductName();
        String firstProductPrice = productsPage.getFirstProductPrice();
        
        // Проверяем, что информация о продукте получена
        assertThat("Название продукта не должно быть пустым", firstProductName, not(emptyString()));
        assertThat("Цена продукта не должна быть пустой", firstProductPrice, not(emptyString()));
        
        // Добавляем товар в корзину
        productsPage.addFirstProductToCart();
        
        // Проверяем, что товар добавлен в корзину
        String cartItemCount = productsPage.getCartItemCount();
        assertThat("Количество товаров в корзине должно быть 1", cartItemCount, equalTo("1"));
    }

    @Test
    @TestTag(id = "SMOKE_004", description = "Smoke тест навигации", category = "UI", priority = 2)
    @Story("Smoke Tests")
    @DisplayName("Проверка навигации между страницами")
    @Description("Проверяем, что навигация работает корректно")
    public void testNavigationSmoke() {
        // Создаем Page Objects
        SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
        SauceDemoProductsPage productsPage = new SauceDemoProductsPage(driver);
        
        // Логинимся
        loginPage.openLoginPage().login("standard_user", "secret_sauce");
        
        // Проверяем, что мы на странице продуктов
        assertThat("Страница продуктов должна загрузиться", productsPage.isProductsPageLoaded(), is(true));
        
        // Добавляем товар в корзину
        productsPage.addFirstProductToCart();
        
        // Переходим в корзину
        productsPage.openCart();
        
        // Проверяем, что мы в корзине
        assertThat("URL должен содержать cart", driver.getCurrentUrl(), containsString("cart"));
    }

    @Test
    @TestTag(id = "SMOKE_005", description = "Smoke тест логаута", category = "UI", priority = 2)
    @Story("Smoke Tests")
    @DisplayName("Проверка логаута")
    @Description("Проверяем, что логаут работает корректно")
    public void testLogoutSmoke() {
        // Создаем Page Objects
        SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
        SauceDemoProductsPage productsPage = new SauceDemoProductsPage(driver);
        
        // Логинимся
        loginPage.openLoginPage().login("standard_user", "secret_sauce");
        
        // Проверяем, что мы на странице продуктов
        assertThat("Страница продуктов должна загрузиться", productsPage.isProductsPageLoaded(), is(true));
        
        // Выполняем логаут
        productsPage.logout();
        
        // Проверяем, что мы вернулись на страницу логина
        assertThat("URL должен содержать login", driver.getCurrentUrl(), containsString("login"));
        assertThat("Логотип должен отображаться", loginPage.isLogoDisplayed(), is(true));
    }
}
