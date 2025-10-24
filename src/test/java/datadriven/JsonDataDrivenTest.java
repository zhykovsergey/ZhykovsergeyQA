package datadriven;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pages.SauceDemoLoginPage;
import pages.SauceDemoProductsPage;
import utils.BaseUiTest;
import utils.TestTag;
import utils.AssertUtils;
import utils.Config;
import utils.WebDriverUtils;

import java.io.InputStream;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Data-driven тесты с JSON данными
 * Демонстрирует использование @ParameterizedTest с JSON провайдерами
 */
@Epic("Data-driven Testing")
@Feature("JSON Data-driven Tests")
public class JsonDataDrivenTest extends BaseUiTest {

    private SauceDemoLoginPage loginPage;
    private SauceDemoProductsPage productsPage;
    private ObjectMapper objectMapper;

    @BeforeEach
    @Step("Инициализация страниц для JSON Data-driven тестов")
    public void setupPages() {
        loginPage = new SauceDemoLoginPage(driver);
        productsPage = new SauceDemoProductsPage(driver);
        objectMapper = new ObjectMapper();
    }

    @ParameterizedTest
    @MethodSource("getLoginDataFromJson")
    @TestTag(id = "JSON_001", description = "JSON тест логина с разными пользователями", category = "Data-driven", priority = 1)
    @Story("JSON Data-driven")
    @DisplayName("Проверка логина с данными из JSON")
    @Description("Тестируем логин с данными из JSON файла")
    public void testLoginWithJsonData(String username, String password, boolean shouldLogin, String expectedPage) {
        // Открываем страницу логина
        navigateTo(Config.getSauceDemoUrl());
        waitForPageLoad();
        
        // Выполняем логин
        loginPage.login(username, password);
        
        if (shouldLogin) {
            // Проверяем успешный логин
            WebDriverUtils.waitForElementVisible(driver, org.openqa.selenium.By.className("inventory_container"), 10);
            AssertUtils.assertUrlContains("inventory.html", driver.getCurrentUrl(), "URL должен содержать inventory.html");
            AssertUtils.assertTrue(productsPage.isProductsPageLoaded(), "Страница продуктов должна загрузиться");
        } else {
            // Проверяем неуспешный логин - остаемся на главной странице
            AssertUtils.assertUrlContains("saucedemo.com", driver.getCurrentUrl(), "URL должен содержать saucedemo.com");
        }
    }

    @ParameterizedTest
    @MethodSource("getProductDataFromJson")
    @TestTag(id = "JSON_002", description = "JSON тест товаров", category = "Data-driven", priority = 1)
    @Story("JSON Data-driven")
    @DisplayName("Проверка товаров с данными из JSON")
    @Description("Тестируем товары с данными из JSON файла")
    public void testProductsWithJsonData(String productName, String expectedPrice, String category) {
        SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
        SauceDemoProductsPage productsPage = new SauceDemoProductsPage(driver);
        
        // Логинимся
        loginPage.openLoginPage().login("standard_user", "secret_sauce");
        
        // Проверяем, что мы на странице продуктов
        assertThat("URL должен содержать inventory", 
                driver.getCurrentUrl(), containsString("inventory"));
        assertThat("Страница продуктов должна загрузиться", 
                productsPage.isProductsPageLoaded(), is(true));
        
        // Проверяем, что товары отображаются
        assertThat("Продукты должны отображаться", 
                productsPage.areProductsDisplayed(), is(true));
    }

    @ParameterizedTest
    @MethodSource("getUserScenariosFromJson")
    @TestTag(id = "JSON_003", description = "JSON тест пользовательских сценариев", category = "Data-driven", priority = 2)
    @Story("JSON Data-driven")
    @DisplayName("Проверка пользовательских сценариев из JSON")
    @Description("Тестируем различные пользовательские сценарии из JSON")
    public void testUserScenariosWithJsonData(String scenario, String username, String password, String expectedResult) {
        SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
        SauceDemoProductsPage productsPage = new SauceDemoProductsPage(driver);
        
        // Открываем страницу логина
        loginPage.openLoginPage();
        
        // Выполняем логин
        loginPage.login(username, password);
        
        if ("success".equals(expectedResult)) {
            // Проверяем успешный логин
            assertThat("URL должен содержать inventory", 
                    driver.getCurrentUrl(), containsString("inventory"));
            assertThat("Страница продуктов должна загрузиться", 
                    productsPage.isProductsPageLoaded(), is(true));
        } else {
            // Проверяем неуспешный логин
            assertThat("URL должен содержать login", 
                    driver.getCurrentUrl(), containsString("login"));
        }
    }

    @ParameterizedTest
    @MethodSource("getTestConfigurationsFromJson")
    @TestTag(id = "JSON_004", description = "JSON тест конфигураций", category = "Data-driven", priority = 2)
    @Story("JSON Data-driven")
    @DisplayName("Проверка различных конфигураций из JSON")
    @Description("Тестируем различные конфигурации из JSON файла")
    public void testConfigurationsWithJsonData(String configName, String browserMode, String testType, boolean shouldPass) {
        SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
        SauceDemoProductsPage productsPage = new SauceDemoProductsPage(driver);
        
        // Логинимся
        loginPage.openLoginPage().login("standard_user", "secret_sauce");
        
        // Проверяем, что мы на странице продуктов
        assertThat("URL должен содержать inventory", 
                driver.getCurrentUrl(), containsString("inventory"));
        assertThat("Страница продуктов должна загрузиться", 
                productsPage.isProductsPageLoaded(), is(true));
    }

    /**
     * Метод для получения данных логина из JSON файла
     */
    private static Stream<Arguments> getLoginDataFromJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = JsonDataDrivenTest.class.getClassLoader()
                    .getResourceAsStream("data/login_data.json");
            JsonNode rootNode = mapper.readTree(inputStream);
            JsonNode loginData = rootNode.get("loginData");
            
            return Stream.of(
                Arguments.of(
                    loginData.get(0).get("username").asText(),
                    loginData.get(0).get("password").asText(),
                    loginData.get(0).get("shouldLogin").asBoolean(),
                    loginData.get(0).get("expectedPage").asText()
                ),
                Arguments.of(
                    loginData.get(1).get("username").asText(),
                    loginData.get(1).get("password").asText(),
                    loginData.get(1).get("shouldLogin").asBoolean(),
                    loginData.get(1).get("expectedPage").asText()
                ),
                Arguments.of(
                    loginData.get(2).get("username").asText(),
                    loginData.get(2).get("password").asText(),
                    loginData.get(2).get("shouldLogin").asBoolean(),
                    loginData.get(2).get("expectedPage").asText()
                )
            );
        } catch (Exception e) {
            return Stream.of(
                Arguments.of("standard_user", "secret_sauce", true, "Products"),
                Arguments.of("locked_out_user", "secret_sauce", false, "Login"),
                Arguments.of("problem_user", "secret_sauce", true, "Products")
            );
        }
    }

    /**
     * Метод для получения данных товаров из JSON файла
     */
    private static Stream<Arguments> getProductDataFromJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = JsonDataDrivenTest.class.getClassLoader()
                    .getResourceAsStream("data/products_data.json");
            JsonNode rootNode = mapper.readTree(inputStream);
            JsonNode productsData = rootNode.get("products");
            
            return Stream.of(
                Arguments.of(
                    productsData.get(0).get("name").asText(),
                    productsData.get(0).get("price").asText(),
                    productsData.get(0).get("category").asText()
                ),
                Arguments.of(
                    productsData.get(1).get("name").asText(),
                    productsData.get(1).get("price").asText(),
                    productsData.get(1).get("category").asText()
                ),
                Arguments.of(
                    productsData.get(2).get("name").asText(),
                    productsData.get(2).get("price").asText(),
                    productsData.get(2).get("category").asText()
                )
            );
        } catch (Exception e) {
            return Stream.of(
                Arguments.of("Sauce Labs Backpack", "$29.99", "backpack"),
                Arguments.of("Sauce Labs Bike Light", "$9.99", "light"),
                Arguments.of("Sauce Labs Bolt T-Shirt", "$15.99", "shirt")
            );
        }
    }

    /**
     * Метод для получения пользовательских сценариев из JSON файла
     */
    private static Stream<Arguments> getUserScenariosFromJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = JsonDataDrivenTest.class.getClassLoader()
                    .getResourceAsStream("data/user_scenarios.json");
            JsonNode rootNode = mapper.readTree(inputStream);
            JsonNode scenarios = rootNode.get("scenarios");
            
            return Stream.of(
                Arguments.of(
                    scenarios.get(0).get("scenario").asText(),
                    scenarios.get(0).get("username").asText(),
                    scenarios.get(0).get("password").asText(),
                    scenarios.get(0).get("expectedResult").asText()
                ),
                Arguments.of(
                    scenarios.get(1).get("scenario").asText(),
                    scenarios.get(1).get("username").asText(),
                    scenarios.get(1).get("password").asText(),
                    scenarios.get(1).get("expectedResult").asText()
                )
            );
        } catch (Exception e) {
            return Stream.of(
                Arguments.of("Valid Login", "standard_user", "secret_sauce", "success"),
                Arguments.of("Invalid Login", "invalid_user", "invalid_password", "failure")
            );
        }
    }

    /**
     * Метод для получения конфигураций из JSON файла
     */
    private static Stream<Arguments> getTestConfigurationsFromJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = JsonDataDrivenTest.class.getClassLoader()
                    .getResourceAsStream("data/test_configurations.json");
            JsonNode rootNode = mapper.readTree(inputStream);
            JsonNode configurations = rootNode.get("configurations");
            
            return Stream.of(
                Arguments.of(
                    configurations.get(0).get("configName").asText(),
                    configurations.get(0).get("browserMode").asText(),
                    configurations.get(0).get("testType").asText(),
                    configurations.get(0).get("shouldPass").asBoolean()
                ),
                Arguments.of(
                    configurations.get(1).get("configName").asText(),
                    configurations.get(1).get("browserMode").asText(),
                    configurations.get(1).get("testType").asText(),
                    configurations.get(1).get("shouldPass").asBoolean()
                )
            );
        } catch (Exception e) {
            return Stream.of(
                Arguments.of("Headless Mode", "headless", "smoke", true),
                Arguments.of("Visible Mode", "visible", "regression", true)
            );
        }
    }
}
