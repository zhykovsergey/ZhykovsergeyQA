package e2e;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DisplayName;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.SauceDemoLoginPage;
import pages.SauceDemoProductsPage;
import pages.SauceDemoCartPage;
import pages.SauceDemoCheckoutPage;
import utils.TestTag;
import utils.WebDriverUtils;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * E2E тесты: UI → API последовательность
 * Проверяем интеграцию между UI и API компонентами
 */
@Epic("E2E Testing")
@Feature("UI to API E2E Tests")
public class UiToApiE2ETest {

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
    @TestTag(id = "E2E_UI_API_001", description = "UI логин → API проверка пользователя", category = "E2E", priority = 1)
    @Story("UI to API Flow")
    @DisplayName("UI логин и последующая API проверка пользователя")
    @Description("Выполняем UI логин, затем проверяем API пользователя")
    public void testUiLoginThenApiUserValidation() {
        // Шаг 1: UI логин
        SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
        SauceDemoProductsPage productsPage = new SauceDemoProductsPage(driver);
        
        // Открываем страницу логина
        loginPage.openLoginPage();
        
        // Выполняем логин
        loginPage.login("standard_user", "secret_sauce");
        
        // Проверяем, что UI логин успешен
        assertThat("URL должен содержать inventory", 
                driver.getCurrentUrl(), containsString("inventory"));
        assertThat("Страница продуктов должна загрузиться", 
                productsPage.isProductsPageLoaded(), is(true));
        
        // Шаг 2: API проверка - получаем данные пользователя
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
    }

    @Test
    @TestTag(id = "E2E_UI_API_002", description = "UI добавление в корзину → API проверка постов", category = "E2E", priority = 1)
    @Story("UI to API Flow")
    @DisplayName("UI добавление в корзину и последующая API проверка постов")
    @Description("Добавляем товар в корзину через UI, затем проверяем API постов")
    public void testUiAddToCartThenApiPostsValidation() {
        // Шаг 1: UI добавление в корзину
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
        
        // Шаг 2: API проверка - получаем список постов
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
    }

    @Test
    @TestTag(id = "E2E_UI_API_003", description = "UI полный e-commerce → API проверка комментариев", category = "E2E", priority = 2)
    @Story("UI to API Flow")
    @DisplayName("UI полный e-commerce сценарий и последующая API проверка комментариев")
    @Description("Выполняем полный UI e-commerce сценарий, затем проверяем API комментариев")
    public void testUiFullEcommerceThenApiCommentsValidation() {
        // Шаг 1: UI полный e-commerce сценарий
        SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
        SauceDemoProductsPage productsPage = new SauceDemoProductsPage(driver);
        SauceDemoCartPage cartPage = new SauceDemoCartPage(driver);
        SauceDemoCheckoutPage checkoutPage = new SauceDemoCheckoutPage(driver);
        
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
        
        // Переходим к оформлению заказа
        cartPage.proceedToCheckout();
        
        // Проверяем, что мы на странице оформления заказа
        assertThat("URL должен содержать checkout-step-one", 
                driver.getCurrentUrl(), containsString("checkout-step-one"));
        
        // Шаг 2: API проверка - получаем комментарии
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
    }

    @Test
    @TestTag(id = "E2E_UI_API_004", description = "UI проверка данных → API создание поста", category = "E2E", priority = 2)
    @Story("UI to API Flow")
    @DisplayName("UI проверка данных и последующее API создание поста")
    @Description("Проверяем UI данные, затем создаем пост через API")
    public void testUiDataValidationThenApiCreatePost() {
        // Шаг 1: UI проверка данных
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
        
        // Шаг 2: API создание поста
        String postData = "{\n" +
                "  \"title\": \"E2E UI to API Test Post\",\n" +
                "  \"body\": \"This post was created after UI validation in E2E testing\",\n" +
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
        assertThat("API должен содержать title", apiResponse, containsString("E2E UI to API Test Post"));
        assertThat("API должен содержать body", apiResponse, containsString("UI validation"));
    }

    @Test
    @TestTag(id = "E2E_UI_API_005", description = "UI комплексный сценарий → API комплексная проверка", category = "E2E", priority = 3)
    @Story("UI to API Flow")
    @DisplayName("UI комплексный сценарий и API комплексная проверка")
    @Description("Выполняем комплексный UI сценарий, затем комплексную API проверку")
    public void testUiComplexScenarioThenApiComplexValidation() {
        // Шаг 1: UI комплексный сценарий
        SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
        SauceDemoProductsPage productsPage = new SauceDemoProductsPage(driver);
        SauceDemoCartPage cartPage = new SauceDemoCartPage(driver);
        
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
        
        // Шаг 2: API комплексная проверка
        // Проверяем пользователей
        String usersResponse = given()
                .when()
                    .get("https://reqres.in/api/users")
                .then()
                    .statusCode(200)
                    .extract()
                    .response()
                    .asString();
        
        assertThat("API должен вернуть пользователей", usersResponse, not(emptyString()));
        
        // Проверяем посты
        String postsResponse = given()
                .when()
                    .get("https://jsonplaceholder.typicode.com/posts")
                .then()
                    .statusCode(200)
                    .extract()
                    .response()
                    .asString();
        
        assertThat("API должен вернуть посты", postsResponse, not(emptyString()));
        
        // Проверяем комментарии
        String commentsResponse = given()
                .when()
                    .get("https://jsonplaceholder.typicode.com/comments")
                .then()
                    .statusCode(200)
                    .extract()
                    .response()
                    .asString();
        
        assertThat("API должен вернуть комментарии", commentsResponse, not(emptyString()));
    }
}
