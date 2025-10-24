package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object для страницы оформления заказа SauceDemo
 */
public class SauceDemoCheckoutPage extends BasePage {
    
    // Локаторы элементов
    @FindBy(className = "title")
    private WebElement pageTitle;
    
    @FindBy(id = "first-name")
    private WebElement firstNameInput;
    
    @FindBy(id = "last-name")
    private WebElement lastNameInput;
    
    @FindBy(id = "postal-code")
    private WebElement postalCodeInput;
    
    @FindBy(id = "continue")
    private WebElement continueButton;
    
    @FindBy(id = "cancel")
    private WebElement cancelButton;
    
    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;
    
    public SauceDemoCheckoutPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Получить заголовок страницы
     */
    @Step("Получить заголовок страницы")
    public String getPageTitle() {
        waitForElement(pageTitle);
        return getElementText(pageTitle);
    }
    
    /**
     * Ввести имя
     */
    @Step("Ввести имя: {firstName}")
    public SauceDemoCheckoutPage enterFirstName(String firstName) {
        waitForElement(firstNameInput);
        enterText(firstNameInput, firstName);
        return this;
    }
    
    /**
     * Ввести фамилию
     */
    @Step("Ввести фамилию: {lastName}")
    public SauceDemoCheckoutPage enterLastName(String lastName) {
        waitForElement(lastNameInput);
        enterText(lastNameInput, lastName);
        return this;
    }
    
    /**
     * Ввести почтовый индекс
     */
    @Step("Ввести почтовый индекс: {postalCode}")
    public SauceDemoCheckoutPage enterPostalCode(String postalCode) {
        waitForElement(postalCodeInput);
        enterText(postalCodeInput, postalCode);
        return this;
    }
    
    /**
     * Заполнить форму оформления заказа
     */
    @Step("Заполнить форму оформления заказа")
    public SauceDemoCheckoutPage fillCheckoutForm(String firstName, String lastName, String postalCode) {
        return enterFirstName(firstName)
                .enterLastName(lastName)
                .enterPostalCode(postalCode);
    }
    
    /**
     * Продолжить оформление заказа
     */
    @Step("Продолжить оформление заказа")
    public SauceDemoCheckoutPage continueCheckout() {
        waitForClickable(continueButton);
        clickElement(continueButton);
        return this;
    }
    
    /**
     * Отменить оформление заказа
     */
    @Step("Отменить оформление заказа")
    public SauceDemoCheckoutPage cancelCheckout() {
        waitForClickable(cancelButton);
        clickElement(cancelButton);
        return this;
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
     * Проверить, что страница оформления заказа загружена
     */
    @Step("Проверить загрузку страницы оформления заказа")
    public boolean isCheckoutPageLoaded() {
        try {
            // Ждем загрузки страницы
            waitForPageLoad();
            
            // Проверяем URL
            String currentUrl = driver.getCurrentUrl();
            if (!currentUrl.contains("checkout-step-one")) {
                return false;
            }
            
            // Проверяем заголовок страницы
            if (isElementDisplayed(pageTitle)) {
                String titleText = getElementText(pageTitle);
                return titleText.contains("Checkout");
            }
            
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Проверить, что поля формы отображаются
     */
    @Step("Проверить отображение полей формы")
    public boolean areFormFieldsDisplayed() {
        return isElementDisplayed(firstNameInput) && 
               isElementDisplayed(lastNameInput) && 
               isElementDisplayed(postalCodeInput);
    }
}
