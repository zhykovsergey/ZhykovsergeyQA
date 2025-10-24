package ui;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DisplayName;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.interactions.Actions;
import utils.TestTag;
import utils.WebDriverUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * UI тесты для DemoQA
 * https://demoqa.com/
 * Современный сайт для практики автотестирования
 */
@Epic("DemoQA UI Testing")
@Feature("Modern UI Practice Tests")
public class DemoQAUiTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private Actions actions;

    @BeforeEach
    @Step("Настройка браузера")
    public void setUp() {
        driver = WebDriverUtils.createWebDriver();
        wait = WebDriverUtils.createWebDriverWait(driver);
        actions = new Actions(driver);
    }

    @AfterEach
    @Step("Закрытие браузера")
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @TestTag(id = "DEMO001", description = "Тест элементов", category = "UI", priority = 1)
    @Story("Elements")
    @DisplayName("Проверка элементов")
    @Description("Проверяем работу с различными элементами")
    public void testElements() {
        try {
            WebDriverUtils.openUrl(driver, "https://demoqa.com/elements");
            
            // Ждем загрузки страницы
            WebDriverUtils.waitForPageLoad(driver);
            
            // Проверяем заголовок страницы с несколькими вариантами селекторов
            WebElement header = null;
            try {
                header = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".main-header")));
            } catch (Exception e) {
                try {
                    header = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h1")));
                } catch (Exception e2) {
                    header = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("title")));
                }
            }
            
            assertThat("Заголовок должен содержать 'Elements'", header.getText(), containsString("Elements"));
            
            // Проверяем наличие различных элементов
            WebElement textBox = driver.findElement(By.cssSelector("li#item-0"));
            WebElement checkBox = driver.findElement(By.cssSelector("li#item-1"));
            WebElement radioButton = driver.findElement(By.cssSelector("li#item-2"));
            WebElement webTables = driver.findElement(By.cssSelector("li#item-3"));
            
            assertThat("Text Box должен быть виден", textBox.isDisplayed(), is(true));
            assertThat("Check Box должен быть виден", checkBox.isDisplayed(), is(true));
            assertThat("Radio Button должен быть виден", radioButton.isDisplayed(), is(true));
            assertThat("Web Tables должен быть виден", webTables.isDisplayed(), is(true));
            
            System.out.println("✅ Тест элементов прошел успешно");
            
        } catch (Exception e) {
            System.err.println("❌ Ошибка в тесте элементов: " + e.getMessage());
            throw e;
        }
    }

    @Test
    @TestTag(id = "DEMO002", description = "Тест Text Box", category = "UI", priority = 1)
    @Story("Text Box")
    @DisplayName("Проверка Text Box")
    @Description("Проверяем работу с текстовыми полями")
    public void testTextBox() {
        try {
            WebDriverUtils.openUrl(driver, "https://demoqa.com/text-box");
            
            // Заполняем форму
            driver.findElement(By.id("userName")).sendKeys("John Doe");
            driver.findElement(By.id("userEmail")).sendKeys("john.doe@example.com");
            driver.findElement(By.id("currentAddress")).sendKeys("123 Main Street, City, Country");
            driver.findElement(By.id("permanentAddress")).sendKeys("456 Oak Avenue, Town, State");
            
            // Нажимаем кнопку Submit
            driver.findElement(By.id("submit")).click();
            
            // Проверяем отображение данных
            WebElement output = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("output")));
            assertThat("Вывод должен содержать имя", output.getText(), containsString("John Doe"));
            assertThat("Вывод должен содержать email", output.getText(), containsString("john.doe@example.com"));
            
            System.out.println("✅ Тест Text Box прошел успешно");
            
        } catch (Exception e) {
            System.err.println("❌ Ошибка в тесте Text Box: " + e.getMessage());
            throw e;
        }
    }

    @Test
    @TestTag(id = "DEMO003", description = "Тест Check Box", category = "UI", priority = 2)
    @Story("Check Box")
    @DisplayName("Проверка Check Box")
    @Description("Проверяем работу с чекбоксами")
    public void testCheckBox() {
        try {
            WebDriverUtils.openUrl(driver, "https://demoqa.com/checkbox");
            
            // Ждем загрузки страницы
            WebDriverUtils.waitForPageLoad(driver);
            
            // Разворачиваем дерево
            WebElement expandButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[title='Expand all']")));
            expandButton.click();
            
            // Ждем разворачивания дерева
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            // Выбираем чекбоксы через label (более надежно)
            WebElement homeLabel = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("label[for='tree-node-home']")));
            homeLabel.click();
            
            // Выбираем desktop чекбокс
            WebElement desktopLabel = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("label[for='tree-node-desktop']")));
            desktopLabel.click();
            
            // Ждем обновления результата
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            // Проверяем результат
            WebElement result = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("result")));
            assertThat("Результат должен содержать 'home'", result.getText(), containsString("home"));
            assertThat("Результат должен содержать 'desktop'", result.getText(), containsString("desktop"));
            
            System.out.println("✅ Тест Check Box прошел успешно");
            
        } catch (Exception e) {
            System.err.println("❌ Ошибка в тесте Check Box: " + e.getMessage());
            throw e;
        }
    }

    @Test
    @TestTag(id = "DEMO004", description = "Тест Radio Button", category = "UI", priority = 2)
    @Story("Radio Button")
    @DisplayName("Проверка Radio Button")
    @Description("Проверяем работу с радиокнопками")
    public void testRadioButton() {
        try {
            WebDriverUtils.openUrl(driver, "https://demoqa.com/radio-button");
            
            // Выбираем радиокнопку "Yes"
            WebElement yesRadio = driver.findElement(By.id("yesRadio"));
            actions.moveToElement(yesRadio).click().perform();
            
            // Проверяем результат
            WebElement result = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".text-success")));
            assertThat("Результат должен содержать 'Yes'", result.getText(), containsString("Yes"));
            
            System.out.println("✅ Тест Radio Button прошел успешно");
            
        } catch (Exception e) {
            System.err.println("❌ Ошибка в тесте Radio Button: " + e.getMessage());
            throw e;
        }
    }

    @Test
    @TestTag(id = "DEMO005", description = "Тест Web Tables", category = "UI", priority = 2)
    @Story("Web Tables")
    @DisplayName("Проверка Web Tables")
    @Description("Проверяем работу с таблицами")
    public void testWebTables() {
        try {
            WebDriverUtils.openUrl(driver, "https://demoqa.com/webtables");
            
            // Проверяем наличие таблицы
            WebElement table = driver.findElement(By.cssSelector(".rt-table"));
            assertThat("Таблица должна быть видна", table.isDisplayed(), is(true));
            
            // Проверяем заголовки
            WebElement header = driver.findElement(By.cssSelector(".rt-thead"));
            assertThat("Заголовок должен содержать 'First Name'", header.getText(), containsString("First Name"));
            assertThat("Заголовок должен содержать 'Last Name'", header.getText(), containsString("Last Name"));
            assertThat("Заголовок должен содержать 'Email'", header.getText(), containsString("Email"));
            
            // Проверяем строки данных
            int rows = driver.findElements(By.cssSelector(".rt-tbody .rt-tr")).size();
            assertThat("В таблице должны быть строки данных", rows, greaterThan(0));
            
            System.out.println("✅ Тест Web Tables прошел успешно");
            
        } catch (Exception e) {
            System.err.println("❌ Ошибка в тесте Web Tables: " + e.getMessage());
            throw e;
        }
    }

    @Test
    @TestTag(id = "DEMO006", description = "Тест Buttons", category = "UI", priority = 2)
    @Story("Buttons")
    @DisplayName("Проверка Buttons")
    @Description("Проверяем работу с кнопками")
    public void testButtons() {
        try {
            WebDriverUtils.openUrl(driver, "https://demoqa.com/buttons");
            
            // Тестируем двойной клик
            WebElement doubleClickBtn = driver.findElement(By.id("doubleClickBtn"));
            actions.doubleClick(doubleClickBtn).perform();
            
            WebElement doubleClickMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("doubleClickMessage")));
            assertThat("Сообщение о двойном клике должно появиться", doubleClickMessage.isDisplayed(), is(true));
            
            // Тестируем правый клик
            WebElement rightClickBtn = driver.findElement(By.id("rightClickBtn"));
            actions.contextClick(rightClickBtn).perform();
            
            WebElement rightClickMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("rightClickMessage")));
            assertThat("Сообщение о правом клике должно появиться", rightClickMessage.isDisplayed(), is(true));
            
            // Тестируем обычный клик
            WebElement clickBtn = driver.findElement(By.xpath("//button[text()='Click Me']"));
            clickBtn.click();
            
            WebElement clickMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("dynamicClickMessage")));
            assertThat("Сообщение о клике должно появиться", clickMessage.isDisplayed(), is(true));
            
            System.out.println("✅ Тест Buttons прошел успешно");
            
        } catch (Exception e) {
            System.err.println("❌ Ошибка в тесте Buttons: " + e.getMessage());
            throw e;
        }
    }

    @Test
    @TestTag(id = "DEMO007", description = "Тест Forms", category = "UI", priority = 1)
    @Story("Forms")
    @DisplayName("Проверка Forms")
    @Description("Проверяем работу с формами")
    public void testForms() {
        try {
            WebDriverUtils.openUrl(driver, "https://demoqa.com/forms");
            
            // Ждем загрузки страницы
            WebDriverUtils.waitForPageLoad(driver);
            
            // Проверяем заголовок страницы с несколькими вариантами селекторов
            WebElement header = null;
            try {
                header = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".main-header")));
            } catch (Exception e) {
                try {
                    header = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h1")));
                } catch (Exception e2) {
                    header = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("title")));
                }
            }
            
            assertThat("Заголовок должен содержать 'Forms'", header.getText(), containsString("Forms"));
            
            // Проверяем наличие элементов форм
            WebElement practiceForm = driver.findElement(By.cssSelector("li#item-0"));
            assertThat("Practice Form должен быть виден", practiceForm.isDisplayed(), is(true));
            
            System.out.println("✅ Тест Forms прошел успешно");
            
        } catch (Exception e) {
            System.err.println("❌ Ошибка в тесте Forms: " + e.getMessage());
            throw e;
        }
    }

    @Test
    @TestTag(id = "DEMO008", description = "Тест Alerts", category = "UI", priority = 3)
    @Story("Alerts")
    @DisplayName("Проверка Alerts")
    @Description("Проверяем работу с алертами")
    public void testAlerts() {
        try {
            WebDriverUtils.openUrl(driver, "https://demoqa.com/alerts");
            
            // Ждем загрузки страницы
            WebDriverUtils.waitForPageLoad(driver);
            
            // Проверяем заголовок страницы с несколькими вариантами селекторов
            WebElement header = null;
            try {
                header = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".main-header")));
            } catch (Exception e) {
                try {
                    header = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h1")));
                } catch (Exception e2) {
                    header = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("title")));
                }
            }
            
            assertThat("Заголовок должен содержать 'Alerts'", header.getText(), containsString("Alerts"));
            
            // Проверяем наличие кнопок для алертов
            WebElement alertButton = driver.findElement(By.id("alertButton"));
            WebElement confirmButton = driver.findElement(By.id("confirmButton"));
            WebElement promptButton = driver.findElement(By.id("promptButton"));
            
            assertThat("Кнопка Alert должна быть видна", alertButton.isDisplayed(), is(true));
            assertThat("Кнопка Confirm должна быть видна", confirmButton.isDisplayed(), is(true));
            assertThat("Кнопка Prompt должна быть видна", promptButton.isDisplayed(), is(true));
            
            System.out.println("✅ Тест Alerts прошел успешно");
            
        } catch (Exception e) {
            System.err.println("❌ Ошибка в тесте Alerts: " + e.getMessage());
            throw e;
        }
    }
}
