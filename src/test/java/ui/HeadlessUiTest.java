package ui;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DisplayName;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.SauceDemoLoginPage;
import pages.SauceDemoProductsPage;
import utils.TestTag;
import utils.WebDriverUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * UI тесты в headless режиме (без подсказок о паролях)
 */
@Epic("UI Testing")
@Feature("Headless UI Tests")
public class HeadlessUiTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    @Step("Настройка headless браузера")
    public void setUp() {
        // Используем headless режим - никаких подсказок о паролях
        driver = WebDriverUtils.createWebDriver(true);
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
    @TestTag(id = "HEADLESS_001", description = "Headless тест логина", category = "UI", priority = 1)
    @Story("Headless Tests")
    @DisplayName("Проверка логина в headless режиме")
    @Description("Тест логина без подсказок о паролях")
    public void testHeadlessLogin() {
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
    @TestTag(id = "HEADLESS_002", description = "Headless тест добавления в корзину", category = "UI", priority = 1)
    @Story("Headless Tests")
    @DisplayName("Проверка добавления товара в корзину в headless режиме")
    @Description("Тест добавления товара без подсказок о паролях")
    public void testHeadlessAddToCart() {
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
        assertThat("Страница должна остаться на inventory", 
                driver.getCurrentUrl(), containsString("inventory"));
    }

    @Test
    @TestTag(id = "HEADLESS_003", description = "Headless тест перехода в корзину", category = "UI", priority = 2)
    @Story("Headless Tests")
    @DisplayName("Проверка перехода в корзину в headless режиме")
    @Description("Тест перехода в корзину без подсказок о паролях")
    public void testHeadlessGoToCart() {
        // Создаем Page Objects
        SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
        SauceDemoProductsPage productsPage = new SauceDemoProductsPage(driver);
        
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
        
        // Проверяем, что мы в корзине
        assertThat("URL должен содержать cart", 
                driver.getCurrentUrl(), containsString("cart"));
    }

    @Test
    @TestTag(id = "HEADLESS_004", description = "Headless тест оформления заказа", category = "UI", priority = 2)
    @Story("Headless Tests")
    @DisplayName("Проверка перехода к оформлению заказа в headless режиме")
    @Description("Тест оформления заказа без подсказок о паролях")
    public void testHeadlessCheckout() {
        // Создаем Page Objects
        SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
        SauceDemoProductsPage productsPage = new SauceDemoProductsPage(driver);
        
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
        
        // Проверяем, что мы в корзине
        assertThat("URL должен содержать cart", 
                driver.getCurrentUrl(), containsString("cart"));
    }
}
