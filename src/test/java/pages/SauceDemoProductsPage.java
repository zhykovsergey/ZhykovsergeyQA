package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Page Object для страницы продуктов SauceDemo
 */
public class SauceDemoProductsPage extends BasePage {
    
    // Локаторы элементов
    @FindBy(className = "title")
    private WebElement pageTitle;
    
    @FindBy(className = "inventory_item")
    private List<WebElement> productItems;
    
    @FindBy(css = ".inventory_item_name")
    private List<WebElement> productNames;
    
    @FindBy(css = ".inventory_item_price")
    private List<WebElement> productPrices;
    
    @FindBy(css = ".btn_inventory")
    private List<WebElement> addToCartButtons;
    
    @FindBy(id = "shopping_cart_container")
    private WebElement cartIcon;
    
    @FindBy(className = "shopping_cart_badge")
    private WebElement cartBadge;
    
    @FindBy(id = "react-burger-menu-btn")
    private WebElement menuButton;
    
    @FindBy(id = "logout_sidebar_link")
    private WebElement logoutLink;
    
    public SauceDemoProductsPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Получить заголовок страницы
     */
    @Step("Получить заголовок страницы")
    public String getPageTitle() {
        waitForElement(this.pageTitle);
        return getElementText(this.pageTitle);
    }
    
    /**
     * Получить количество продуктов
     */
    @Step("Получить количество продуктов")
    public int getProductCount() {
        waitForElement(productItems.get(0));
        return productItems.size();
    }
    
    /**
     * Получить название первого продукта
     */
    @Step("Получить название первого продукта")
    public String getFirstProductName() {
        waitForElement(productNames.get(0));
        return getElementText(productNames.get(0));
    }
    
    /**
     * Получить цену первого продукта
     */
    @Step("Получить цену первого продукта")
    public String getFirstProductPrice() {
        waitForElement(productPrices.get(0));
        return getElementText(productPrices.get(0));
    }
    
    /**
     * Добавить первый продукт в корзину
     */
    @Step("Добавить первый продукт в корзину")
    public SauceDemoProductsPage addFirstProductToCart() {
        waitForClickable(addToCartButtons.get(0));
        clickElement(addToCartButtons.get(0));
        return this;
    }
    
    /**
     * Получить количество товаров в корзине
     */
    @Step("Получить количество товаров в корзине")
    public String getCartItemCount() {
        if (isElementDisplayed(cartBadge)) {
            return getElementText(cartBadge);
        }
        return "0";
    }
    
    /**
     * Открыть корзину
     */
    @Step("Открыть корзину")
    public SauceDemoProductsPage openCart() {
        try {
            // Ждем немного для загрузки страницы
            Thread.sleep(1000);
            
            // Проверяем, что корзина не пуста
            String cartCount = getCartItemCount();
            if ("0".equals(cartCount)) {
                throw new RuntimeException("Корзина пуста, нельзя перейти в корзину");
            }
            
            // Используем ID селектор
            waitForClickable(cartIcon);
            clickElement(cartIcon);
            
            // Ждем перехода на страницу корзины
            Thread.sleep(1000);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Прервано ожидание", e);
        }
        return this;
    }
    
    /**
     * Открыть меню
     */
    @Step("Открыть меню")
    public SauceDemoProductsPage openMenu() {
        waitForClickable(menuButton);
        clickElement(menuButton);
        return this;
    }
    
    /**
     * Выполнить логаут
     */
    @Step("Выполнить логаут")
    public SauceDemoProductsPage logout() {
        openMenu();
        waitForClickable(logoutLink);
        clickElement(logoutLink);
        return this;
    }
    
    /**
     * Проверить, что страница продуктов загружена
     */
    @Step("Проверить загрузку страницы продуктов")
    public boolean isProductsPageLoaded() {
        return isElementDisplayed(pageTitle) && 
               getElementText(pageTitle).equals("Products");
    }
    
    /**
     * Проверить, что продукты отображаются
     */
    @Step("Проверить отображение продуктов")
    public boolean areProductsDisplayed() {
        return productItems.size() > 0;
    }
    
    /**
     * Проверить, что корзина отображается
     */
    @Step("Проверить отображение корзины")
    public boolean isCartDisplayed() {
        return isElementDisplayed(cartIcon);
    }
}
