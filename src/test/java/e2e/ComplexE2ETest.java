package e2e;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DisplayName;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.*;
import utils.TestTag;
import utils.WebDriverUtils;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Комплексные E2E тесты
 * Полные сценарии с интеграцией API и UI
 */
@Epic("E2E Testing")
@Feature("Complex E2E Tests")
public class ComplexE2ETest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    @Step("Настройка браузера для комплексных E2E тестов")
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
    @TestTag(id = "E2E_COMPLEX_001", description = "Полный E2E сценарий: API → UI → API", category = "E2E", priority = 1)
    @Story("Complex E2E Flow")
    @DisplayName("Полный E2E сценарий с API → UI → API последовательностью")
    @Description("Выполняем полный E2E сценарий: проверяем API, выполняем UI действия, снова проверяем API")
    public void testFullE2EScenarioApiUiApi() {
        // Шаг 1: API проверка - получаем данные пользователя
        String userResponse = given()
                .when()
                    .get("https://reqres.in/api/users/1")
                .then()
                    .statusCode(200)
                    .extract()
                    .response()
                    .asString();
        
        assertThat("API должен вернуть данные пользователя", userResponse, not(emptyString()));
        assertThat("API должен содержать email", userResponse, containsString("email"));
        
        // Шаг 2: UI действия - логин и добавление в корзину
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
        
        // Шаг 3: API проверка - получаем посты
        String postsResponse = given()
                .when()
                    .get("https://jsonplaceholder.typicode.com/posts")
                .then()
                    .statusCode(200)
                    .extract()
                    .response()
                    .asString();
        
        assertThat("API должен вернуть посты", postsResponse, not(emptyString()));
        assertThat("API должен содержать userId", postsResponse, containsString("userId"));
    }

    @Test
    @TestTag(id = "E2E_COMPLEX_002", description = "E2E сценарий с созданием данных", category = "E2E", priority = 1)
    @Story("Complex E2E Flow")
    @DisplayName("E2E сценарий с созданием данных через API и UI")
    @Description("Создаем данные через API, затем проверяем их через UI")
    public void testE2EScenarioWithDataCreation() {
        // Шаг 1: API создание поста
        String postData = "{\n" +
                "  \"title\": \"E2E Complex Test Post\",\n" +
                "  \"body\": \"This post was created during complex E2E testing\",\n" +
                "  \"userId\": 1\n" +
                "}";
        
        String createdPost = given()
                .header("Content-Type", "application/json")
                .body(postData)
                .when()
                    .post("https://jsonplaceholder.typicode.com/posts")
                .then()
                    .statusCode(201)
                    .extract()
                    .response()
                    .asString();
        
        assertThat("API должен вернуть созданный пост", createdPost, not(emptyString()));
        assertThat("API должен содержать title", createdPost, containsString("E2E Complex Test Post"));
        
        // Шаг 2: UI проверка функционала
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
        
        // Шаг 3: API проверка - получаем комментарии
        String commentsResponse = given()
                .when()
                    .get("https://jsonplaceholder.typicode.com/comments")
                .then()
                    .statusCode(200)
                    .extract()
                    .response()
                    .asString();
        
        assertThat("API должен вернуть комментарии", commentsResponse, not(emptyString()));
        assertThat("API должен содержать email", commentsResponse, containsString("email"));
    }

    @Test
    @TestTag(id = "E2E_COMPLEX_003", description = "E2E сценарий с валидацией данных", category = "E2E", priority = 2)
    @Story("Complex E2E Flow")
    @DisplayName("E2E сценарий с валидацией данных между API и UI")
    @Description("Проверяем валидацию данных между API и UI компонентами")
    public void testE2EScenarioWithDataValidation() {
        // Шаг 1: API проверка - получаем пользователей
        String usersResponse = given()
                .when()
                    .get("https://reqres.in/api/users")
                .then()
                    .statusCode(200)
                    .extract()
                    .response()
                    .asString();
        
        assertThat("API должен вернуть пользователей", usersResponse, not(emptyString()));
        assertThat("API должен содержать data", usersResponse, containsString("data"));
        
        // Шаг 2: UI валидация - проверяем логин
        SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
        SauceDemoProductsPage productsPage = new SauceDemoProductsPage(driver);
        
        // Логинимся в UI
        loginPage.openLoginPage().login("standard_user", "secret_sauce");
        
        // Проверяем, что UI логин успешен
        assertThat("URL должен содержать inventory", 
                driver.getCurrentUrl(), containsString("inventory"));
        assertThat("Страница продуктов должна загрузиться", 
                productsPage.isProductsPageLoaded(), is(true));
        
        // Шаг 3: API проверка - получаем посты
        String postsResponse = given()
                .when()
                    .get("https://jsonplaceholder.typicode.com/posts")
                .then()
                    .statusCode(200)
                    .extract()
                    .response()
                    .asString();
        
        assertThat("API должен вернуть посты", postsResponse, not(emptyString()));
        assertThat("API должен содержать userId", postsResponse, containsString("userId"));
        
        // Шаг 4: UI валидация - проверяем добавление в корзину
        productsPage.addFirstProductToCart();
        
        // Проверяем, что товар добавлен
        assertThat("Страница должна остаться на inventory", 
                driver.getCurrentUrl(), containsString("inventory"));
        
        // Шаг 5: API проверка - получаем комментарии
        String commentsResponse = given()
                .when()
                    .get("https://jsonplaceholder.typicode.com/comments")
                .then()
                    .statusCode(200)
                    .extract()
                    .response()
                    .asString();
        
        assertThat("API должен вернуть комментарии", commentsResponse, not(emptyString()));
        assertThat("API должен содержать body", commentsResponse, containsString("body"));
    }

    @Test
    @TestTag(id = "E2E_COMPLEX_004", description = "E2E сценарий с обработкой ошибок", category = "E2E", priority = 2)
    @Story("Complex E2E Flow")
    @DisplayName("E2E сценарий с обработкой ошибок API и UI")
    @Description("Проверяем обработку ошибок в API и UI компонентах")
    public void testE2EScenarioWithErrorHandling() {
        // Шаг 1: API проверка - получаем несуществующего пользователя
        given()
                .when()
                    .get("https://reqres.in/api/users/999")
                .then()
                    .statusCode(404);
        
        // Шаг 2: UI проверка - логин с неверными данными
        SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
        
        // Пытаемся залогиниться с неверными данными
        loginPage.openLoginPage().login("invalid_user", "invalid_password");
        
        // Проверяем, что мы остались на странице логина
        assertThat("URL должен содержать login", 
                driver.getCurrentUrl(), containsString("login"));
        
        // Шаг 3: UI проверка - правильный логин
        loginPage.login("standard_user", "secret_sauce");
        
        // Проверяем, что логин успешен
        assertThat("URL должен содержать inventory", 
                driver.getCurrentUrl(), containsString("inventory"));
        
        // Шаг 4: API проверка - получаем валидные данные
        String validResponse = given()
                .when()
                    .get("https://reqres.in/api/users/1")
                .then()
                    .statusCode(200)
                    .extract()
                    .response()
                    .asString();
        
        assertThat("API должен вернуть валидные данные", validResponse, not(emptyString()));
    }

    @Test
    @TestTag(id = "E2E_COMPLEX_005", description = "E2E сценарий с производительностью", category = "E2E", priority = 3)
    @Story("Complex E2E Flow")
    @DisplayName("E2E сценарий с проверкой производительности")
    @Description("Проверяем производительность API и UI компонентов")
    public void testE2EScenarioWithPerformanceCheck() {
        long startTime = System.currentTimeMillis();
        
        // Шаг 1: API проверка производительности
        String apiResponse = given()
                .when()
                    .get("https://reqres.in/api/users")
                .then()
                    .statusCode(200)
                    .extract()
                    .response()
                    .asString();
        
        long apiTime = System.currentTimeMillis() - startTime;
        assertThat("API должен вернуть данные", apiResponse, not(emptyString()));
        assertThat("API время ответа должно быть разумным", apiTime, lessThan(5000L));
        
        // Шаг 2: UI проверка производительности
        long uiStartTime = System.currentTimeMillis();
        
        SauceDemoLoginPage loginPage = new SauceDemoLoginPage(driver);
        SauceDemoProductsPage productsPage = new SauceDemoProductsPage(driver);
        
        // Логинимся в UI
        loginPage.openLoginPage().login("standard_user", "secret_sauce");
        
        long uiTime = System.currentTimeMillis() - uiStartTime;
        
        // Проверяем, что UI загрузился быстро
        assertThat("URL должен содержать inventory", 
                driver.getCurrentUrl(), containsString("inventory"));
        assertThat("UI время загрузки должно быть разумным", uiTime, lessThan(10000L));
        
        // Шаг 3: Комплексная проверка производительности
        long complexStartTime = System.currentTimeMillis();
        
        // Добавляем товар в корзину
        productsPage.addFirstProductToCart();
        
        // Переходим в корзину
        productsPage.openCart();
        
        long complexTime = System.currentTimeMillis() - complexStartTime;
        
        // Проверяем, что комплексные операции выполняются быстро
        assertThat("URL должен содержать cart", 
                driver.getCurrentUrl(), containsString("cart"));
        assertThat("Комплексные операции должны выполняться быстро", complexTime, lessThan(15000L));
    }
}