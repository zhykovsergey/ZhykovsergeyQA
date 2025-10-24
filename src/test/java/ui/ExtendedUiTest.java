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
 * Расширенные UI тесты
 */
@Epic("UI Testing")
@Feature("Extended UI Tests")
public class ExtendedUiTest {

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
    @TestTag(id = "UI003", description = "Проверка GitHub", category = "UI", priority = 1)
    @Story("GitHub Test")
    @DisplayName("Проверка GitHub")
    @Description("Проверяем открытие GitHub")
    public void testGitHub() {
        try {
            WebDriverUtils.openUrl(driver, "https://github.com");
            
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            String title = WebDriverUtils.getPageTitle(driver);
            assertThat("Заголовок должен содержать GitHub", title, containsString("GitHub"));
            
            String currentUrl = WebDriverUtils.getCurrentUrl(driver);
            assertThat("URL должен содержать github.com", currentUrl, containsString("github.com"));
            
            System.out.println("✅ GitHub тест прошел успешно");
            
        } catch (Exception e) {
            System.err.println("❌ Ошибка в GitHub тесте: " + e.getMessage());
            throw e;
        }
    }

    @Test
    @TestTag(id = "UI004", description = "Проверка Stack Overflow", category = "UI", priority = 2)
    @Story("Stack Overflow Test")
    @DisplayName("Проверка Stack Overflow")
    @Description("Проверяем открытие Stack Overflow")
    public void testStackOverflow() {
        try {
            WebDriverUtils.openUrl(driver, "https://stackoverflow.com");
            
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            String title = WebDriverUtils.getPageTitle(driver);
            assertThat("Заголовок должен содержать Stack Overflow", title, containsString("Stack Overflow"));
            
            String currentUrl = WebDriverUtils.getCurrentUrl(driver);
            assertThat("URL должен содержать stackoverflow.com", currentUrl, containsString("stackoverflow.com"));
            
            System.out.println("✅ Stack Overflow тест прошел успешно");
            
        } catch (Exception e) {
            System.err.println("❌ Ошибка в Stack Overflow тесте: " + e.getMessage());
            throw e;
        }
    }

    @Test
    @TestTag(id = "UI005", description = "Проверка Reddit", category = "UI", priority = 3)
    @Story("Reddit Test")
    @DisplayName("Проверка Reddit")
    @Description("Проверяем открытие Reddit")
    public void testReddit() {
        try {
            WebDriverUtils.openUrl(driver, "https://www.reddit.com");
            
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            String title = WebDriverUtils.getPageTitle(driver);
            assertThat("Заголовок должен содержать Reddit", title, containsString("Reddit"));
            
            String currentUrl = WebDriverUtils.getCurrentUrl(driver);
            assertThat("URL должен содержать reddit.com", currentUrl, containsString("reddit.com"));
            
            System.out.println("✅ Reddit тест прошел успешно");
            
        } catch (Exception e) {
            System.err.println("❌ Ошибка в Reddit тесте: " + e.getMessage());
            throw e;
        }
    }
}
