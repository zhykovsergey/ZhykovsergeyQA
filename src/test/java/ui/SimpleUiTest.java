package ui;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DisplayName;
import utils.BaseUiTest;
import utils.TestTag;
import utils.AssertUtils;
import utils.Config;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Простой UI тест с улучшенными ассертами и автоматическими скриншотами
 */
@Epic("UI Testing")
@Feature("Basic UI Tests")
public class SimpleUiTest extends BaseUiTest {

    @Test
    @TestTag(id = "UI001", description = "Поиск в Google", category = "UI", priority = 1)
    @Story("Google Search")
    @DisplayName("Поиск в Google")
    @Description("Проверяем открытие Google и поиск")
    public void testGoogleSearch() {
        // Открываем Google
        navigateTo(Config.getGoogleUrl());
        System.out.println("🌐 Открываем Google...");
        
        // Ждем загрузки страницы
        waitForPageLoad();
        
        // Проверяем заголовок с улучшенными ассертами
        String title = driver.getTitle();
        AssertUtils.assertPageTitleContains("Google", title, "Заголовок должен содержать Google");
        
        // Проверяем URL с улучшенными ассертами
        String currentUrl = driver.getCurrentUrl();
        AssertUtils.assertUrlContains("google.com", currentUrl, "URL должен содержать google.com");
        
        System.out.println("✅ Google тест прошел успешно");
    }

    @Test
    @TestTag(id = "UI002", description = "Открытие YouTube", category = "UI", priority = 2)
    @Story("YouTube")
    @DisplayName("Открытие YouTube")
    @Description("Проверяем открытие YouTube")
    public void testYouTube() {
        // Открываем YouTube
        navigateTo(Config.getYouTubeUrl());
        System.out.println("📺 Открываем YouTube...");
        
        // Ждем загрузки страницы
        waitForPageLoad();
        
        // Проверяем заголовок с улучшенными ассертами
        String title = driver.getTitle();
        AssertUtils.assertPageTitleContains("YouTube", title, "Заголовок должен содержать YouTube");
        
        // Проверяем URL с улучшенными ассертами
        String currentUrl = driver.getCurrentUrl();
        AssertUtils.assertUrlContains("youtube.com", currentUrl, "URL должен содержать youtube.com");
        
        System.out.println("✅ YouTube тест прошел успешно");
    }
}
