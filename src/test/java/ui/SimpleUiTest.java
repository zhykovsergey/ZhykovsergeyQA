package ui;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import utils.*;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Простые UI тесты для демонстрации
 */
@Epic("UI Testing")
@Feature("Simple UI Tests")
@ExtendWith(RetryExtension.class)
public class SimpleUiTest extends BaseUiTest {

    @Test
    @TestTag(id = "UI_009", description = "Проверка загрузки страницы", category = "UI", priority = 1)
    @Story("Basic UI Tests")
    @DisplayName("Проверка загрузки страницы")
    @Description("Проверяем базовую функциональность загрузки страницы")
    @Severity(SeverityLevel.CRITICAL)
    public void testPageLoad() {
        step("Открываем тестовую страницу", () -> {
            driver.get("https://www.google.com");
        });

        step("Проверяем, что страница загрузилась", () -> {
            String pageTitle = driver.getTitle();
            assertNotNull(pageTitle, "Заголовок страницы не должен быть null");
            assertFalse(pageTitle.isEmpty(), "Заголовок страницы не должен быть пустым");
        });

        step("Проверяем URL страницы", () -> {
            String currentUrl = driver.getCurrentUrl();
            assertTrue(currentUrl.contains("google.com"), "URL должен содержать 'google.com'");
        });
    }

    @Test
    @TestTag(id = "UI_010", description = "Проверка навигации", category = "UI", priority = 2)
    @Story("Basic UI Tests")
    @DisplayName("Проверка навигации")
    @Description("Проверяем базовую навигацию между страницами")
    @Severity(SeverityLevel.NORMAL)
    public void testNavigation() {
        step("Открываем первую страницу", () -> {
            driver.get("https://www.google.com");
        });

        step("Переходим на вторую страницу", () -> {
            driver.get("https://www.saucedemo.com");
        });

        step("Проверяем, что мы на правильной странице", () -> {
            String currentUrl = driver.getCurrentUrl();
            assertTrue(currentUrl.contains("saucedemo.com"), "URL должен содержать 'saucedemo.com'");
        });

        step("Возвращаемся на первую страницу", () -> {
            driver.navigate().back();
        });

        step("Проверяем, что мы вернулись", () -> {
            String currentUrl = driver.getCurrentUrl();
            assertTrue(currentUrl.contains("google.com"), "URL должен содержать 'google.com'");
        });
    }

    @Test
    @TestTag(id = "UI_011", description = "Проверка размеров окна", category = "UI", priority = 3)
    @Story("Basic UI Tests")
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