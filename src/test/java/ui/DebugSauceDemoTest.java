package ui;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DisplayName;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.SauceDemoLoginPage;
import pages.SauceDemoProductsPage;
import utils.TestTag;
import utils.WebDriverUtils;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Отладочные тесты для SauceDemo
 */
@Epic("UI Testing")
@Feature("Debug SauceDemo Tests")
public class DebugSauceDemoTest {

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
    @TestTag(id = "DEBUG_001", description = "Отладка элементов на странице продуктов", category = "UI", priority = 1)
    @Story("Debug Tests")
    @DisplayName("Проверка элементов на странице продуктов")
    @Description("Отладочный тест для проверки доступных элементов")
    public void testDebugProductsPage() {
        // Логинимся
        SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
        loginPage.openLoginPage().login("standard_user", "secret_sauce");
        
        // Ждем загрузки страницы
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Проверяем URL
        System.out.println("Current URL: " + driver.getCurrentUrl());
        
        // Ищем все ссылки на странице
        List<WebElement> links = driver.findElements(By.tagName("a"));
        System.out.println("Found " + links.size() + " links on the page:");
        for (WebElement link : links) {
            String href = link.getAttribute("href");
            String text = link.getText();
            String className = link.getAttribute("class");
            System.out.println("Link: " + text + " | href: " + href + " | class: " + className);
        }
        
        // Ищем элементы с классом shopping_cart
        List<WebElement> cartElements = driver.findElements(By.className("shopping_cart_link"));
        System.out.println("Found " + cartElements.size() + " elements with class 'shopping_cart_link'");
        
        // Ищем элементы с ID shopping_cart
        List<WebElement> cartById = driver.findElements(By.id("shopping_cart_container"));
        System.out.println("Found " + cartById.size() + " elements with id 'shopping_cart_container'");
        
        // Ищем все элементы с текстом "cart"
        List<WebElement> cartByText = driver.findElements(By.xpath("//*[contains(text(), 'cart') or contains(text(), 'Cart')]"));
        System.out.println("Found " + cartByText.size() + " elements containing 'cart' text");
        
        // Проверяем, что мы на странице продуктов
        assertThat("URL должен содержать inventory", 
                driver.getCurrentUrl(), containsString("inventory"));
    }

    @Test
    @TestTag(id = "DEBUG_002", description = "Отладка добавления товара в корзину", category = "UI", priority = 1)
    @Story("Debug Tests")
    @DisplayName("Проверка добавления товара в корзину")
    @Description("Отладочный тест для проверки кнопок добавления в корзину")
    public void testDebugAddToCart() {
        // Логинимся
        SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
        loginPage.openLoginPage().login("standard_user", "secret_sauce");
        
        // Ждем загрузки страницы
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Ищем кнопки добавления в корзину
        List<WebElement> addToCartButtons = driver.findElements(By.className("btn_inventory"));
        System.out.println("Found " + addToCartButtons.size() + " 'Add to cart' buttons");
        
        if (addToCartButtons.size() > 0) {
            WebElement firstButton = addToCartButtons.get(0);
            System.out.println("First button text: " + firstButton.getText());
            System.out.println("First button class: " + firstButton.getAttribute("class"));
            
            // Кликаем по первой кнопке
            firstButton.click();
            System.out.println("Clicked on first 'Add to cart' button");
            
            // Ждем немного
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            // Проверяем, изменился ли текст кнопки
            System.out.println("Button text after click: " + firstButton.getText());
        }
        
        // Проверяем, что мы все еще на странице продуктов
        assertThat("URL должен содержать inventory", 
                driver.getCurrentUrl(), containsString("inventory"));
    }
}
