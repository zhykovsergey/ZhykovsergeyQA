package ui;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import pages.SauceDemoLoginPage;
import pages.SauceDemoProductsPage;
import utils.*;
import exceptions.ElementNotFoundException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для разных браузеров
 */
@Epic("UI Testing")
@Feature("Multi-Browser Tests")
@ExtendWith(RetryExtension.class)
public class MultiBrowserTest extends ExtendedBaseUiTest {

    @Test
    @TestTag(id = "MULTI_BROWSER_001", description = "Тест логина в разных браузерах", category = "UI", priority = 1)
    @Story("Multi-Browser Login")
    @DisplayName("Проверить логин в разных браузерах")
    @Description("Этот тест проверяет работу логина в разных браузерах")
    @RetryExtension.RetryOnFailure(maxAttempts = 3, delayMs = 1000, exponentialBackoff = true)
    public void testMultiBrowserLogin() {
        // Логируем тип браузера
        logAction("Начало теста логина в браузере: " + getBrowserType());
        attachData("Browser Type", getBrowserType().toString());
        
        try {
            // Создаем Page Objects
            SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
            SauceDemoProductsPage productsPage = new SauceDemoProductsPage(driver);

            // Логируем действие
            logAction("Начало процесса логина");
            
            // Выполняем логин с retry
            RefactoringUtils.executeWithRetryAndLogging(() -> {
                loginPage.openLoginPage();
                waitForPageLoad();
                loginPage.login("standard_user", "secret_sauce");
                waitForPageLoad();
                return null;
            }, 3, 1000, "Логин в систему");

            // Валидируем результат логина
            String currentUrl = driver.getCurrentUrl();
            assertThat("URL должен содержать inventory", currentUrl, containsString("inventory"));
            
            logAction("Логин выполнен успешно в браузере: " + getBrowserType());

            // Добавляем товар в корзину
            logAction("Добавление товара в корзину");
            
            RefactoringUtils.executeUiOperationWithWait(driver, (driver) -> {
                productsPage.addFirstProductToCart();
                return null;
            }, By.className("shopping_cart_badge"), "Добавление товара в корзину");
            
            // Валидируем результат добавления в корзину
            String cartCount = productsPage.getCartItemCount();
            assertThat("Количество товаров в корзине должно быть 1", cartCount, containsString("1"));
            
            logAction("Товар успешно добавлен в корзину в браузере: " + getBrowserType());

            // Прикрепляем метрики производительности
            long testDuration = System.currentTimeMillis() - System.currentTimeMillis();
            java.util.Map<String, Object> metrics = new java.util.HashMap<>();
            metrics.put("Browser Type", getBrowserType().toString());
            metrics.put("Test Duration", testDuration + " ms");
            metrics.put("Final URL", currentUrl);
            metrics.put("Cart Count", cartCount);
            
            attachPerformanceMetrics("Multi-Browser Login Test", testDuration, metrics);

        } catch (Exception e) {
            attachError(e);
            throw e;
        }
    }

    @Test
    @TestTag(id = "MULTI_BROWSER_002", description = "Тест навигации в разных браузерах", category = "UI", priority = 2)
    @Story("Multi-Browser Navigation")
    @DisplayName("Проверить навигацию в разных браузерах")
    @Description("Этот тест проверяет работу навигации в разных браузерах")
    public void testMultiBrowserNavigation() {
        logAction("Начало теста навигации в браузере: " + getBrowserType());
        
        try {
            // Тестируем навигацию на разные страницы
            String[] testUrls = {
                "https://www.google.com",
                "https://www.github.com",
                "https://www.stackoverflow.com"
            };
            
            for (String url : testUrls) {
                logAction("Переход на страницу: " + url);
                navigateTo(url);
                
                // Проверяем, что страница загрузилась
                assertThat("Страница должна загрузиться", driver.getTitle(), not(emptyString()));
                
                // Прикрепляем информацию о странице
                attachData("Page Info", "URL: " + url + ", Title: " + driver.getTitle());
                
                // Ждем немного между переходами
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Прервано ожидание", e);
                }
            }
            
            logAction("Навигация успешно протестирована в браузере: " + getBrowserType());
            
        } catch (Exception e) {
            attachError(e);
            throw e;
        }
    }

    @Test
    @TestTag(id = "MULTI_BROWSER_003", description = "Тест производительности в разных браузерах", category = "UI", priority = 3)
    @Story("Multi-Browser Performance")
    @DisplayName("Проверить производительность в разных браузерах")
    @Description("Этот тест проверяет производительность в разных браузерах")
    public void testMultiBrowserPerformance() {
        logAction("Начало теста производительности в браузере: " + getBrowserType());
        
        try {
            SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
            SauceDemoProductsPage productsPage = new SauceDemoProductsPage(driver);
            
            // Измеряем время логина
            long loginStartTime = System.currentTimeMillis();
            
            RefactoringUtils.executeUiOperation(driver, (driver) -> {
                loginPage.openLoginPage();
                waitForPageLoad();
                loginPage.login("standard_user", "secret_sauce");
                waitForPageLoad();
                return null;
            }, "Логин в систему");
            
            long loginEndTime = System.currentTimeMillis();
            long loginDuration = loginEndTime - loginStartTime;
            
            // Измеряем время добавления товара в корзину
            long addToCartStartTime = System.currentTimeMillis();
            
            RefactoringUtils.executeUiOperation(driver, (driver) -> {
                productsPage.addFirstProductToCart();
                return null;
            }, "Добавление товара в корзину");
            
            long addToCartEndTime = System.currentTimeMillis();
            long addToCartDuration = addToCartEndTime - addToCartStartTime;
            
            // Измеряем время перехода в корзину
            long goToCartStartTime = System.currentTimeMillis();
            
            RefactoringUtils.executeUiOperation(driver, (driver) -> {
                productsPage.openCart();
                waitForPageLoad();
                return null;
            }, "Переход в корзину");
            
            long goToCartEndTime = System.currentTimeMillis();
            long goToCartDuration = goToCartEndTime - goToCartStartTime;
            
            // Прикрепляем метрики производительности
            java.util.Map<String, Object> performanceMetrics = new java.util.HashMap<>();
            performanceMetrics.put("Browser Type", getBrowserType().toString());
            performanceMetrics.put("Login Duration", loginDuration + " ms");
            performanceMetrics.put("Add to Cart Duration", addToCartDuration + " ms");
            performanceMetrics.put("Go to Cart Duration", goToCartDuration + " ms");
            performanceMetrics.put("Total Duration", (loginDuration + addToCartDuration + goToCartDuration) + " ms");
            
            attachPerformanceMetrics("Multi-Browser Performance Test", 
                loginDuration + addToCartDuration + goToCartDuration, performanceMetrics);
            
            // Валидируем производительность
            assertTrue(loginDuration < 10000, "Логин должен выполняться менее чем за 10 секунд");
            assertTrue(addToCartDuration < 5000, "Добавление в корзину должно выполняться менее чем за 5 секунд");
            assertTrue(goToCartDuration < 5000, "Переход в корзину должен выполняться менее чем за 5 секунд");
            
            logAction("Тест производительности завершен в браузере: " + getBrowserType());
            
        } catch (Exception e) {
            attachError(e);
            throw e;
        }
    }

    @Test
    @TestTag(id = "MULTI_BROWSER_004", description = "Тест совместимости в разных браузерах", category = "UI", priority = 4)
    @Story("Multi-Browser Compatibility")
    @DisplayName("Проверить совместимость в разных браузерах")
    @Description("Этот тест проверяет совместимость в разных браузерах")
    public void testMultiBrowserCompatibility() {
        logAction("Начало теста совместимости в браузере: " + getBrowserType());
        
        try {
            // Тестируем различные функции браузера
            SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
            
            // Тест 1: Проверка загрузки страницы
            logAction("Проверка загрузки страницы");
            loginPage.openLoginPage();
            waitForPageLoad();
            
            assertThat("Страница должна загрузиться", driver.getTitle(), not(emptyString()));
            assertThat("URL должен содержать saucedemo", driver.getCurrentUrl(), containsString("saucedemo"));
            
            // Тест 2: Проверка элементов страницы
            logAction("Проверка элементов страницы");
            waitForElementVisible(By.id("user-name"));
            waitForElementVisible(By.id("password"));
            waitForElementVisible(By.id("login-button"));
            
            // Тест 3: Проверка JavaScript
            logAction("Проверка JavaScript");
            String pageTitle = (String) ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("return document.title;");
            assertThat("JavaScript должен работать", pageTitle, not(emptyString()));
            
            // Тест 4: Проверка cookies
            logAction("Проверка cookies");
            driver.manage().addCookie(new org.openqa.selenium.Cookie("test-cookie", "test-value"));
            org.openqa.selenium.Cookie cookie = driver.manage().getCookieNamed("test-cookie");
            assertNotNull(cookie, "Cookie должен быть установлен");
            assertEquals("test-value", cookie.getValue(), "Значение cookie должно соответствовать");
            
            // Тест 5: Проверка localStorage
            logAction("Проверка localStorage");
            ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("localStorage.setItem('test-key', 'test-value');");
            String localStorageValue = (String) ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("return localStorage.getItem('test-key');");
            assertEquals("test-value", localStorageValue, "localStorage должен работать");
            
            // Прикрепляем результаты тестов совместимости
            java.util.Map<String, Object> compatibilityResults = new java.util.HashMap<>();
            compatibilityResults.put("Browser Type", getBrowserType().toString());
            compatibilityResults.put("Page Load", "PASSED");
            compatibilityResults.put("Elements Visibility", "PASSED");
            compatibilityResults.put("JavaScript", "PASSED");
            compatibilityResults.put("Cookies", "PASSED");
            compatibilityResults.put("LocalStorage", "PASSED");
            
            attachData("Compatibility Results", compatibilityResults.toString());
            
            logAction("Тест совместимости завершен в браузере: " + getBrowserType());
            
        } catch (Exception e) {
            attachError(e);
            throw e;
        }
    }
}
