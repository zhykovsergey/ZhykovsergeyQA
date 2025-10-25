package ui;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.GoogleSearchPage;
import utils.*;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.*;

/**
 * UI тесты для Google поиска
 */
@Epic("UI Testing")
@Feature("Google Search UI Tests")
@ExtendWith(RetryExtension.class)
public class GoogleUiTest extends BaseUiTest {

    private GoogleSearchPage googlePage;

    @BeforeEach
    void setUp() {
        step("Инициализация Google страницы", () -> {
            googlePage = new GoogleSearchPage(driver);
        });
    }

    @Test
    @TestTag(id = "UI_006", description = "Поиск в Google", category = "UI", priority = 1)
    @Story("Google Search Tests")
    @DisplayName("Поиск в Google")
    @Description("Проверяем функциональность поиска в Google")
    @Severity(SeverityLevel.CRITICAL)
    public void testGoogleSearch() {
        step("Открываем Google", () -> {
            driver.get("https://www.google.com");
        });

        step("Вводим поисковый запрос", () -> {
            googlePage.enterSearchQuery("Selenium WebDriver");
        });

        step("Нажимаем кнопку поиска", () -> {
            googlePage.clickSearchButton();
        });

        step("Проверяем результаты поиска", () -> {
            assertTrue(googlePage.areSearchResultsDisplayed(), "Результаты поиска должны отображаться");
            assertTrue(googlePage.getSearchResultsCount() > 0, "Должно быть больше 0 результатов");
        });
    }

    @Test
    @TestTag(id = "UI_007", description = "Проверка заголовка страницы", category = "UI", priority = 2)
    @Story("Google Search Tests")
    @DisplayName("Проверка заголовка страницы")
    @Description("Проверяем корректность заголовка страницы")
    @Severity(SeverityLevel.NORMAL)
    public void testPageTitle() {
        step("Открываем Google", () -> {
            driver.get("https://www.google.com");
        });

        step("Проверяем заголовок страницы", () -> {
            String pageTitle = driver.getTitle();
            assertTrue(pageTitle.contains("Google"), "Заголовок должен содержать 'Google'");
        });
    }

    @Test
    @TestTag(id = "UI_008", description = "Проверка логотипа Google", category = "UI", priority = 3)
    @Story("Google Search Tests")
    @DisplayName("Проверка логотипа Google")
    @Description("Проверяем отображение логотипа Google")
    @Severity(SeverityLevel.MINOR)
    public void testGoogleLogo() {
        step("Открываем Google", () -> {
            driver.get("https://www.google.com");
        });

        step("Проверяем отображение логотипа", () -> {
            assertTrue(googlePage.isLogoDisplayed(), "Логотип Google должен отображаться");
        });
    }
}
