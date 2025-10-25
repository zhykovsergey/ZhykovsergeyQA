package ui;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import utils.*;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Рабочие UI тесты для демонстрации
 */
@Epic("UI Testing")
@Feature("Working UI Tests")
@ExtendWith(RetryExtension.class)
public class WorkingUiTest extends BaseUiTest {

    @Test
    @TestTag(id = "UI_012", description = "Проверка загрузки Google", category = "UI", priority = 1)
    @Story("Basic UI Tests")
    @DisplayName("Проверка загрузки Google")
    @Description("Проверяем базовую функциональность загрузки страницы Google")
    @Severity(SeverityLevel.CRITICAL)
    public void testGooglePageLoad() {
        step("Открываем Google", () -> {
            driver.get("https://www.google.com");
        });

        step("Проверяем заголовок страницы", () -> {
            String pageTitle = driver.getTitle();
            assertNotNull(pageTitle, "Заголовок страницы не должен быть null");
            assertTrue(pageTitle.contains("Google"), "Заголовок должен содержать 'Google'");
        });

        step("Проверяем URL страницы", () -> {
            String currentUrl = driver.getCurrentUrl();
            assertTrue(currentUrl.contains("google.com"), "URL должен содержать 'google.com'");
        });
    }

    @Test
    @TestTag(id = "UI_013", description = "Проверка загрузки SauceDemo", category = "UI", priority = 1)
    @Story("Basic UI Tests")
    @DisplayName("Проверка загрузки SauceDemo")
    @Description("Проверяем базовую функциональность загрузки страницы SauceDemo")
    @Severity(SeverityLevel.CRITICAL)
    public void testSauceDemoPageLoad() {
        step("Открываем SauceDemo", () -> {
            driver.get("https://www.saucedemo.com");
        });

        step("Проверяем заголовок страницы", () -> {
            String pageTitle = driver.getTitle();
            assertNotNull(pageTitle, "Заголовок страницы не должен быть null");
            assertTrue(pageTitle.contains("Swag Labs"), "Заголовок должен содержать 'Swag Labs'");
        });

        step("Проверяем URL страницы", () -> {
            String currentUrl = driver.getCurrentUrl();
            assertTrue(currentUrl.contains("saucedemo.com"), "URL должен содержать 'saucedemo.com'");
        });
    }

    @Test
    @TestTag(id = "UI_014", description = "Проверка навигации между страницами", category = "UI", priority = 2)
    @Story("Navigation Tests")
    @DisplayName("Проверка навигации между страницами")
    @Description("Проверяем навигацию между разными страницами")
    @Severity(SeverityLevel.NORMAL)
    public void testPageNavigation() {
        step("Открываем первую страницу", () -> {
            driver.get("https://www.google.com");
        });

        step("Проверяем, что мы на Google", () -> {
            String currentUrl = driver.getCurrentUrl();
            assertTrue(currentUrl.contains("google.com"), "Должны быть на Google");
        });

        step("Переходим на вторую страницу", () -> {
            driver.get("https://www.saucedemo.com");
        });

        step("Проверяем, что мы на SauceDemo", () -> {
            String currentUrl = driver.getCurrentUrl();
            assertTrue(currentUrl.contains("saucedemo.com"), "Должны быть на SauceDemo");
        });

        step("Возвращаемся на первую страницу", () -> {
            driver.navigate().back();
        });

        step("Проверяем, что мы вернулись на Google", () -> {
            String currentUrl = driver.getCurrentUrl();
            assertTrue(currentUrl.contains("google.com"), "Должны вернуться на Google");
        });
    }

    @Test
    @TestTag(id = "UI_015", description = "Проверка размеров окна", category = "UI", priority = 3)
    @Story("Window Management Tests")
    @DisplayName("Проверка размеров окна")
    @Description("Проверяем управление размерами окна браузера")
    @Severity(SeverityLevel.MINOR)
    public void testWindowSize() {
        step("Открываем страницу", () -> {
            driver.get("https://www.google.com");
        });

        step("Проверяем текущий размер окна", () -> {
            var windowSize = driver.manage().window().getSize();
            assertTrue(windowSize.getWidth() > 0, "Ширина окна должна быть больше 0");
            assertTrue(windowSize.getHeight() > 0, "Высота окна должна быть больше 0");
        });

        step("Изменяем размер окна", () -> {
            driver.manage().window().setSize(new org.openqa.selenium.Dimension(800, 600));
        });

        step("Проверяем новый размер окна", () -> {
            var windowSize = driver.manage().window().getSize();
            assertEquals(800, windowSize.getWidth(), "Ширина окна должна быть 800");
            assertEquals(600, windowSize.getHeight(), "Высота окна должна быть 600");
        });
    }
}