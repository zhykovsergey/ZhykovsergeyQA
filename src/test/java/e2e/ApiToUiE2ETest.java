package e2e;

import api.ReqResApiTest;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DisplayName;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.SauceDemoLoginPage;
import pages.SauceDemoProductsPage;
import utils.TestTag;
import utils.WebDriverUtils;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * E2E тесты: API → UI последовательность
 * Проверяем интеграцию между API и UI компонентами
 */
@Epic("E2E Testing")
@Feature("API to UI E2E Tests")
public class ApiToUiE2ETest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    @Step("Настройка браузера для E2E тестов")
    public void setUp() {
        // Используем headless режим для стабильности
        driver = WebDriverUtils.createWebDriver(true);
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
    @TestTag(id = "E2E_API_UI_001", description = "API проверка пользователя → UI логин", category = "E2E", priority = 1)
    @Story("API to UI Flow")
    @DisplayName("Проверка API пользователя и последующий UI логин")
    @Description("Проверяем API пользователя, затем выполняем UI логин с теми же данными")
    public void testApiUserValidationThenUiLogin() {
        // Шаг 1: API проверка - получаем данные пользователя
        String apiResponse = given()
                .when()
                    .get("https://jsonplaceholder.typicode.com/users/1")
                .then()
                    .statusCode(200)
                    .extract()
                    .response()
                    .asString();
        
        // Проверяем, что API вернул данные пользователя
        assertThat("API должен вернуть данные пользователя", apiResponse, not(emptyString()));
        assertThat("API должен содержать email", apiResponse, containsString("email"));
        assertThat("API должен содержать name", apiResponse, containsString("name"));
        
        // Шаг 2: UI логин - используем данные из API
        SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
        SauceDemoProductsPage productsPage = new SauceDemoProductsPage(driver);
        
        // Открываем страницу логина
        loginPage.openLoginPage();
        
        // Выполняем логин (используем тестовые данные SauceDemo)
        loginPage.login("standard_user", "secret_sauce");
        
        // Проверяем, что UI логин успешен
        assertThat("URL должен содержать inventory", 
                driver.getCurrentUrl(), containsString("inventory"));
        assertThat("Страница продуктов должна загрузиться", 
                productsPage.isProductsPageLoaded(), is(true));
    }

    @Test
    @TestTag(id = "E2E_API_UI_002", description = "API проверка постов → UI добавление в корзину", category = "E2E", priority = 1)
    @Story("API to UI Flow")
    @DisplayName("Проверка API постов и последующее добавление товара в корзину")
    @Description("Проверяем API постов, затем добавляем товар в корзину через UI")
    public void testApiPostsValidationThenUiAddToCart() {
        // Шаг 1: API проверка - получаем список постов
        String apiResponse = given()
                .when()
                    .get("https://jsonplaceholder.typicode.com/posts")
                .then()
                    .statusCode(200)
                    .extract()
                    .response()
                    .asString();
        
        // Проверяем, что API вернул список постов
        assertThat("API должен вернуть список постов", apiResponse, not(emptyString()));
        assertThat("API должен содержать userId", apiResponse, containsString("userId"));
        assertThat("API должен содержать title", apiResponse, containsString("title"));
        
        // Шаг 2: UI добавление в корзину
        SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
        SauceDemoProductsPage productsPage = new SauceDemoProductsPage(driver);
        
        // Логинимся в UI
        loginPage.openLoginPage().login("standard_user", "secret_sauce");
        
        // Проверяем, что мы на странице продуктов
        assertThat("URL должен содержать inventory", 
                driver.getCurrentUrl(), containsString("inventory"));
        
        // Добавляем товар в корзину
        productsPage.addFirstProductToCart();
        
        // Проверяем, что товар добавлен
        assertThat("Страница должна остаться на inventory", 
                driver.getCurrentUrl(), containsString("inventory"));
    }

    @Test
    @TestTag(id = "E2E_API_UI_003", description = "API проверка комментариев → UI полный e-commerce сценарий", category = "E2E", priority = 2)
    @Story("API to UI Flow")
    @DisplayName("Проверка API комментариев и полный UI e-commerce сценарий")
    @Description("Проверяем API комментариев, затем выполняем полный UI e-commerce сценарий")
    public void testApiCommentsValidationThenUiEcommerceFlow() {
        // Шаг 1: API проверка - получаем комментарии
        String apiResponse = given()
                .when()
                    .get("https://jsonplaceholder.typicode.com/comments")
                .then()
                    .statusCode(200)
                    .extract()
                    .response()
                    .asString();
        
        // Проверяем, что API вернул комментарии
        assertThat("API должен вернуть комментарии", apiResponse, not(emptyString()));
        assertThat("API должен содержать email", apiResponse, containsString("email"));
        assertThat("API должен содержать body", apiResponse, containsString("body"));
        
        // Шаг 2: UI полный e-commerce сценарий
        SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
        SauceDemoProductsPage productsPage = new SauceDemoProductsPage(driver);
        
        // Логинимся в UI
        loginPage.openLoginPage().login("standard_user", "secret_sauce");
        
        // Проверяем, что мы на странице продуктов
        assertThat("URL должен содержать inventory", 
                driver.getCurrentUrl(), containsString("inventory"));
        
        // Добавляем товар в корзину
        productsPage.addFirstProductToCart();
        
        // Переходим в корзину
        productsPage.openCart();
        
        // Проверяем, что мы в корзине
        assertThat("URL должен содержать cart", 
                driver.getCurrentUrl(), containsString("cart"));
    }

    @Test
    @TestTag(id = "E2E_API_UI_004", description = "API создание поста → UI проверка данных", category = "E2E", priority = 2)
    @Story("API to UI Flow")
    @DisplayName("API создание поста и UI проверка отображения данных")
    @Description("Создаем пост через API, затем проверяем отображение данных в UI")
    public void testApiCreatePostThenUiDataValidation() {
        // Шаг 1: API создание поста
        String postData = "{\n" +
                "  \"title\": \"E2E Test Post\",\n" +
                "  \"body\": \"This is a test post created during E2E testing\",\n" +
                "  \"userId\": 1\n" +
                "}";
        
        String apiResponse = given()
                .header("Content-Type", "application/json")
                .body(postData)
                .when()
                    .post("https://jsonplaceholder.typicode.com/posts")
                .then()
                    .statusCode(201)
                    .extract()
                    .response()
                    .asString();
        
        // Проверяем, что пост создан
        assertThat("API должен вернуть созданный пост", apiResponse, not(emptyString()));
        assertThat("API должен содержать title", apiResponse, containsString("E2E Test Post"));
        assertThat("API должен содержать body", apiResponse, containsString("E2E testing"));
        
        // Шаг 2: UI проверка - логинимся и проверяем функционал
        SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
        SauceDemoProductsPage productsPage = new SauceDemoProductsPage(driver);
        
        // Логинимся в UI
        loginPage.openLoginPage().login("standard_user", "secret_sauce");
        
        // Проверяем, что UI функционал работает
        assertThat("URL должен содержать inventory", 
                driver.getCurrentUrl(), containsString("inventory"));
        assertThat("Страница продуктов должна загрузиться", 
                productsPage.isProductsPageLoaded(), is(true));
        
        // Добавляем товар в корзину
        productsPage.addFirstProductToCart();
        
        // Проверяем, что товар добавлен
        assertThat("Страница должна остаться на inventory", 
                driver.getCurrentUrl(), containsString("inventory"));
    }
}
