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
 * Улучшенный UI тест с новой системой валидации и логирования
 */
@Epic("UI Testing")
@Feature("Improved Quality Tests")
@ExtendWith(RetryExtension.class)
public class ImprovedQualityTest extends BaseUiTest {

    @Test
    @TestTag(id = "QUALITY_UI_001", description = "Тест с валидацией и улучшенным логированием", category = "UI", priority = 1)
    @Story("Quality Login and Cart")
    @DisplayName("Проверить логин и корзину с валидацией данных")
    @Description("Этот тест демонстрирует использование новой системы валидации и логирования")
    @RetryExtension.RetryOnFailure(maxAttempts = 3, delayMs = 1000, exponentialBackoff = true)
    public void testQualityLoginAndCart() {
        // Валидируем входные данные
        String username = "standard_user";
        String password = "secret_sauce";
        
        ValidationResult usernameValidation = ValidationUtils.validateUsername(username);
        ValidationResult passwordValidation = ValidationUtils.validateNotEmpty(password, "Пароль");
        
        RefactoringUtils.validateWithLogging(usernameValidation, "Валидация username");
        RefactoringUtils.validateWithLogging(passwordValidation, "Валидация пароля");
        
        // Логируем начало теста
        LoggerUtils.logTestStart(null, "testQualityLoginAndCart", "Тест с валидацией и логированием");
        
        try {
            // Создаем Page Objects
            SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
            SauceDemoProductsPage productsPage = new SauceDemoProductsPage(driver);

            // Логируем действие
            LoggerUtils.logAction("Начало процесса логина", "SauceDemo");
            
            // Выполняем логин с retry
            RefactoringUtils.executeWithRetryAndLogging(() -> {
                loginPage.openLoginPage();
                waitForPageLoad();
                loginPage.login(username, password);
                waitForPageLoad();
                return null;
            }, 3, 1000, "Логин в систему");

            // Валидируем результат логина
            String currentUrl = driver.getCurrentUrl();
            ValidationResult urlValidation = ValidationUtils.validateNotEmpty(currentUrl, "URL");
            RefactoringUtils.validateWithLogging(urlValidation, "Валидация URL после логина");
            
            assertThat("URL должен содержать inventory", currentUrl, containsString("inventory"));
            
            LoggerUtils.logAction("Логин выполнен успешно", "SauceDemo");

            // Добавляем товар в корзину с валидацией
            LoggerUtils.logAction("Добавление товара в корзину", "SauceDemo");
            
            RefactoringUtils.executeUiOperationWithWait(driver, (driver) -> {
                productsPage.addFirstProductToCart();
                return null;
            }, By.className("shopping_cart_badge"), "Добавление товара в корзину");
            
            // Валидируем результат добавления в корзину
            String cartCount = productsPage.getCartItemCount();
            ValidationResult cartValidation = ValidationUtils.validateNotEmpty(cartCount, "Количество товаров в корзине");
            RefactoringUtils.validateWithLogging(cartValidation, "Валидация корзины");
            
            assertThat("Количество товаров в корзине должно быть 1", cartCount, containsString("1"));
            
            LoggerUtils.logAction("Товар успешно добавлен в корзину", "SauceDemo");

            // Переходим в корзину
            LoggerUtils.logAction("Переход в корзину", "SauceDemo");
            
            RefactoringUtils.executeUiOperation(driver, (driver) -> {
                productsPage.openCart();
                waitForPageLoad();
                return null;
            }, "Переход в корзину");
            
            // Валидируем результат перехода в корзину
            String cartUrl = driver.getCurrentUrl();
            ValidationResult cartUrlValidation = ValidationUtils.validateNotEmpty(cartUrl, "URL корзины");
            RefactoringUtils.validateWithLogging(cartUrlValidation, "Валидация URL корзины");
            
            assertThat("URL должен содержать cart", cartUrl, containsString("cart"));
            
            LoggerUtils.logAction("Успешно перешли в корзину", "SauceDemo");

        } catch (Exception e) {
            LoggerUtils.logError("Ошибка в тесте качества", e);
            attachError(e);
            throw e;
        }
    }

    @Test
    @TestTag(id = "QUALITY_UI_002", description = "Тест с комплексной валидацией данных", category = "UI", priority = 2)
    @Story("Complex Data Validation")
    @DisplayName("Проверить комплексную валидацию данных")
    @Description("Этот тест демонстрирует комплексную валидацию пользовательских данных")
    public void testComplexDataValidation() {
        LoggerUtils.logTestStart(null, "testComplexDataValidation", "Тест комплексной валидации");
        
        try {
            // Создаем тестовые данные
            String validUsername = "standard_user";
            String validPassword = "secret_sauce";
            String invalidUsername = "ab"; // слишком короткий
            String invalidPassword = ""; // пустой
            
            // Валидируем валидные данные
            ValidationResult validUserValidation = ValidationUtils.validateUser(
                "Test User",
                validUsername,
                "test@example.com",
                "123-456-7890",
                "https://example.com"
            );
            
            RefactoringUtils.validateWithLogging(validUserValidation, "Валидация валидного пользователя");
            assertTrue(validUserValidation.isValid(), "Валидный пользователь должен пройти валидацию");
            
            // Валидируем невалидные данные
            ValidationResult invalidUserValidation = ValidationUtils.validateUser(
                "", // невалидное имя
                invalidUsername, // невалидный username
                "invalid-email", // невалидный email
                "invalid-phone", // невалидный телефон
                "invalid-url" // невалидный URL
            );
            
            assertFalse(invalidUserValidation.isValid(), "Невалидный пользователь не должен пройти валидацию");
            assertTrue(invalidUserValidation.getErrorCount() > 1, "Должно быть несколько ошибок");
            
            LoggerUtils.logData("Валидация пользователя", "Валидные данные: " + validUserValidation.isValid() + 
                ", Невалидные данные: " + invalidUserValidation.getErrorCount() + " ошибок");
            
            // Тестируем логин с валидными данными
            SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
            
            RefactoringUtils.executeUiOperation(driver, (driver) -> {
                loginPage.openLoginPage();
                waitForPageLoad();
                loginPage.login(validUsername, validPassword);
                waitForPageLoad();
                return null;
            }, "Логин с валидными данными");
            
            // Валидируем успешный логин
            String currentUrl = driver.getCurrentUrl();
            assertThat("URL должен содержать inventory", currentUrl, containsString("inventory"));
            
            LoggerUtils.logAction("Комплексная валидация завершена успешно", "SauceDemo");
            
        } catch (Exception e) {
            LoggerUtils.logError("Ошибка в тесте комплексной валидации", e);
            attachError(e);
            throw e;
        }
    }

    @Test
    @TestTag(id = "QUALITY_UI_003", description = "Тест с улучшенной обработкой ошибок", category = "UI", priority = 3)
    @Story("Enhanced Error Handling")
    @DisplayName("Проверить улучшенную обработку ошибок")
    @Description("Этот тест демонстрирует улучшенную обработку ошибок с детальным логированием")
    public void testEnhancedErrorHandling() {
        LoggerUtils.logTestStart(null, "testEnhancedErrorHandling", "Тест улучшенной обработки ошибок");
        
        try {
            // Тестируем обработку ошибки при поиске несуществующего элемента
            try {
                waitForElementWithRetry(By.id("non-existent-element"), 2);
                fail("Должна была возникнуть ошибка");
            } catch (Exception e) {
                // Ожидаемая ошибка
                LoggerUtils.logError("Ожидаемая ошибка при поиске несуществующего элемента", e);
                
                // Проверяем, что это правильный тип исключения
                assertTrue(e instanceof RuntimeException, "Должно быть RuntimeException");
                assertTrue(e.getMessage().contains("не найден"), "Сообщение должно содержать 'не найден'");
            }
            
            // Тестируем обработку ошибки при невалидных данных
            try {
                ValidationResult invalidValidation = ValidationUtils.validateUser(
                    "", "", "", "", ""
                );
                
                if (!invalidValidation.isValid()) {
                    LoggerUtils.logError("Валидация не пройдена", new RuntimeException("Validation failed"));
                    throw new RuntimeException("Валидация не пройдена: " + invalidValidation.getAllErrorsAsString());
                }
            } catch (Exception e) {
                // Ожидаемая ошибка
                LoggerUtils.logError("Ожидаемая ошибка валидации", e);
                assertTrue(e.getMessage().contains("Валидация не пройдена"), "Сообщение должно содержать 'Валидация не пройдена'");
            }
            
            LoggerUtils.logAction("Улучшенная обработка ошибок протестирована", "SauceDemo");
            
        } catch (Exception e) {
            LoggerUtils.logError("Неожиданная ошибка в тесте обработки ошибок", e);
            attachError(e);
            throw e;
        }
    }

    @Test
    @TestTag(id = "QUALITY_UI_004", description = "Тест производительности с логированием", category = "UI", priority = 4)
    @Story("Performance Testing")
    @DisplayName("Проверить производительность с детальным логированием")
    @Description("Этот тест демонстрирует измерение производительности с детальным логированием")
    public void testPerformanceWithLogging() {
        LoggerUtils.logTestStart(null, "testPerformanceWithLogging", "Тест производительности");
        
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
            
            LoggerUtils.logPerformance("Логин в систему", loginDuration);
            
            // Измеряем время добавления товара в корзину
            long addToCartStartTime = System.currentTimeMillis();
            
            RefactoringUtils.executeUiOperation(driver, (driver) -> {
                productsPage.addFirstProductToCart();
                return null;
            }, "Добавление товара в корзину");
            
            long addToCartEndTime = System.currentTimeMillis();
            long addToCartDuration = addToCartEndTime - addToCartStartTime;
            
            LoggerUtils.logPerformance("Добавление товара в корзину", addToCartDuration);
            
            // Измеряем время перехода в корзину
            long goToCartStartTime = System.currentTimeMillis();
            
            RefactoringUtils.executeUiOperation(driver, (driver) -> {
                productsPage.openCart();
                waitForPageLoad();
                return null;
            }, "Переход в корзину");
            
            long goToCartEndTime = System.currentTimeMillis();
            long goToCartDuration = goToCartEndTime - goToCartStartTime;
            
            LoggerUtils.logPerformance("Переход в корзину", goToCartDuration);
            
            // Валидируем производительность
            assertTrue(loginDuration < 10000, "Логин должен выполняться менее чем за 10 секунд");
            assertTrue(addToCartDuration < 5000, "Добавление в корзину должно выполняться менее чем за 5 секунд");
            assertTrue(goToCartDuration < 5000, "Переход в корзину должен выполняться менее чем за 5 секунд");
            
            LoggerUtils.logAction("Тест производительности завершен успешно", "SauceDemo");
            
        } catch (Exception e) {
            LoggerUtils.logError("Ошибка в тесте производительности", e);
            attachError(e);
            throw e;
        }
    }
}
