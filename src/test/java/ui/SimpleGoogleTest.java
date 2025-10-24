package ui;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.TestTag;
import utils.WebDriverUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Простой тест для Google (всегда доступен)
 */
@Epic("UI Testing")
@Feature("Simple Google Tests")
public class SimpleGoogleTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    @Step("Настройка браузера")
    public void setUp() {
        driver = WebDriverUtils.createWebDriver(false); // Видимый браузер
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
    @TestTag(id = "GOOGLE_001", description = "Простой тест Google", category = "UI", priority = 1)
    @Story("Google Tests")
    @DisplayName("Проверка загрузки Google")
    @Description("Простой тест для проверки загрузки Google")
    public void testGoogleLoads() {
        // Открываем Google
        WebDriverUtils.openUrl(driver, "https://www.google.com");
        
        // Ждем загрузки страницы
        WebDriverUtils.waitForPageLoad(driver);
        
        // Проверяем заголовок страницы
        String title = driver.getTitle();
        assertThat("Заголовок должен содержать 'Google'", title, containsString("Google"));
        
        // Проверяем URL
        String currentUrl = driver.getCurrentUrl();
        assertThat("URL должен содержать 'google'", currentUrl, containsString("google"));
        
        // Проверяем наличие поисковой строки
        WebElement searchBox = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("q")));
        assertThat("Поисковая строка должна быть видна", searchBox.isDisplayed(), is(true));
        
        System.out.println("✅ Тест Google прошел успешно");
    }

    @Test
    @TestTag(id = "GOOGLE_002", description = "Тест поиска в Google", category = "UI", priority = 2)
    @Story("Google Tests")
    @DisplayName("Проверка поиска в Google")
    @Description("Тест поиска в Google")
    public void testGoogleSearch() {
        // Открываем Google
        WebDriverUtils.openUrl(driver, "https://www.google.com");
        
        // Ждем загрузки страницы
        WebDriverUtils.waitForPageLoad(driver);
        
        // Находим поисковую строку
        WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(By.name("q")));
        
        // Вводим поисковый запрос
        searchBox.sendKeys("Selenium WebDriver");
        
        // Нажимаем Enter
        searchBox.submit();
        
        // Ждем загрузки результатов
        WebDriverUtils.waitForPageLoad(driver);
        
        // Проверяем, что мы на странице результатов
        String currentUrl = driver.getCurrentUrl();
        assertThat("URL должен содержать 'search'", currentUrl, containsString("search"));
        
        // Проверяем заголовок страницы
        String title = driver.getTitle();
        assertThat("Заголовок должен содержать 'Selenium'", title, containsString("Selenium"));
        
        System.out.println("✅ Тест поиска Google прошел успешно");
    }
}
