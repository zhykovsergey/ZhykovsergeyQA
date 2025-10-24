package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object для страницы логина SauceDemo
 */
public class SauceDemoLoginPage extends BasePage {
    
    // Локаторы элементов
    @FindBy(id = "user-name")
    private WebElement usernameInput;
    
    @FindBy(id = "password")
    private WebElement passwordInput;
    
    @FindBy(id = "login-button")
    private WebElement loginButton;
    
    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;
    
    @FindBy(className = "login_logo")
    private WebElement logo;
    
    public SauceDemoLoginPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Открыть страницу логина
     */
    @Step("Открыть страницу логина SauceDemo")
    public SauceDemoLoginPage openLoginPage() {
        openUrl("https://www.saucedemo.com");
        waitForPageLoad();
        return this;
    }
    
    /**
     * Ввести имя пользователя
     */
    @Step("Ввести имя пользователя: {username}")
    public SauceDemoLoginPage enterUsername(String username) {
        waitForElement(usernameInput);
        enterText(usernameInput, username);
        return this;
    }
    
    /**
     * Ввести пароль
     */
    @Step("Ввести пароль")
    public SauceDemoLoginPage enterPassword(String password) {
        waitForElement(passwordInput);
        enterText(passwordInput, password);
        return this;
    }
    
    /**
     * Нажать кнопку логина
     */
    @Step("Нажать кнопку логина")
    public SauceDemoLoginPage clickLoginButton() {
        waitForClickable(loginButton);
        clickElement(loginButton);
        return this;
    }
    
    /**
     * Выполнить логин
     */
    @Step("Выполнить логин с пользователем: {username}")
    public SauceDemoLoginPage login(String username, String password) {
        return enterUsername(username)
                .enterPassword(password)
                .clickLoginButton();
    }
    
    /**
     * Получить сообщение об ошибке
     */
    @Step("Получить сообщение об ошибке")
    public String getErrorMessage() {
        if (isElementDisplayed(errorMessage)) {
            return getElementText(errorMessage);
        }
        return "";
    }
    
    /**
     * Проверить, что сообщение об ошибке отображается
     */
    @Step("Проверить отображение сообщения об ошибке")
    public boolean isErrorMessageDisplayed() {
        return isElementDisplayed(errorMessage);
    }
    
    /**
     * Проверить, что логотип отображается
     */
    @Step("Проверить отображение логотипа")
    public boolean isLogoDisplayed() {
        return isElementDisplayed(logo);
    }
    
    /**
     * Проверить, что поля ввода отображаются
     */
    @Step("Проверить отображение полей ввода")
    public boolean areInputFieldsDisplayed() {
        return isElementDisplayed(usernameInput) && isElementDisplayed(passwordInput);
    }
}
