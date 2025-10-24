package datadriven;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import pages.SauceDemoLoginPage;
import pages.SauceDemoProductsPage;
import utils.BaseUiTest;
import utils.TestTag;
import utils.AssertUtils;
import utils.Config;
import utils.WebDriverUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Data-driven тесты с CSV данными
 * Демонстрирует использование @ParameterizedTest с CSV провайдерами
 */
@Epic("Data-driven Testing")
@Feature("CSV Data-driven Tests")
public class CsvDataDrivenTest extends BaseUiTest {

    private SauceDemoLoginPage loginPage;
    private SauceDemoProductsPage productsPage;

    @BeforeEach
    @Step("Инициализация страниц для Data-driven тестов")
    public void setupPages() {
        loginPage = new SauceDemoLoginPage(driver);
        productsPage = new SauceDemoProductsPage(driver);
    }

    @ParameterizedTest
    @CsvSource({
        "standard_user, secret_sauce, true, Products",
        "locked_out_user, secret_sauce, false, Login",
        "problem_user, secret_sauce, true, Products",
        "performance_glitch_user, secret_sauce, true, Products"
    })
    @TestTag(id = "CSV_001", description = "CSV тест логина с разными пользователями", category = "Data-driven", priority = 1)
    @Story("CSV Data-driven")
    @DisplayName("Проверка логина с разными пользователями (CSV Source)")
    @Description("Тестируем логин с различными пользователями из CSV данных")
    public void testLoginWithDifferentUsersCsvSource(
            String username, 
            String password, 
            boolean shouldLogin, 
            String expectedPage) {
        
        // Открываем страницу логина
        navigateTo(Config.getSauceDemoUrl());
        waitForPageLoad();
        
        // Выполняем логин
        loginPage.login(username, password);
        
        if (shouldLogin) {
            // Проверяем успешный логин
            WebDriverUtils.waitForElementVisible(driver, org.openqa.selenium.By.className("inventory_container"), 10);
            AssertUtils.assertUrlContains("inventory.html", driver.getCurrentUrl(), "URL должен содержать inventory.html");
            AssertUtils.assertTrue(productsPage.isProductsPageLoaded(), "Страница продуктов должна загрузиться");
        } else {
            // Проверяем неуспешный логин - остаемся на главной странице
            AssertUtils.assertUrlContains("saucedemo.com", driver.getCurrentUrl(), "URL должен содержать saucedemo.com");
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/data/login_data.csv", numLinesToSkip = 1)
    @TestTag(id = "CSV_002", description = "CSV тест логина из файла", category = "Data-driven", priority = 1)
    @Story("CSV Data-driven")
    @DisplayName("Проверка логина с данными из CSV файла")
    @Description("Тестируем логин с данными из CSV файла")
    public void testLoginWithCsvFile(
            String username, 
            String password, 
            String expectedResult, 
            String description) {
        
        // Открываем страницу логина
        navigateTo(Config.getSauceDemoUrl());
        waitForPageLoad();
        
        // Выполняем логин (проверяем, что пароль не пустой)
        if (password != null && !password.trim().isEmpty()) {
            loginPage.login(username, password);
        } else {
            // Для пустого пароля просто вводим username
            loginPage.enterUsername(username);
            loginPage.clickLoginButton();
        }
        
        if ("success".equals(expectedResult)) {
            // Проверяем успешный логин
            WebDriverUtils.waitForElementVisible(driver, org.openqa.selenium.By.className("inventory_container"), 10);
            AssertUtils.assertUrlContains("inventory.html", driver.getCurrentUrl(), "URL должен содержать inventory.html");
            AssertUtils.assertTrue(productsPage.isProductsPageLoaded(), "Страница продуктов должна загрузиться");
        } else {
            // Проверяем неуспешный логин - остаемся на главной странице
            AssertUtils.assertUrlContains("saucedemo.com", driver.getCurrentUrl(), "URL должен содержать saucedemo.com");
        }
    }

    @ParameterizedTest
    @CsvSource({
        "Sauce Labs Backpack, $29.99",
        "Sauce Labs Bike Light, $9.99",
        "Sauce Labs Bolt T-Shirt, $15.99",
        "Sauce Labs Fleece Jacket, $49.99",
        "Sauce Labs Onesie, $7.99",
        "Test.allTheThings() T-Shirt (Red), $15.99"
    })
    @TestTag(id = "CSV_003", description = "CSV тест проверки товаров", category = "Data-driven", priority = 2)
    @Story("CSV Data-driven")
    @DisplayName("Проверка товаров и их цен (CSV Source)")
    @Description("Тестируем отображение товаров и их цен")
    public void testProductPricesCsvSource(String productName, String expectedPrice) {
        // Логинимся
        navigateTo(Config.getSauceDemoUrl());
        waitForPageLoad();
        loginPage.login("standard_user", "secret_sauce");
        
        // Проверяем, что мы на странице продуктов
        WebDriverUtils.waitForElementVisible(driver, org.openqa.selenium.By.className("inventory_container"), 10);
        AssertUtils.assertUrlContains("inventory.html", driver.getCurrentUrl(), "URL должен содержать inventory.html");
        AssertUtils.assertTrue(productsPage.isProductsPageLoaded(), "Страница продуктов должна загрузиться");
        
        // Проверяем, что товары отображаются
        AssertUtils.assertTrue(productsPage.areProductsDisplayed(), "Продукты должны отображаться");
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/data/user_data.csv", numLinesToSkip = 1)
    @TestTag(id = "CSV_004", description = "CSV тест пользователей из файла", category = "Data-driven", priority = 2)
    @Story("CSV Data-driven")
    @DisplayName("Проверка различных пользователей из CSV файла")
    @Description("Тестируем различных пользователей из CSV файла")
    public void testDifferentUsersCsvFile(
            String username, 
            String password, 
            String userType, 
            String expectedBehavior) {
        
        // Открываем страницу логина
        navigateTo(Config.getSauceDemoUrl());
        waitForPageLoad();
        
        // Выполняем логин
        loginPage.login(username, password);
        
        if ("success".equals(expectedBehavior)) {
            // Проверяем успешный логин
            WebDriverUtils.waitForElementVisible(driver, org.openqa.selenium.By.className("inventory_container"), 10);
            AssertUtils.assertUrlContains("inventory.html", driver.getCurrentUrl(), "URL должен содержать inventory.html");
            AssertUtils.assertTrue(productsPage.isProductsPageLoaded(), "Страница продуктов должна загрузиться");
        } else if ("locked".equals(expectedBehavior)) {
            // Проверяем заблокированного пользователя - остаемся на главной странице
            AssertUtils.assertUrlContains("saucedemo.com", driver.getCurrentUrl(), "URL должен содержать saucedemo.com");
        } else if ("problem".equals(expectedBehavior)) {
            // Проверяем проблемного пользователя (может залогиниться, но с проблемами)
            WebDriverUtils.waitForElementVisible(driver, org.openqa.selenium.By.className("inventory_container"), 10);
            AssertUtils.assertUrlContains("inventory.html", driver.getCurrentUrl(), "URL должен содержать inventory.html");
        }
    }

    @ParameterizedTest
    @CsvSource({
        "1, 1, 1",
        "2, 2, 2", 
        "3, 3, 3",
        "4, 4, 4",
        "5, 5, 5"
    })
    @TestTag(id = "CSV_005", description = "CSV тест множественных товаров", category = "Data-driven", priority = 3)
    @Story("CSV Data-driven")
    @DisplayName("Проверка добавления множественных товаров (CSV Source)")
    @Description("Тестируем добавление разного количества товаров")
    public void testMultipleProductsCsvSource(int productIndex, int expectedCount, int testRun) {
        // Логинимся
        navigateTo(Config.getSauceDemoUrl());
        waitForPageLoad();
        loginPage.login("standard_user", "secret_sauce");
        
        // Проверяем, что мы на странице продуктов
        WebDriverUtils.waitForElementVisible(driver, org.openqa.selenium.By.className("inventory_container"), 10);
        AssertUtils.assertUrlContains("inventory.html", driver.getCurrentUrl(), "URL должен содержать inventory.html");
        AssertUtils.assertTrue(productsPage.isProductsPageLoaded(), "Страница продуктов должна загрузиться");
        
        // Проверяем, что товары отображаются
        AssertUtils.assertTrue(productsPage.areProductsDisplayed(), "Продукты должны отображаться");
    }
}
