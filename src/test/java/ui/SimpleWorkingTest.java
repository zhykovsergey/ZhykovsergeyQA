package ui;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import pages.SauceDemoLoginPage;
import pages.SauceDemoProductsPage;
import utils.TestTag;
import utils.WebDriverUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Простой рабочий тест для проверки базовой функциональности
 */
@Epic("UI Testing")
@Feature("Simple Working Tests")
public class SimpleWorkingTest {

    private WebDriver driver;

    @BeforeEach
    @Step("Настройка браузера")
    public void setUp() {
        driver = WebDriverUtils.createWebDriver(false); // Видимый браузер для отладки
    }

    @AfterEach
    @Step("Закрытие браузера")
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @TestTag(id = "SIMPLE_001", description = "Простой тест логина и добавления в корзину", category = "UI", priority = 1)
    @Story("Simple Tests")
    @DisplayName("Проверка логина и добавления товара в корзину")
    @Description("Простой тест для проверки базовой функциональности")
    public void testSimpleLoginAndAddToCart() {
        // Создаем Page Objects
        SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
        SauceDemoProductsPage productsPage = new SauceDemoProductsPage(driver);
        
        // Логинимся
        loginPage.openLoginPage().login("standard_user", "secret_sauce");
        
        // Ждем загрузки страницы продуктов
        WebDriverUtils.waitForPageLoad(driver);
        
        // Проверяем, что мы на странице продуктов
        assertThat("URL должен содержать inventory", 
                driver.getCurrentUrl(), containsString("inventory"));
        
        // Добавляем товар в корзину
        productsPage.addFirstProductToCart();
        
        // Ждем обновления счетчика корзины
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Проверяем, что товар добавлен в корзину
        String cartCount = productsPage.getCartItemCount();
        assertThat("Количество товаров в корзине должно быть больше 0", 
                cartCount, not(equalTo("0")));
        
        // Переходим в корзину
        productsPage.openCart();
        
        // Ждем загрузки страницы корзины
        WebDriverUtils.waitForPageLoad(driver);
        
        // Проверяем, что мы в корзине
        assertThat("URL должен содержать cart", 
                driver.getCurrentUrl(), containsString("cart"));
    }
}
