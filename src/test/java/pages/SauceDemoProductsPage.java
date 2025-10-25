package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object для страницы продуктов SauceDemo
 */
public class SauceDemoProductsPage extends BasePage {

    // Локаторы элементов
    private static final By PRODUCTS_CONTAINER = By.className("inventory_container");
    private static final By PRODUCT_ITEMS = By.className("inventory_item");
    private static final By ADD_TO_CART_BUTTONS = By.xpath("//button[contains(text(), 'Add to cart')]");
    private static final By REMOVE_BUTTONS = By.xpath("//button[contains(text(), 'Remove')]");
    private static final By CART_BADGE = By.className("shopping_cart_badge");
    private static final By CART_ICON = By.className("shopping_cart_link");
    private static final By PRODUCT_NAMES = By.className("inventory_item_name");
    private static final By PRODUCT_PRICES = By.className("inventory_item_price");
    private static final By SORT_DROPDOWN = By.className("product_sort_container");
    private static final By MENU_BUTTON = By.id("react-burger-menu-btn");
    private static final By LOGOUT_LINK = By.id("logout_sidebar_link");

    public SauceDemoProductsPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Проверить, что страница продуктов загружена
     */
    @Step("Проверить загрузку страницы продуктов")
    public boolean isProductsPageLoaded() {
        try {
            waitForElementVisible(PRODUCTS_CONTAINER);
            return isElementDisplayed(findElement(PRODUCTS_CONTAINER));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Получить количество товаров на странице
     */
    @Step("Получить количество товаров")
    public int getProductCount() {
        waitForElementVisible(PRODUCTS_CONTAINER);
        List<WebElement> products = findElements(PRODUCT_ITEMS);
        return products.size();
    }

    /**
     * Добавить первый товар в корзину
     */
    @Step("Добавить первый товар в корзину")
    public void addFirstProductToCart() {
        waitForElementVisible(PRODUCTS_CONTAINER);
        List<WebElement> addButtons = findElements(ADD_TO_CART_BUTTONS);
        if (!addButtons.isEmpty()) {
            clickElement(addButtons.get(0));
        }
    }

    /**
     * Добавить товар в корзину по индексу
     */
    @Step("Добавить товар в корзину по индексу: {index}")
    public void addProductToCart(int index) {
        waitForElementVisible(PRODUCTS_CONTAINER);
        List<WebElement> addButtons = findElements(ADD_TO_CART_BUTTONS);
        if (index < addButtons.size()) {
            clickElement(addButtons.get(index));
        }
    }

    /**
     * Удалить товар из корзины по индексу
     */
    @Step("Удалить товар из корзины по индексу: {index}")
    public void removeProductFromCart(int index) {
        waitForElementVisible(PRODUCTS_CONTAINER);
        List<WebElement> removeButtons = findElements(REMOVE_BUTTONS);
        if (index < removeButtons.size()) {
            clickElement(removeButtons.get(index));
        }
    }

    /**
     * Получить количество товаров в корзине
     */
    @Step("Получить количество товаров в корзине")
    public int getCartItemCount() {
        try {
            if (isElementDisplayed(findElement(CART_BADGE))) {
                String countText = getElementText(findElement(CART_BADGE));
                return Integer.parseInt(countText);
            }
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Открыть корзину
     */
    @Step("Открыть корзину")
    public void openCart() {
        waitForClickable(CART_ICON);
        clickElement(CART_ICON);
    }

    /**
     * Получить название товара по индексу
     */
    @Step("Получить название товара по индексу: {index}")
    public String getProductName(int index) {
        waitForElementVisible(PRODUCTS_CONTAINER);
        List<WebElement> productNames = findElements(PRODUCT_NAMES);
        if (index < productNames.size()) {
            return getElementText(productNames.get(index));
        }
        return "";
    }

    /**
     * Получить цену товара по индексу
     */
    @Step("Получить цену товара по индексу: {index}")
    public String getProductPrice(int index) {
        waitForElementVisible(PRODUCTS_CONTAINER);
        List<WebElement> productPrices = findElements(PRODUCT_PRICES);
        if (index < productPrices.size()) {
            return getElementText(productPrices.get(index));
        }
        return "";
    }

    /**
     * Сортировать товары
     */
    @Step("Сортировать товары по: {sortOption}")
    public void sortProducts(String sortOption) {
        waitForElementVisible(SORT_DROPDOWN);
        WebElement sortDropdown = findElement(SORT_DROPDOWN);
        sortDropdown.click();
        
        // Выбираем опцию сортировки
        By sortOptionLocator = By.xpath("//option[contains(text(), '" + sortOption + "')]");
        waitForElementVisible(sortOptionLocator);
        clickElement(sortOptionLocator);
    }

    /**
     * Открыть меню
     */
    @Step("Открыть меню")
    public void openMenu() {
        waitForClickable(MENU_BUTTON);
        clickElement(MENU_BUTTON);
    }

    /**
     * Выйти из системы
     */
    @Step("Выйти из системы")
    public void logout() {
        openMenu();
        waitForClickable(LOGOUT_LINK);
        clickElement(LOGOUT_LINK);
    }

    /**
     * Проверить, что товар добавлен в корзину
     */
    @Step("Проверить, что товар добавлен в корзину")
    public boolean isProductAddedToCart() {
        try {
            return isElementDisplayed(findElement(REMOVE_BUTTONS)) && getCartItemCount() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Получить все названия товаров
     */
    @Step("Получить все названия товаров")
    public List<String> getAllProductNames() {
        waitForElementVisible(PRODUCTS_CONTAINER);
        List<WebElement> productNames = findElements(PRODUCT_NAMES);
        return productNames.stream()
            .map(WebElement::getText)
            .toList();
    }

    /**
     * Получить все цены товаров
     */
    @Step("Получить все цены товаров")
    public List<String> getAllProductPrices() {
        waitForElementVisible(PRODUCTS_CONTAINER);
        List<WebElement> productPrices = findElements(PRODUCT_PRICES);
        return productPrices.stream()
            .map(WebElement::getText)
            .toList();
    }

    /**
     * Проверить, что товары отображаются
     */
    @Step("Проверить отображение товаров")
    public boolean areProductsDisplayed() {
        try {
            waitForElementVisible(PRODUCTS_CONTAINER);
            return isElementDisplayed(PRODUCTS_CONTAINER) && getProductCount() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Проверить, что корзина отображается
     */
    @Step("Проверить отображение корзины")
    public boolean isCartDisplayed() {
        try {
            return isElementDisplayed(CART_ICON);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Получить название первого товара
     */
    @Step("Получить название первого товара")
    public String getFirstProductName() {
        waitForElementVisible(PRODUCT_NAMES);
        List<WebElement> productNames = findElements(PRODUCT_NAMES);
        if (!productNames.isEmpty()) {
            return productNames.get(0).getText();
        }
        return "";
    }

    /**
     * Получить цену первого товара
     */
    @Step("Получить цену первого товара")
    public String getFirstProductPrice() {
        waitForElementVisible(PRODUCT_PRICES);
        List<WebElement> productPrices = findElements(PRODUCT_PRICES);
        if (!productPrices.isEmpty()) {
            return productPrices.get(0).getText();
        }
        return "";
    }

    @Step("Проверить отображение значка корзины")
    public boolean isCartBadgeDisplayed() {
        return isElementDisplayed(CART_BADGE);
    }

    @Step("Нажать на кнопку корзины")
    public void clickCartButton() {
        clickElement(CART_ICON);
    }
}