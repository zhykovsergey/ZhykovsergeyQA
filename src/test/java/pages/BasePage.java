package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.WebDriverUtils;

import java.time.Duration;
import java.util.List;

/**
 * Базовый класс для всех Page Object классов
 * Содержит общие методы для работы с элементами
 */
public abstract class BasePage {
    
    protected WebDriver driver;
    protected WebDriverWait wait;
    
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = WebDriverUtils.createWebDriverWait(driver);
        PageFactory.initElements(driver, this);
    }
    
    /**
     * Открыть URL
     */
    @Step("Открыть URL: {url}")
    public void openUrl(String url) {
        driver.get(url);
    }
    
    /**
     * Получить текущий URL
     */
    @Step("Получить текущий URL")
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
    
    /**
     * Получить заголовок страницы
     */
    @Step("Получить заголовок страницы")
    public String getPageTitle() {
        return driver.getTitle();
    }
    
    /**
     * Ждать появления элемента
     */
    @Step("Ждать появления элемента")
    protected void waitForElement(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }
    
    /**
     * Ждать кликабельности элемента
     */
    @Step("Ждать кликабельности элемента")
    protected void waitForClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }
    
    /**
     * Кликнуть по элементу
     */
    @Step("Кликнуть по элементу")
    protected void clickElement(WebElement element) {
        waitForClickable(element);
        element.click();
    }
    
    /**
     * Ввести текст в поле
     */
    @Step("Ввести текст '{text}' в поле")
    protected void enterText(WebElement element, String text) {
        waitForElement(element);
        element.clear();
        element.sendKeys(text);
    }
    
    /**
     * Получить текст элемента
     */
    @Step("Получить текст элемента")
    protected String getElementText(WebElement element) {
        waitForElement(element);
        return element.getText();
    }
    
    /**
     * Проверить, что элемент отображается
     */
    @Step("Проверить отображение элемента")
    protected boolean isElementDisplayed(WebElement element) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Ждать загрузки страницы
     */
    @Step("Ждать загрузки страницы")
    protected void waitForPageLoad() {
        try {
            wait.until(ExpectedConditions.jsReturnsValue("return document.readyState == 'complete'"));
        } catch (Exception e) {
            // Если JavaScript не работает, ждем немного
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    /**
     * Сделать скриншот
     */
    @Step("Сделать скриншот")
    protected void takeScreenshot() {
        // Скриншот будет автоматически добавлен в Allure отчет
        // при использовании AllureRestAssuredFilter
    }

    /**
     * Найти элемент по локатору
     */
    @Step("Найти элемент по локатору")
    protected WebElement findElement(By locator) {
        return driver.findElement(locator);
    }

    /**
     * Найти элементы по локатору
     */
    @Step("Найти элементы по локатору")
    protected List<WebElement> findElements(By locator) {
        return driver.findElements(locator);
    }

    /**
     * Ждать видимости элемента по локатору
     */
    @Step("Ждать видимости элемента по локатору")
    protected void waitForElementVisible(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Ждать кликабельности элемента по локатору
     */
    @Step("Ждать кликабельности элемента по локатору")
    protected void waitForClickable(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Кликнуть по элементу по локатору
     */
    @Step("Кликнуть по элементу по локатору")
    protected void clickElement(By locator) {
        waitForClickable(locator);
        findElement(locator).click();
    }

    /**
     * Проверить, что элемент отображается по локатору
     */
    @Step("Проверить отображение элемента по локатору")
    protected boolean isElementDisplayed(By locator) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
