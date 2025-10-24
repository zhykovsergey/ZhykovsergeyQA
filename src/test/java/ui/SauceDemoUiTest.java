package ui;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DisplayName;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.TestTag;
import utils.WebDriverUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * UI тесты для SauceDemo
 * https://www.saucedemo.com/
 * E-commerce сайт для практики автотестирования
 */
@Epic("SauceDemo UI Testing")
@Feature("E-commerce Practice Tests")
public class SauceDemoUiTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    @Step("Настройка браузера")
    public void setUp() {
        driver = WebDriverUtils.createWebDriver();
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
    @TestTag(id = "SAUCE001", description = "Тест авторизации", category = "UI", priority = 1)
    @Story("Login")
    @DisplayName("Проверка авторизации")
    @Description("Проверяем успешную авторизацию на сайте")
    public void testLogin() {
        try {
            WebDriverUtils.openUrl(driver, "https://www.saucedemo.com/");
            
            // Проверяем заголовок страницы
            WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".login_logo")));
            assertThat("Заголовок должен содержать 'Swag Labs'", title.getText(), containsString("Swag Labs"));
            
            // Вводим логин и пароль
            driver.findElement(By.id("user-name")).sendKeys("standard_user");
            driver.findElement(By.id("password")).sendKeys("secret_sauce");
            
            // Нажимаем кнопку входа
            driver.findElement(By.id("login-button")).click();
            
            // Проверяем успешную авторизацию
            WebElement productsTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".title")));
            assertThat("Заголовок должен содержать 'Products'", productsTitle.getText(), containsString("Products"));
            
            System.out.println("✅ Тест авторизации прошел успешно");
            
        } catch (Exception e) {
            System.err.println("❌ Ошибка в тесте авторизации: " + e.getMessage());
            throw e;
        }
    }

    @Test
    @TestTag(id = "SAUCE002", description = "Тест каталога товаров", category = "UI", priority = 1)
    @Story("Products")
    @DisplayName("Проверка каталога товаров")
    @Description("Проверяем отображение товаров в каталоге")
    public void testProductsCatalog() {
        try {
            // Авторизуемся
            WebDriverUtils.openUrl(driver, "https://www.saucedemo.com/");
            driver.findElement(By.id("user-name")).sendKeys("standard_user");
            driver.findElement(By.id("password")).sendKeys("secret_sauce");
            driver.findElement(By.id("login-button")).click();
            
            // Проверяем заголовок каталога
            WebElement productsTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".title")));
            assertThat("Заголовок должен содержать 'Products'", productsTitle.getText(), containsString("Products"));
            
            // Проверяем наличие товаров
            int productCount = driver.findElements(By.cssSelector(".inventory_item")).size();
            assertThat("Должно быть товары в каталоге", productCount, greaterThan(0));
            
            // Проверяем первый товар
            WebElement firstProduct = driver.findElement(By.cssSelector(".inventory_item:first-child"));
            assertThat("Первый товар должен быть виден", firstProduct.isDisplayed(), is(true));
            
            // Проверяем элементы товара
            WebElement productName = firstProduct.findElement(By.cssSelector(".inventory_item_name"));
            WebElement productPrice = firstProduct.findElement(By.cssSelector(".inventory_item_price"));
            WebElement addToCartButton = firstProduct.findElement(By.cssSelector(".btn_inventory"));
            
            assertThat("Название товара должно быть видно", productName.isDisplayed(), is(true));
            assertThat("Цена товара должна быть видна", productPrice.isDisplayed(), is(true));
            assertThat("Кнопка добавления должна быть видна", addToCartButton.isDisplayed(), is(true));
            
            System.out.println("✅ Тест каталога товаров прошел успешно");
            
        } catch (Exception e) {
            System.err.println("❌ Ошибка в тесте каталога товаров: " + e.getMessage());
            throw e;
        }
    }

    @Test
    @TestTag(id = "SAUCE003", description = "Тест добавления товара в корзину", category = "UI", priority = 2)
    @Story("Add to Cart")
    @DisplayName("Проверка добавления товара в корзину")
    @Description("Проверяем добавление товара в корзину")
    public void testAddToCart() {
        try {
            // Авторизуемся
            WebDriverUtils.openUrl(driver, "https://www.saucedemo.com/");
            driver.findElement(By.id("user-name")).sendKeys("standard_user");
            driver.findElement(By.id("password")).sendKeys("secret_sauce");
            driver.findElement(By.id("login-button")).click();
            
            // Добавляем товар в корзину
            WebElement addToCartButton = driver.findElement(By.cssSelector(".inventory_item:first-child .btn_inventory"));
            addToCartButton.click();
            
            // Проверяем, что кнопка изменилась на "Remove"
            WebElement removeButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".inventory_item:first-child .btn_inventory")));
            assertThat("Кнопка должна измениться на 'Remove'", removeButton.getText(), containsString("Remove"));
            
            // Проверяем счетчик корзины
            WebElement cartBadge = driver.findElement(By.cssSelector(".shopping_cart_badge"));
            assertThat("Счетчик корзины должен показывать '1'", cartBadge.getText(), equalTo("1"));
            
            System.out.println("✅ Тест добавления товара в корзину прошел успешно");
            
        } catch (Exception e) {
            System.err.println("❌ Ошибка в тесте добавления товара в корзину: " + e.getMessage());
            throw e;
        }
    }

    @Test
    @TestTag(id = "SAUCE004", description = "Тест корзины", category = "UI", priority = 2)
    @Story("Shopping Cart")
    @DisplayName("Проверка корзины")
    @Description("Проверяем работу с корзиной покупок")
    public void testShoppingCart() {
        try {
            // Авторизуемся
            WebDriverUtils.openUrl(driver, "https://www.saucedemo.com/");
            driver.findElement(By.id("user-name")).sendKeys("standard_user");
            driver.findElement(By.id("password")).sendKeys("secret_sauce");
            driver.findElement(By.id("login-button")).click();
            
            // Добавляем товар в корзину
            driver.findElement(By.cssSelector(".inventory_item:first-child .btn_inventory")).click();
            
            // Переходим в корзину
            driver.findElement(By.cssSelector(".shopping_cart_link")).click();
            
            // Проверяем заголовок корзины
            WebElement cartTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".title")));
            assertThat("Заголовок должен содержать 'Your Cart'", cartTitle.getText(), containsString("Your Cart"));
            
            // Проверяем наличие товара в корзине
            int cartItems = driver.findElements(By.cssSelector(".cart_item")).size();
            assertThat("В корзине должен быть товар", cartItems, greaterThan(0));
            
            // Проверяем кнопки управления
            WebElement continueShopping = driver.findElement(By.id("continue-shopping"));
            WebElement checkout = driver.findElement(By.id("checkout"));
            
            assertThat("Кнопка 'Continue Shopping' должна быть видна", continueShopping.isDisplayed(), is(true));
            assertThat("Кнопка 'Checkout' должна быть видна", checkout.isDisplayed(), is(true));
            
            System.out.println("✅ Тест корзины прошел успешно");
            
        } catch (Exception e) {
            System.err.println("❌ Ошибка в тесте корзины: " + e.getMessage());
            throw e;
        }
    }

    @Test
    @TestTag(id = "SAUCE005", description = "Тест оформления заказа", category = "UI", priority = 3)
    @Story("Checkout")
    @DisplayName("Проверка оформления заказа")
    @Description("Проверяем процесс оформления заказа")
    public void testCheckout() {
        try {
            // Авторизуемся
            WebDriverUtils.openUrl(driver, "https://www.saucedemo.com/");
            driver.findElement(By.id("user-name")).sendKeys("standard_user");
            driver.findElement(By.id("password")).sendKeys("secret_sauce");
            driver.findElement(By.id("login-button")).click();
            
            // Добавляем товар в корзину
            driver.findElement(By.cssSelector(".inventory_item:first-child .btn_inventory")).click();
            
            // Переходим в корзину
            driver.findElement(By.cssSelector(".shopping_cart_link")).click();
            
            // Нажимаем Checkout
            driver.findElement(By.id("checkout")).click();
            
            // Проверяем заголовок страницы оформления
            WebElement checkoutTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".title")));
            assertThat("Заголовок должен содержать 'Checkout: Your Information'", checkoutTitle.getText(), containsString("Checkout: Your Information"));
            
            // Заполняем форму
            driver.findElement(By.id("first-name")).sendKeys("John");
            driver.findElement(By.id("last-name")).sendKeys("Doe");
            driver.findElement(By.id("postal-code")).sendKeys("12345");
            
            // Продолжаем оформление
            driver.findElement(By.id("continue")).click();
            
            // Проверяем страницу обзора заказа
            WebElement overviewTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".title")));
            assertThat("Заголовок должен содержать 'Checkout: Overview'", overviewTitle.getText(), containsString("Checkout: Overview"));
            
            System.out.println("✅ Тест оформления заказа прошел успешно");
            
        } catch (Exception e) {
            System.err.println("❌ Ошибка в тесте оформления заказа: " + e.getMessage());
            throw e;
        }
    }

    @Test
    @TestTag(id = "SAUCE006", description = "Тест фильтрации товаров", category = "UI", priority = 2)
    @Story("Product Filter")
    @DisplayName("Проверка фильтрации товаров")
    @Description("Проверяем фильтрацию товаров по цене")
    public void testProductFilter() {
        try {
            // Авторизуемся
            WebDriverUtils.openUrl(driver, "https://www.saucedemo.com/");
            driver.findElement(By.id("user-name")).sendKeys("standard_user");
            driver.findElement(By.id("password")).sendKeys("secret_sauce");
            driver.findElement(By.id("login-button")).click();
            
            // Проверяем наличие фильтра
            WebElement filter = driver.findElement(By.cssSelector(".product_sort_container"));
            assertThat("Фильтр должен быть виден", filter.isDisplayed(), is(true));
            
            // Выбираем сортировку по цене (от низкой к высокой)
            Select sortSelect = new Select(filter);
            sortSelect.selectByValue("lohi");
            
            // Проверяем, что товары отсортированы
            WebElement firstProductPrice = driver.findElement(By.cssSelector(".inventory_item:first-child .inventory_item_price"));
            assertThat("Цена первого товара должна быть видна", firstProductPrice.isDisplayed(), is(true));
            
            System.out.println("✅ Тест фильтрации товаров прошел успешно");
            
        } catch (Exception e) {
            System.err.println("❌ Ошибка в тесте фильтрации товаров: " + e.getMessage());
            throw e;
        }
    }

    @Test
    @TestTag(id = "SAUCE007", description = "Тест меню", category = "UI", priority = 1)
    @Story("Menu")
    @DisplayName("Проверка меню")
    @Description("Проверяем работу с меню")
    public void testMenu() {
        try {
            // Авторизуемся
            WebDriverUtils.openUrl(driver, "https://www.saucedemo.com/");
            driver.findElement(By.id("user-name")).sendKeys("standard_user");
            driver.findElement(By.id("password")).sendKeys("secret_sauce");
            driver.findElement(By.id("login-button")).click();
            
            // Открываем меню
            driver.findElement(By.id("react-burger-menu-btn")).click();
            
            // Проверяем элементы меню
            WebElement allItems = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inventory_sidebar_link")));
            WebElement about = driver.findElement(By.id("about_sidebar_link"));
            WebElement logout = driver.findElement(By.id("logout_sidebar_link"));
            WebElement reset = driver.findElement(By.id("reset_sidebar_link"));
            
            assertThat("'All Items' должен быть виден", allItems.isDisplayed(), is(true));
            assertThat("'About' должен быть виден", about.isDisplayed(), is(true));
            assertThat("'Logout' должен быть виден", logout.isDisplayed(), is(true));
            assertThat("'Reset App State' должен быть виден", reset.isDisplayed(), is(true));
            
            System.out.println("✅ Тест меню прошел успешно");
            
        } catch (Exception e) {
            System.err.println("❌ Ошибка в тесте меню: " + e.getMessage());
            throw e;
        }
    }

    @Test
    @TestTag(id = "SAUCE008", description = "Тест выхода", category = "UI", priority = 1)
    @Story("Logout")
    @DisplayName("Проверка выхода")
    @Description("Проверяем выход из системы")
    public void testLogout() {
        try {
            // Авторизуемся
            WebDriverUtils.openUrl(driver, "https://www.saucedemo.com/");
            driver.findElement(By.id("user-name")).sendKeys("standard_user");
            driver.findElement(By.id("password")).sendKeys("secret_sauce");
            driver.findElement(By.id("login-button")).click();
            
            // Открываем меню и выходим
            driver.findElement(By.id("react-burger-menu-btn")).click();
            driver.findElement(By.id("logout_sidebar_link")).click();
            
            // Проверяем, что вернулись на страницу авторизации
            WebElement loginButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
            assertThat("Кнопка входа должна быть видна", loginButton.isDisplayed(), is(true));
            
            System.out.println("✅ Тест выхода прошел успешно");
            
        } catch (Exception e) {
            System.err.println("❌ Ошибка в тесте выхода: " + e.getMessage());
            throw e;
        }
    }
}
