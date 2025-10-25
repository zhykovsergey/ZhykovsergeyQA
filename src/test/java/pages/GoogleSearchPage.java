package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Page Object для Google Search
 */
public class GoogleSearchPage extends BasePage {
    
    // Локаторы элементов
    @FindBy(name = "q")
    private WebElement searchInput;
    
    @FindBy(name = "btnK")
    private WebElement searchButton;
    
    @FindBy(id = "search")
    private WebElement searchResults;
    
    @FindBy(css = "h3")
    private WebElement firstResult;
    
    @FindBy(css = "img[alt='Google']")
    private WebElement googleLogo;
    
    public GoogleSearchPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Открыть Google
     */
    @Step("Открыть Google")
    public GoogleSearchPage openGoogle() {
        openUrl("https://www.google.com");
        waitForPageLoad();
        return this;
    }
    
    /**
     * Ввести поисковый запрос
     */
    @Step("Ввести поисковый запрос: {query}")
    public GoogleSearchPage enterSearchQuery(String query) {
        waitForElement(searchInput);
        enterText(searchInput, query);
        return this;
    }
    
    /**
     * Нажать кнопку поиска
     */
    @Step("Нажать кнопку поиска")
    public GoogleSearchPage clickSearchButton() {
        waitForClickable(searchButton);
        clickElement(searchButton);
        waitForPageLoad();
        return this;
    }
    
    /**
     * Выполнить поиск
     */
    @Step("Выполнить поиск: {query}")
    public GoogleSearchPage searchFor(String query) {
        return enterSearchQuery(query).clickSearchButton();
    }
    
    /**
     * Получить текст первого результата
     */
    @Step("Получить текст первого результата")
    public String getFirstResultText() {
        waitForElement(firstResult);
        return getElementText(firstResult);
    }
    
    /**
     * Проверить, что результаты поиска отображаются
     */
    @Step("Проверить отображение результатов поиска")
    public boolean areSearchResultsDisplayed() {
        return isElementDisplayed(searchResults);
    }
    
    /**
     * Проверить, что логотип Google отображается
     */
    @Step("Проверить отображение логотипа Google")
    public boolean isGoogleLogoDisplayed() {
        return isElementDisplayed(googleLogo);
    }
    
    /**
     * Получить заголовок страницы
     */
    @Step("Получить заголовок страницы")
    public String getPageTitle() {
        return driver.getTitle();
    }

    @Step("Получить количество результатов поиска")
    public int getSearchResultsCount() {
        try {
            return driver.findElements(org.openqa.selenium.By.cssSelector("h3")).size();
        } catch (Exception e) {
            return 0;
        }
    }

    @Step("Проверить отображение логотипа Google")
    public boolean isLogoDisplayed() {
        return isElementDisplayed(googleLogo);
    }
}
