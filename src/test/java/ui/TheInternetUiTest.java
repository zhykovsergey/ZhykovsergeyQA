package ui;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DisplayName;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.TestTag;
import utils.WebDriverUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * UI тесты для The Internet
 * https://the-internet.herokuapp.com/
 * Специально созданный сайт для практики автотестирования
 */
@Epic("The Internet UI Testing")
@Feature("UI Practice Tests")
public class TheInternetUiTest {

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
    @TestTag(id = "INTERNET001", description = "Тест авторизации", category = "UI", priority = 1)
    @Story("Login")
    @DisplayName("Проверка авторизации")
    @Description("Проверяем успешную авторизацию на сайте")
    public void testLogin() {
        try {
            WebDriverUtils.openUrl(driver, "https://the-internet.herokuapp.com/login");
            
            // Вводим логин и пароль
            driver.findElement(By.id("username")).sendKeys("tomsmith");
            driver.findElement(By.id("password")).sendKeys("SuperSecretPassword!");
            
            // Нажимаем кнопку входа
            driver.findElement(By.cssSelector("button[type='submit']")).click();
            
            // Проверяем успешную авторизацию
            WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".flash.success")));
            assertThat("Сообщение об успехе должно отображаться", successMessage.isDisplayed(), is(true));
            assertThat("URL должен содержать secure", driver.getCurrentUrl(), containsString("secure"));
            
            System.out.println("✅ Тест авторизации прошел успешно");
            
        } catch (Exception e) {
            System.err.println("❌ Ошибка в тесте авторизации: " + e.getMessage());
            throw e;
        }
    }

    @Test
    @TestTag(id = "INTERNET002", description = "Тест добавления элементов", category = "UI", priority = 1)
    @Story("Add/Remove Elements")
    @DisplayName("Проверка добавления элементов")
    @Description("Проверяем добавление и удаление элементов на странице")
    public void testAddRemoveElements() {
        try {
            WebDriverUtils.openUrl(driver, "https://the-internet.herokuapp.com/add_remove_elements/");
            
            // Нажимаем кнопку "Add Element" несколько раз
            for (int i = 0; i < 3; i++) {
                driver.findElement(By.cssSelector("button[onclick='addElement()']")).click();
            }
            
            // Проверяем, что элементы добавились
            int deleteButtons = driver.findElements(By.cssSelector("button[onclick='deleteElement()']")).size();
            assertThat("Должно быть 3 кнопки удаления", deleteButtons, equalTo(3));
            
            // Удаляем один элемент
            driver.findElement(By.cssSelector("button[onclick='deleteElement()']")).click();
            
            // Проверяем, что осталось 2 элемента
            int remainingButtons = driver.findElements(By.cssSelector("button[onclick='deleteElement()']")).size();
            assertThat("Должно остаться 2 кнопки", remainingButtons, equalTo(2));
            
            System.out.println("✅ Тест добавления элементов прошел успешно");
            
        } catch (Exception e) {
            System.err.println("❌ Ошибка в тесте добавления элементов: " + e.getMessage());
            throw e;
        }
    }

    @Test
    @TestTag(id = "INTERNET003", description = "Тест чекбоксов", category = "UI", priority = 2)
    @Story("Checkboxes")
    @DisplayName("Проверка чекбоксов")
    @Description("Проверяем работу с чекбоксами")
    public void testCheckboxes() {
        try {
            WebDriverUtils.openUrl(driver, "https://the-internet.herokuapp.com/checkboxes");
            
            // Находим все чекбоксы
            WebElement checkbox1 = driver.findElement(By.cssSelector("input[type='checkbox']:first-of-type"));
            WebElement checkbox2 = driver.findElement(By.cssSelector("input[type='checkbox']:last-of-type"));
            
            // Проверяем начальное состояние
            assertThat("Первый чекбокс должен быть не отмечен", checkbox1.isSelected(), is(false));
            assertThat("Второй чекбокс должен быть отмечен", checkbox2.isSelected(), is(true));
            
            // Кликаем на первый чекбокс
            checkbox1.click();
            assertThat("Первый чекбокс должен быть отмечен", checkbox1.isSelected(), is(true));
            
            // Кликаем на второй чекбокс
            checkbox2.click();
            assertThat("Второй чекбокс должен быть не отмечен", checkbox2.isSelected(), is(false));
            
            System.out.println("✅ Тест чекбоксов прошел успешно");
            
        } catch (Exception e) {
            System.err.println("❌ Ошибка в тесте чекбоксов: " + e.getMessage());
            throw e;
        }
    }

    @Test
    @TestTag(id = "INTERNET004", description = "Тест выпадающих списков", category = "UI", priority = 2)
    @Story("Dropdown")
    @DisplayName("Проверка выпадающих списков")
    @Description("Проверяем работу с выпадающими списками")
    public void testDropdown() {
        try {
            WebDriverUtils.openUrl(driver, "https://the-internet.herokuapp.com/dropdown");
            
            // Находим выпадающий список
            WebElement dropdown = driver.findElement(By.id("dropdown"));
            
            // Выбираем опцию "Option 1"
            dropdown.findElement(By.cssSelector("option[value='1']")).click();
            String selectedOption = dropdown.findElement(By.cssSelector("option:checked")).getText();
            assertThat("Должна быть выбрана Option 1", selectedOption, equalTo("Option 1"));
            
            // Выбираем опцию "Option 2"
            dropdown.findElement(By.cssSelector("option[value='2']")).click();
            selectedOption = dropdown.findElement(By.cssSelector("option:checked")).getText();
            assertThat("Должна быть выбрана Option 2", selectedOption, equalTo("Option 2"));
            
            System.out.println("✅ Тест выпадающих списков прошел успешно");
            
        } catch (Exception e) {
            System.err.println("❌ Ошибка в тесте выпадающих списков: " + e.getMessage());
            throw e;
        }
    }

    @Test
    @TestTag(id = "INTERNET005", description = "Тест динамической загрузки", category = "UI", priority = 2)
    @Story("Dynamic Loading")
    @DisplayName("Проверка динамической загрузки")
    @Description("Проверяем загрузку контента после нажатия кнопки")
    public void testDynamicLoading() {
        try {
            WebDriverUtils.openUrl(driver, "https://the-internet.herokuapp.com/dynamic_loading/1");
            
            // Нажимаем кнопку "Start"
            driver.findElement(By.cssSelector("button")).click();
            
            // Ждем появления текста "Hello World!"
            WebElement helloWorld = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#finish h4")));
            assertThat("Текст 'Hello World!' должен появиться", helloWorld.getText(), equalTo("Hello World!"));
            
            System.out.println("✅ Тест динамической загрузки прошел успешно");
            
        } catch (Exception e) {
            System.err.println("❌ Ошибка в тесте динамической загрузки: " + e.getMessage());
            throw e;
        }
    }

    @Test
    @TestTag(id = "INTERNET006", description = "Тест файловых загрузок", category = "UI", priority = 3)
    @Story("File Download")
    @DisplayName("Проверка загрузки файлов")
    @Description("Проверяем загрузку файлов")
    public void testFileDownload() {
        try {
            WebDriverUtils.openUrl(driver, "https://the-internet.herokuapp.com/download");
            
            // Проверяем наличие ссылок для загрузки
            WebElement downloadLink = driver.findElement(By.cssSelector("a[href*='some-file.txt']"));
            assertThat("Ссылка для загрузки должна быть видна", downloadLink.isDisplayed(), is(true));
            assertThat("Текст ссылки должен содержать 'some-file.txt'", downloadLink.getText(), containsString("some-file.txt"));
            
            System.out.println("✅ Тест загрузки файлов прошел успешно");
            
        } catch (Exception e) {
            System.err.println("❌ Ошибка в тесте загрузки файлов: " + e.getMessage());
            throw e;
        }
    }

    @Test
    @TestTag(id = "INTERNET007", description = "Тест форм", category = "UI", priority = 2)
    @Story("Form Authentication")
    @DisplayName("Проверка форм")
    @Description("Проверяем работу с формами")
    public void testFormAuthentication() {
        try {
            WebDriverUtils.openUrl(driver, "https://the-internet.herokuapp.com/login");
            
            // Проверяем наличие полей формы
            WebElement usernameField = driver.findElement(By.id("username"));
            WebElement passwordField = driver.findElement(By.id("password"));
            WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
            
            assertThat("Поле username должно быть видно", usernameField.isDisplayed(), is(true));
            assertThat("Поле password должно быть видно", passwordField.isDisplayed(), is(true));
            assertThat("Кнопка входа должна быть видна", loginButton.isDisplayed(), is(true));
            
            // Проверяем плейсхолдеры
            assertThat("Поле username должно иметь плейсхолдер", usernameField.getAttribute("placeholder"), notNullValue());
            assertThat("Поле password должно иметь плейсхолдер", passwordField.getAttribute("placeholder"), notNullValue());
            
            System.out.println("✅ Тест форм прошел успешно");
            
        } catch (Exception e) {
            System.err.println("❌ Ошибка в тесте форм: " + e.getMessage());
            throw e;
        }
    }

    @Test
    @TestTag(id = "INTERNET008", description = "Тест таблиц", category = "UI", priority = 2)
    @Story("Tables")
    @DisplayName("Проверка таблиц")
    @Description("Проверяем работу с таблицами")
    public void testTables() {
        try {
            WebDriverUtils.openUrl(driver, "https://the-internet.herokuapp.com/tables");
            
            // Проверяем наличие таблиц
            WebElement table1 = driver.findElement(By.id("table1"));
            WebElement table2 = driver.findElement(By.id("table2"));
            
            assertThat("Первая таблица должна быть видна", table1.isDisplayed(), is(true));
            assertThat("Вторая таблица должна быть видна", table2.isDisplayed(), is(true));
            
            // Проверяем заголовки первой таблицы
            WebElement header = table1.findElement(By.cssSelector("thead tr"));
            assertThat("Заголовок таблицы должен содержать текст", header.getText(), not(emptyString()));
            
            // Проверяем строки данных
            int rows = table1.findElements(By.cssSelector("tbody tr")).size();
            assertThat("В таблице должны быть строки данных", rows, greaterThan(0));
            
            System.out.println("✅ Тест таблиц прошел успешно");
            
        } catch (Exception e) {
            System.err.println("❌ Ошибка в тесте таблиц: " + e.getMessage());
            throw e;
        }
    }
}
