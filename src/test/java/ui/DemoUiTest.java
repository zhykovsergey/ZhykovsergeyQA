package ui;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DisplayName;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.TestTag;
import utils.WebDriverUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Демонстрационный UI тест с видимым браузером
 * Показывает, как работают UI тесты
 */
@Epic("UI Testing")
@Feature("Demo UI Tests")
public class DemoUiTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    @Step("Настройка браузера")
    public void setUp() {
        // Используем видимый браузер для демонстрации
        driver = WebDriverUtils.createWebDriver(false);
        wait = WebDriverUtils.createWebDriverWait(driver);
        System.out.println("🚀 Браузер запущен! Вы должны видеть окно Chrome.");
    }

    @AfterEach
    @Step("Закрытие браузера")
    public void tearDown() {
        System.out.println("🔒 Закрываем браузер...");
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @TestTag(id = "DEMO001", description = "Демонстрация работы браузера", category = "UI", priority = 1)
    @Story("Browser Demo")
    @DisplayName("Демонстрация работы браузера")
    @Description("Показывает, как работает браузер в UI тестах")
    public void testBrowserDemo() {
        try {
            System.out.println("🌐 Шаг 1: Открываем Google...");
            WebDriverUtils.openUrl(driver, "https://www.google.com");
            
            // Ждем, чтобы увидеть процесс
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            System.out.println("🔍 Шаг 2: Проверяем заголовок...");
            String title = WebDriverUtils.getPageTitle(driver);
            assertThat("Заголовок должен содержать Google", title, containsString("Google"));
            System.out.println("✅ Заголовок: " + title);
            
            System.out.println("🌐 Шаг 3: Переходим на YouTube...");
            WebDriverUtils.openUrl(driver, "https://www.youtube.com");
            
            // Ждем, чтобы увидеть процесс
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            System.out.println("🔍 Шаг 4: Проверяем YouTube...");
            String youtubeTitle = WebDriverUtils.getPageTitle(driver);
            assertThat("Заголовок должен содержать YouTube", youtubeTitle, containsString("YouTube"));
            System.out.println("✅ YouTube заголовок: " + youtubeTitle);
            
            System.out.println("🎉 Демонстрация завершена успешно!");
            
        } catch (Exception e) {
            System.err.println("❌ Ошибка в демонстрации: " + e.getMessage());
            throw e;
        }
    }

    @Test
    @TestTag(id = "DEMO002", description = "Демонстрация навигации", category = "UI", priority = 2)
    @Story("Navigation Demo")
    @DisplayName("Демонстрация навигации")
    @Description("Показывает навигацию между сайтами")
    public void testNavigationDemo() {
        try {
            System.out.println("🌐 Навигация по сайтам...");
            
            // GitHub
            System.out.println("📂 Открываем GitHub...");
            WebDriverUtils.openUrl(driver, "https://github.com");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            String githubTitle = WebDriverUtils.getPageTitle(driver);
            System.out.println("✅ GitHub: " + githubTitle);
            
            // Stack Overflow
            System.out.println("❓ Переходим на Stack Overflow...");
            WebDriverUtils.openUrl(driver, "https://stackoverflow.com");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            String stackTitle = WebDriverUtils.getPageTitle(driver);
            System.out.println("✅ Stack Overflow: " + stackTitle);
            
            // Reddit
            System.out.println("🔴 Идем на Reddit...");
            WebDriverUtils.openUrl(driver, "https://www.reddit.com");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            String redditTitle = WebDriverUtils.getPageTitle(driver);
            System.out.println("✅ Reddit: " + redditTitle);
            
            System.out.println("🎉 Навигация завершена!");
            
        } catch (Exception e) {
            System.err.println("❌ Ошибка в навигации: " + e.getMessage());
            throw e;
        }
    }
}
