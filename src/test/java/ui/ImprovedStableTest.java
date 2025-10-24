package ui;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import pages.SauceDemoLoginPage;
import pages.SauceDemoProductsPage;
import utils.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Улучшенный стабильный UI тест с новой системой ожиданий
 */
@Epic("UI Testing")
@Feature("Improved Stable Tests")
@ExtendWith(RetryExtension.class)
public class ImprovedStableTest extends BaseUiTest {

    @Test
    @TestTag(id = "STABLE_UI_001", description = "Стабильный тест логина и добавления в корзину", category = "UI", priority = 1)
    @Story("Stable Login and Cart")
    @DisplayName("Проверить стабильный логин и добавление товара в корзину")
    @Description("Этот тест использует улучшенную систему ожиданий для максимальной стабильности")
    @RetryExtension.RetryOnFailure(maxAttempts = 3, delayMs = 2000, exponentialBackoff = true)
    public void testStableLoginAndAddToCart() {
        try {
            // Создаем Page Objects
            SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
            SauceDemoProductsPage productsPage = new SauceDemoProductsPage(driver);

            // Логинимся с улучшенными ожиданиями
            logAction("Начало процесса логина");
            loginPage.openLoginPage();
            
            // Ждем загрузки страницы логина
            waitForPageLoad();
            
            // Логинимся
            loginPage.login("standard_user", "secret_sauce");
            
            // Ждем загрузки страницы продуктов
            waitForPageLoad();
            
            // Проверяем, что мы на странице продуктов
            assertThat("URL должен содержать inventory", 
                driver.getCurrentUrl(), containsString("inventory"));
            
            logAction("Логин выполнен успешно");

            // Добавляем товар в корзину с улучшенными ожиданиями
            logAction("Добавление товара в корзину");
            productsPage.addFirstProductToCart();
            
            // Ждем обновления счетчика корзины
            waitForElementVisible(By.className("shopping_cart_badge"));
            
            // Проверяем, что товар добавлен
            String cartCount = productsPage.getCartItemCount();
            assertThat("Количество товаров в корзине должно быть 1", 
                cartCount, containsString("1"));
            
            logAction("Товар успешно добавлен в корзину");

            // Переходим в корзину
            logAction("Переход в корзину");
            productsPage.openCart();
            
            // Ждем загрузки страницы корзины
            waitForPageLoad();
            
            // Проверяем, что мы в корзине
            assertThat("URL должен содержать cart", 
                driver.getCurrentUrl(), containsString("cart"));
            
            logAction("Успешно перешли в корзину");

        } catch (Exception e) {
            attachError(e);
            throw e;
        }
    }

    @Test
    @TestTag(id = "STABLE_UI_002", description = "Стабильный тест с повторными попытками", category = "UI", priority = 2)
    @Story("Stable Retry Test")
    @DisplayName("Проверить стабильность с повторными попытками")
    @Description("Этот тест демонстрирует работу retry механизма")
    @RetryExtension.RetryOnFailure(maxAttempts = 5, delayMs = 1000, exponentialBackoff = true)
    public void testStableWithRetry() {
        try {
            // Создаем Page Objects
            SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
            SauceDemoProductsPage productsPage = new SauceDemoProductsPage(driver);

            // Логинимся
            logAction("Начало стабильного теста с retry");
            loginPage.openLoginPage();
            waitForPageLoad();
            loginPage.login("standard_user", "secret_sauce");
            waitForPageLoad();

            // Проверяем, что мы на странице продуктов
            assertThat("URL должен содержать inventory", 
                driver.getCurrentUrl(), containsString("inventory"));

            // Добавляем товар в корзину
            productsPage.addFirstProductToCart();
            
            // Ждем обновления счетчика корзины с повторными попытками
            waitForElementWithRetry(By.className("shopping_cart_badge"), 3);
            
            // Проверяем результат
            String cartCount = productsPage.getCartItemCount();
            assertThat("Количество товаров в корзине должно быть 1", 
                cartCount, containsString("1"));

            logAction("Стабильный тест с retry выполнен успешно");

        } catch (Exception e) {
            attachError(e);
            throw e;
        }
    }

    @Test
    @TestTag(id = "STABLE_UI_003", description = "Тест с кастомными ожиданиями", category = "UI", priority = 3)
    @Story("Custom Wait Test")
    @DisplayName("Проверить работу кастомных ожиданий")
    @Description("Этот тест демонстрирует использование кастомных ожиданий")
    public void testCustomWaits() {
        try {
            // Создаем Page Objects
            SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
            SauceDemoProductsPage productsPage = new SauceDemoProductsPage(driver);

            // Логинимся
            logAction("Начало теста с кастомными ожиданиями");
            loginPage.openLoginPage();
            
            // Используем кастомное ожидание для страницы логина
            waitForElement(By.id("login-button"), 15);
            
            loginPage.login("standard_user", "secret_sauce");
            
            // Используем кастомное ожидание для страницы продуктов
            waitForElement(By.className("title"), 20);
            
            // Проверяем, что мы на странице продуктов
            assertThat("URL должен содержать inventory", 
                driver.getCurrentUrl(), containsString("inventory"));

            // Добавляем товар в корзину
            productsPage.addFirstProductToCart();
            
            // Используем кастомное ожидание для счетчика корзины
            waitForElementClickable(By.className("shopping_cart_badge"));
            
            // Проверяем результат
            String cartCount = productsPage.getCartItemCount();
            assertThat("Количество товаров в корзине должно быть 1", 
                cartCount, containsString("1"));

            logAction("Тест с кастомными ожиданиями выполнен успешно");

        } catch (Exception e) {
            attachError(e);
            throw e;
        }
    }
}
