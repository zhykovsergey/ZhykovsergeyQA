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
 * UI тесты с мониторингом и уведомлениями
 */
@Epic("UI Testing")
@Feature("Monitored UI Tests")
@ExtendWith(RetryExtension.class)
public class MonitoredUiTest extends MonitoredBaseTest {

    @Test
    @TestTag(id = "MONITORED_UI_001", description = "Мониторинг теста логина и добавления в корзину", category = "UI", priority = 1)
    @Story("Monitored Login and Cart")
    @DisplayName("Проверить логин и добавление товара в корзину с мониторингом")
    @Description("Этот тест проверяет базовый сценарий с полным мониторингом и уведомлениями")
    @RetryExtension.RetryOnFailure(maxAttempts = 3, delayMs = 1000, exponentialBackoff = true)
    public void testMonitoredLoginAndAddToCart() {
        // Логируем начало теста
        logAction("Начало мониторинг теста логина и добавления в корзину");
        attachData("Test Type", "Monitored UI Test");
        
        try {
            // Создаем Page Objects
            SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
            SauceDemoProductsPage productsPage = new SauceDemoProductsPage(driver);

            // Логируем действие
            logAction("Начало процесса логина");
            
            // Выполняем логин с мониторингом
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
            
            logAction("Логин выполнен успешно");

            // Добавляем товар в корзину с мониторингом
            logAction("Добавление товара в корзину");
            
            RefactoringUtils.executeUiOperationWithWait(driver, (driver) -> {
                productsPage.addFirstProductToCart();
                return null;
            }, By.className("shopping_cart_badge"), "Добавление товара в корзину");
            
            // Валидируем результат добавления в корзину
            String cartCount = productsPage.getCartItemCount();
            assertThat("Количество товаров в корзине должно быть 1", cartCount, containsString("1"));
            
            logAction("Товар успешно добавлен в корзину");

            // Прикрепляем метрики производительности
            long testDuration = System.currentTimeMillis() - testStartTime;
            java.util.Map<String, Object> metrics = new java.util.HashMap<>();
            metrics.put("Test Duration", testDuration + " ms");
            metrics.put("Final URL", currentUrl);
            metrics.put("Cart Count", cartCount);
            metrics.put("Browser Info", getBrowserInfo());
            
            attachPerformanceMetrics("Monitored Login and Cart Test", testDuration, metrics);

        } catch (Exception e) {
            attachError(e);
            throw e;
        }
    }

    @Test
    @TestTag(id = "MONITORED_UI_002", description = "Мониторинг теста производительности", category = "UI", priority = 2)
    @Story("Monitored Performance")
    @DisplayName("Проверить производительность с мониторингом")
    @Description("Этот тест проверяет производительность с детальным мониторингом")
    public void testMonitoredPerformance() {
        logAction("Начало теста производительности с мониторингом");
        
        try {
            SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
            SauceDemoProductsPage productsPage = new SauceDemoProductsPage(driver);
            
            // Измеряем время логина
            MonitoringUtils.startTimer("login.operation");
            
            RefactoringUtils.executeUiOperation(driver, (driver) -> {
                loginPage.openLoginPage();
                waitForPageLoad();
                loginPage.login("standard_user", "secret_sauce");
                waitForPageLoad();
                return null;
            }, "Логин в систему");
            
            long loginDuration = MonitoringUtils.stopTimer("login.operation");
            
            // Измеряем время добавления товара в корзину
            MonitoringUtils.startTimer("add.to.cart.operation");
            
            RefactoringUtils.executeUiOperation(driver, (driver) -> {
                productsPage.addFirstProductToCart();
                return null;
            }, "Добавление товара в корзину");
            
            long addToCartDuration = MonitoringUtils.stopTimer("add.to.cart.operation");
            
            // Измеряем время перехода в корзину
            MonitoringUtils.startTimer("go.to.cart.operation");
            
            RefactoringUtils.executeUiOperation(driver, (driver) -> {
                productsPage.openCart();
                waitForPageLoad();
                return null;
            }, "Переход в корзину");
            
            long goToCartDuration = MonitoringUtils.stopTimer("go.to.cart.operation");
            
            // Прикрепляем метрики производительности
            java.util.Map<String, Object> performanceMetrics = new java.util.HashMap<>();
            performanceMetrics.put("Login Duration", loginDuration + " ms");
            performanceMetrics.put("Add to Cart Duration", addToCartDuration + " ms");
            performanceMetrics.put("Go to Cart Duration", goToCartDuration + " ms");
            performanceMetrics.put("Total Duration", (loginDuration + addToCartDuration + goToCartDuration) + " ms");
            performanceMetrics.put("Browser Info", getBrowserInfo());
            
            attachPerformanceMetrics("Monitored Performance Test", 
                loginDuration + addToCartDuration + goToCartDuration, performanceMetrics);
            
            // Валидируем производительность
            assertTrue(loginDuration < 10000, "Логин должен выполняться менее чем за 10 секунд");
            assertTrue(addToCartDuration < 5000, "Добавление в корзину должно выполняться менее чем за 5 секунд");
            assertTrue(goToCartDuration < 5000, "Переход в корзину должен выполняться менее чем за 5 секунд");
            
            logAction("Тест производительности с мониторингом завершен");

        } catch (Exception e) {
            attachError(e);
            throw e;
        }
    }

    @Test
    @TestTag(id = "MONITORED_UI_003", description = "Мониторинг теста стабильности", category = "UI", priority = 3)
    @Story("Monitored Stability")
    @DisplayName("Проверить стабильность с мониторингом")
    @Description("Этот тест проверяет стабильность с мониторингом и retry механизмом")
    @RetryExtension.RetryOnFailure(maxAttempts = 5, delayMs = 2000, exponentialBackoff = true)
    public void testMonitoredStability() {
        logAction("Начало теста стабильности с мониторингом");
        
        try {
            SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
            SauceDemoProductsPage productsPage = new SauceDemoProductsPage(driver);
            
            // Тест стабильности с мониторингом
            for (int i = 1; i <= 3; i++) {
                logAction("Итерация " + i + " теста стабильности");
                
                // Логин
                MonitoringUtils.startTimer("stability.login." + i);
                loginPage.openLoginPage();
                waitForPageLoad();
                loginPage.login("standard_user", "secret_sauce");
                waitForPageLoad();
                long loginTime = MonitoringUtils.stopTimer("stability.login." + i);
                
                // Добавление в корзину
                MonitoringUtils.startTimer("stability.add.cart." + i);
                productsPage.addFirstProductToCart();
                waitForElementVisible(By.className("shopping_cart_badge"));
                long addCartTime = MonitoringUtils.stopTimer("stability.add.cart." + i);
                
                // Переход в корзину
                MonitoringUtils.startTimer("stability.go.cart." + i);
                productsPage.openCart();
                waitForPageLoad();
                long goCartTime = MonitoringUtils.stopTimer("stability.go.cart." + i);
                
                // Валидация
                String cartCount = productsPage.getCartItemCount();
                assertThat("Количество товаров в корзине должно быть 1", cartCount, containsString("1"));
                
                // Прикрепляем метрики итерации
                java.util.Map<String, Object> iterationMetrics = new java.util.HashMap<>();
                iterationMetrics.put("Iteration", i);
                iterationMetrics.put("Login Time", loginTime + " ms");
                iterationMetrics.put("Add Cart Time", addCartTime + " ms");
                iterationMetrics.put("Go Cart Time", goCartTime + " ms");
                iterationMetrics.put("Total Time", (loginTime + addCartTime + goCartTime) + " ms");
                
                attachPerformanceMetrics("Stability Iteration " + i, 
                    loginTime + addCartTime + goCartTime, iterationMetrics);
                
                logAction("Итерация " + i + " завершена успешно");
                
                // Пауза между итерациями
                if (i < 3) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Прервано ожидание", e);
                    }
                }
            }
            
            logAction("Тест стабильности с мониторингом завершен");

        } catch (Exception e) {
            attachError(e);
            throw e;
        }
    }

    @Test
    @TestTag(id = "MONITORED_UI_004", description = "Мониторинг теста ошибок", category = "UI", priority = 4)
    @Story("Monitored Error Handling")
    @DisplayName("Проверить обработку ошибок с мониторингом")
    @Description("Этот тест проверяет обработку ошибок с мониторингом и уведомлениями")
    public void testMonitoredErrorHandling() {
        logAction("Начало теста обработки ошибок с мониторингом");
        
        try {
            SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
            
            // Тест с намеренной ошибкой для демонстрации мониторинга
            logAction("Попытка логина с неверными данными");
            
            try {
                loginPage.openLoginPage();
                waitForPageLoad();
                loginPage.login("invalid_user", "invalid_password");
                waitForPageLoad();
                
                // Если логин прошел, это ошибка
                fail("Логин с неверными данными не должен пройти");
                
            } catch (Exception e) {
                // Ожидаемая ошибка
                logAction("Ожидаемая ошибка при логине с неверными данными: " + e.getMessage());
                MonitoringUtils.incrementCounter("ui.actions.failed");
            }
            
            // Тест с корректными данными
            logAction("Попытка логина с корректными данными");
            
            loginPage.openLoginPage();
            waitForPageLoad();
            loginPage.login("standard_user", "secret_sauce");
            waitForPageLoad();
            
            // Валидация успешного логина
            String currentUrl = driver.getCurrentUrl();
            assertThat("URL должен содержать inventory", currentUrl, containsString("inventory"));
            
            logAction("Логин с корректными данными выполнен успешно");
            
            // Прикрепляем метрики обработки ошибок
            java.util.Map<String, Object> errorMetrics = new java.util.HashMap<>();
            errorMetrics.put("Error Handling", "SUCCESS");
            errorMetrics.put("Failed Actions", MonitoringUtils.getCounterValue("ui.actions.failed"));
            errorMetrics.put("Successful Actions", MonitoringUtils.getCounterValue("ui.actions.success"));
            errorMetrics.put("Total Actions", MonitoringUtils.getCounterValue("ui.actions.total"));
            
            attachPerformanceMetrics("Error Handling Test", 
                System.currentTimeMillis() - testStartTime, errorMetrics);
            
            logAction("Тест обработки ошибок с мониторингом завершен");

        } catch (Exception e) {
            attachError(e);
            throw e;
        }
    }
}
