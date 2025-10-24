package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Page Object для страницы корзины SauceDemo
 */
public class SauceDemoCartPage extends BasePage {
    
    // Локаторы элементов
    @FindBy(className = "title")
    private WebElement pageTitle;
    
    @FindBy(className = "cart_item")
    private List<WebElement> cartItems;
    
    @FindBy(css = ".inventory_item_name")
    private List<WebElement> itemNames;
    
    @FindBy(css = ".inventory_item_price")
    private List<WebElement> itemPrices;
    
    @FindBy(css = ".cart_quantity")
    private List<WebElement> itemQuantities;
    
    @FindBy(css = ".btn_secondary")
    private List<WebElement> removeButtons;
    
    @FindBy(id = "continue-shopping")
    private WebElement continueShoppingButton;
    
    @FindBy(id = "checkout")
    private WebElement checkoutButton;
    
    public SauceDemoCartPage(WebDriver driver) {
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
     * Получить количество товаров в корзине
     */
    @Step("Получить количество товаров в корзине")
    public int getCartItemCount() {
        return cartItems.size();
    }
    
    /**
     * Получить название первого товара
     */
    @Step("Получить название первого товара")
    public String getFirstItemName() {
        if (cartItems.size() > 0) {
            waitForElement(itemNames.get(0));
            return getElementText(itemNames.get(0));
        }
        return "";
    }
    
    /**
     * Получить цену первого товара
     */
    @Step("Получить цену первого товара")
    public String getFirstItemPrice() {
        if (cartItems.size() > 0) {
            waitForElement(itemPrices.get(0));
            return getElementText(itemPrices.get(0));
        }
        return "";
    }
    
    /**
     * Удалить первый товар из корзины
     */
    @Step("Удалить первый товар из корзины")
    public SauceDemoCartPage removeFirstItem() {
        if (removeButtons.size() > 0) {
            waitForClickable(removeButtons.get(0));
            clickElement(removeButtons.get(0));
        }
        return this;
    }
    
    /**
     * Продолжить покупки
     */
    @Step("Продолжить покупки")
    public SauceDemoCartPage continueShopping() {
        waitForClickable(continueShoppingButton);
        clickElement(continueShoppingButton);
        return this;
    }
    
    /**
     * Перейти к оформлению заказа
     */
    @Step("Перейти к оформлению заказа")
    public SauceDemoCartPage proceedToCheckout() {
        try {
            // Ждем немного для загрузки страницы
            Thread.sleep(1000);
            
            // Проверяем, что кнопка отображается
            if (isElementDisplayed(checkoutButton)) {
                waitForClickable(checkoutButton);
                clickElement(checkoutButton);
            } else {
                throw new RuntimeException("Кнопка Checkout не найдена");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Прервано ожидание", e);
        }
        return this;
    }
    
    /**
     * Проверить, что страница корзины загружена
     */
    @Step("Проверить загрузку страницы корзины")
    public boolean isCartPageLoaded() {
        return isElementDisplayed(pageTitle) && 
               getElementText(pageTitle).equals("Your Cart");
    }
    
    /**
     * Проверить, что корзина пуста
     */
    @Step("Проверить, что корзина пуста")
    public boolean isCartEmpty() {
        return cartItems.size() == 0;
    }
    
    /**
     * Проверить, что товары отображаются в корзине
     */
    @Step("Проверить отображение товаров в корзине")
    public boolean areItemsDisplayed() {
        return cartItems.size() > 0;
    }
}
