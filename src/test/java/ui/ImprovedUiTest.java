package ui;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import utils.*;
import pages.SauceDemoLoginPage;
import pages.SauceDemoProductsPage;

import java.util.stream.Stream;

/**
 * Улучшенный UI тест с использованием всех новых утилит
 */
@Epic("UI Testing")
@Feature("Improved UI Tests")
public class ImprovedUiTest extends BaseUiTest {

    private SauceDemoLoginPage loginPage;
    private SauceDemoProductsPage productsPage;

    @BeforeEach
    @Step("Инициализация страниц")
    void setupPages() {
        loginPage = new SauceDemoLoginPage(driver);
        productsPage = new SauceDemoProductsPage(driver);
    }

    @Test
    @TestTag(id = "IMPROVED_UI_001", description = "Улучшенный тест логина", category = "UI", priority = 1)
    @Story("Improved Login")
    @DisplayName("Успешный логин с улучшенными ассертами")
    @Description("Демонстрирует использование улучшенных ассертов и автоматических скриншотов")
    public void testSuccessfulLoginImproved() {
        // Открываем страницу логина
        navigateTo(Config.getSauceDemoUrl());
        
        // Ждем загрузки страницы
        waitForPageLoad();
        
        // Проверяем, что мы на странице логина
        String currentUrl = driver.getCurrentUrl();
        AssertUtils.assertUrlContains("saucedemo.com", currentUrl, "URL должен содержать saucedemo.com");
        
        // Выполняем логин
        loginPage.login("standard_user", "secret_sauce");
        
        // Ждем перехода на страницу продуктов
        WebDriverUtils.waitForElementVisible(driver, By.className("inventory_container"), 10);
        
        // Проверяем, что логин прошел успешно
        String productsUrl = driver.getCurrentUrl();
        AssertUtils.assertUrlContains("inventory.html", productsUrl, "После логина должны быть на странице продуктов");
        
        // Проверяем наличие элементов на странице продуктов
        WebElement productsContainer = driver.findElement(By.className("inventory_container"));
        AssertUtils.assertElementVisible(productsContainer, "Products Container", "Контейнер продуктов должен быть виден");
        
        // Проверяем заголовок страницы
        String pageTitle = driver.getTitle();
        AssertUtils.assertPageTitleContains("Swag Labs", pageTitle, "Заголовок должен содержать Swag Labs");
    }

    @ParameterizedTest
    @MethodSource("utils.TestDataProvider#getSauceDemoLoginData")
    @TestTag(id = "IMPROVED_UI_002", description = "Параметризованный тест логина", category = "UI", priority = 2)
    @Story("Parameterized Login")
    @DisplayName("Тест логина с различными пользователями")
    @Description("Тестирует логин с разными типами пользователей")
    public void testLoginParameterized(String username, String password, boolean shouldSucceed, String expectedPage) {
        // Открываем страницу логина
        navigateTo(Config.getSauceDemoUrl());
        waitForPageLoad();
        
        // Выполняем логин
        loginPage.login(username, password);
        
        if (shouldSucceed) {
            // Ожидаем успешный логин
            WebDriverUtils.waitForElementVisible(driver, By.className("inventory_container"), 10);
            
            String currentUrl = driver.getCurrentUrl();
            AssertUtils.assertUrlContains("inventory.html", currentUrl, 
                String.format("Пользователь %s должен успешно войти", username));
            
            // Проверяем, что мы на странице продуктов
            WebElement productsContainer = driver.findElement(By.className("inventory_container"));
            AssertUtils.assertElementVisible(productsContainer, "Products Container", 
                "Контейнер продуктов должен быть виден после успешного логина");
                
        } else {
            // Ожидаем неуспешный логин
            WebDriverUtils.waitForElementVisible(driver, By.className("error-message-container"), 5);
            
            String currentUrl = driver.getCurrentUrl();
            AssertUtils.assertUrlContains("index.html", currentUrl, 
                String.format("Пользователь %s должен остаться на странице логина", username));
            
            // Проверяем наличие сообщения об ошибке
            WebElement errorMessage = driver.findElement(By.className("error-message-container"));
            AssertUtils.assertElementVisible(errorMessage, "Error Message", 
                "Сообщение об ошибке должно быть видно");
        }
    }

    @Test
    @TestTag(id = "IMPROVED_UI_003", description = "Тест добавления товара в корзину", category = "UI", priority = 2)
    @Story("Add to Cart")
    @DisplayName("Добавление товара в корзину")
    @Description("Тестирует добавление товара в корзину с проверками")
    public void testAddToCartImproved() {
        // Логинимся
        navigateTo(Config.getSauceDemoUrl());
        waitForPageLoad();
        loginPage.login("standard_user", "secret_sauce");
        
        // Ждем загрузки страницы продуктов
        WebDriverUtils.waitForElementVisible(driver, By.className("inventory_container"), 10);
        
        // Добавляем первый товар в корзину
        productsPage.addFirstProductToCart();
        
        // Проверяем, что товар добавлен (появляется кнопка "Remove")
        WebDriverUtils.waitForElementVisible(driver, By.xpath("//button[contains(text(), 'Remove')]"), 5);
        
        // Проверяем счетчик корзины
        WebElement cartBadge = driver.findElement(By.className("shopping_cart_badge"));
        AssertUtils.assertElementVisible(cartBadge, "Cart Badge", "Счетчик корзины должен быть виден");
        
        String cartCount = cartBadge.getText();
        AssertUtils.assertEquals("1", cartCount, "В корзине должен быть 1 товар");
        
        // Переходим в корзину
        productsPage.openCart();
        
        // Проверяем, что мы в корзине
        String cartUrl = driver.getCurrentUrl();
        AssertUtils.assertUrlContains("cart.html", cartUrl, "URL должен содержать cart.html");
        
        // Проверяем наличие товара в корзине
        WebElement cartItem = driver.findElement(By.className("cart_item"));
        AssertUtils.assertElementVisible(cartItem, "Cart Item", "Товар должен быть в корзине");
    }

    @Test
    @TestTag(id = "IMPROVED_UI_004", description = "Тест с retry механизмом", category = "UI", priority = 3)
    @Story("Retry UI Test")
    @DisplayName("UI тест с возможностью повторных попыток")
    @Description("Демонстрирует использование RetryExtension для нестабильных UI тестов")
    @ExtendWith(RetryExtension.class)
    @RetryExtension.RetryOnFailure(maxAttempts = 3, delayMs = 2000)
    public void testWithRetryMechanism() {
        // Этот тест может иногда падать из-за нестабильности UI
        navigateTo(Config.getGoogleUrl());
        waitForPageLoad();
        
        // Имитируем нестабильное поведение (15% шанс падения)
        if (Math.random() < 0.15) {
            throw new RuntimeException("Случайная ошибка UI для демонстрации retry");
        }
        
        String title = driver.getTitle();
        AssertUtils.assertPageTitleContains("Google", title, "Заголовок должен содержать Google");
    }

    @Test
    @TestTag(id = "IMPROVED_UI_005", description = "Тест производительности UI", category = "UI", priority = 4)
    @Story("UI Performance Test")
    @DisplayName("Тест производительности загрузки страниц")
    @Description("Проверяет время загрузки страниц")
    public void testPageLoadPerformance() {
        long startTime = System.currentTimeMillis();
        
        navigateTo(Config.getSauceDemoUrl());
        waitForPageLoad();
        
        long endTime = System.currentTimeMillis();
        long loadTime = endTime - startTime;
        
        // Проверяем, что страница загрузилась быстро (менее 10 секунд)
        AssertUtils.assertLessThan(10000L, loadTime, "Время загрузки страницы должно быть менее 10 секунд");
        
        // Прикрепляем метрики производительности к отчету
        Allure.addAttachment("Page Load Performance", "text/plain",
            String.format("Load Time: %d ms\nURL: %s\nTitle: %s",
                loadTime, driver.getCurrentUrl(), driver.getTitle()));
    }

    @Test
    @TestTag(id = "IMPROVED_UI_006", description = "Тест с JavaScript", category = "UI", priority = 3)
    @Story("JavaScript Test")
    @DisplayName("Тест с использованием JavaScript")
    @Description("Демонстрирует использование JavaScript в тестах")
    public void testWithJavaScript() {
        navigateTo(Config.getGoogleUrl());
        waitForPageLoad();
        
        // Выполняем JavaScript для получения информации о странице
        String pageTitle = (String) WebDriverUtils.executeJavaScript(driver, "return document.title;");
        AssertUtils.assertPageTitleContains("Google", pageTitle, "Заголовок страницы должен содержать Google");
        
        // Получаем размер окна через JavaScript
        Long windowWidth = (Long) WebDriverUtils.executeJavaScript(driver, "return window.innerWidth;");
        Long windowHeight = (Long) WebDriverUtils.executeJavaScript(driver, "return window.innerHeight;");
        
        AssertUtils.assertGreaterThan(800L, windowWidth, "Ширина окна должна быть больше 800px");
        AssertUtils.assertGreaterThan(600L, windowHeight, "Высота окна должна быть больше 600px");
        
        // Прокручиваем страницу
        WebDriverUtils.scrollDown(driver);
        try {
            Thread.sleep(1000); // Небольшая пауза для визуализации
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        WebDriverUtils.scrollUp(driver);
        
        // Прикрепляем информацию о JavaScript к отчету
        Allure.addAttachment("JavaScript Info", "text/plain",
            String.format("Page Title: %s\nWindow Size: %dx%d\nURL: %s",
                pageTitle, windowWidth, windowHeight, driver.getCurrentUrl()));
    }

    @Test
    @TestTag(id = "IMPROVED_UI_007", description = "Тест очистки данных", category = "UI", priority = 2)
    @Story("Data Cleanup Test")
    @DisplayName("Тест очистки cookies и localStorage")
    @Description("Демонстрирует очистку данных браузера")
    public void testDataCleanup() {
        // Открываем страницу и добавляем данные
        navigateTo(Config.getSauceDemoUrl());
        waitForPageLoad();
        
        // Добавляем данные в localStorage через JavaScript
        WebDriverUtils.executeJavaScript(driver, "localStorage.setItem('testKey', 'testValue');");
        WebDriverUtils.executeJavaScript(driver, "sessionStorage.setItem('sessionKey', 'sessionValue');");
        
        // Проверяем, что данные добавлены
        String localStorageValue = (String) WebDriverUtils.executeJavaScript(driver, "return localStorage.getItem('testKey');");
        String sessionStorageValue = (String) WebDriverUtils.executeJavaScript(driver, "return sessionStorage.getItem('sessionKey');");
        
        AssertUtils.assertEquals("testValue", localStorageValue, "Значение в localStorage должно быть сохранено");
        AssertUtils.assertEquals("sessionValue", sessionStorageValue, "Значение в sessionStorage должно быть сохранено");
        
        // Очищаем данные
        WebDriverUtils.clearLocalStorage(driver);
        WebDriverUtils.clearSessionStorage(driver);
        WebDriverUtils.clearCookies(driver);
        
        // Проверяем, что данные очищены
        String clearedLocalStorage = (String) WebDriverUtils.executeJavaScript(driver, "return localStorage.getItem('testKey');");
        String clearedSessionStorage = (String) WebDriverUtils.executeJavaScript(driver, "return sessionStorage.getItem('sessionKey');");
        
        AssertUtils.assertNull(clearedLocalStorage, "localStorage должен быть очищен");
        AssertUtils.assertNull(clearedSessionStorage, "sessionStorage должен быть очищен");
    }

    // ==================== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ====================

    /**
     * Провайдер данных для UI тестов
     */
    public static Stream<Arguments> getCustomUiTestData() {
        return Stream.of(
            Arguments.of("standard_user", "secret_sauce", true),
            Arguments.of("problem_user", "secret_sauce", true),
            Arguments.of("locked_out_user", "secret_sauce", false)
        );
    }
}

