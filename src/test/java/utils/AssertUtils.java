package utils;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebElement;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Утилиты для улучшенных ассертов с детальными сообщениями
 */
public class AssertUtils {

    @Step("Проверить, что статус-код равен {expectedStatusCode}")
    public static void assertStatusCode(int expectedStatusCode, int actualStatusCode, String message) {
        Allure.addAttachment("Status Code Check", "text/plain",
                String.format("Ожидаемый: %d\nФактический: %d\nСообщение: %s",
                        expectedStatusCode, actualStatusCode, message));
        Assertions.assertEquals(expectedStatusCode, actualStatusCode, message);
    }

    @Step("Проверить, что элемент '{elementName}' виден")
    public static void assertElementVisible(WebElement element, String elementName, String message) {
        Allure.addAttachment("Element Visibility Check", "text/plain",
                String.format("Элемент: %s\nВиден: %s\nСообщение: %s",
                        elementName, element.isDisplayed(), message));
        assertThat(message, element.isDisplayed(), is(true));
    }

    @Step("Проверить, что элемент '{elementName}' содержит текст '{expectedText}'")
    public static void assertElementText(WebElement element, String expectedText, String elementName, String message) {
        String actualText = element.getText();
        Allure.addAttachment("Element Text Check", "text/plain",
                String.format("Элемент: %s\nОжидаемый текст: '%s'\nФактический текст: '%s'\nСообщение: %s",
                        elementName, expectedText, actualText, message));
        assertThat(message, actualText, containsString(expectedText));
    }

    @Step("Проверить, что URL содержит '{expectedPart}'")
    public static void assertUrlContains(String expectedPart, String actualUrl, String message) {
        Allure.addAttachment("URL Check", "text/plain",
                String.format("Ожидаемая часть: '%s'\nФактический URL: '%s'\nСообщение: %s",
                        expectedPart, actualUrl, message));
        assertThat(message, actualUrl, containsString(expectedPart));
    }

    @Step("Проверить, что заголовок страницы содержит '{expectedTitle}'")
    public static void assertPageTitleContains(String expectedTitle, String actualTitle, String message) {
        Allure.addAttachment("Page Title Check", "text/plain",
                String.format("Ожидаемый заголовок: '%s'\nФактический заголовок: '%s'\nСообщение: %s",
                        expectedTitle, actualTitle, message));
        assertThat(message, actualTitle, containsString(expectedTitle));
    }

    @Step("Проверить, что значение равно {expected}")
    public static <T> void assertEquals(T expected, T actual, String message) {
        Allure.addAttachment("Value Equality Check", "text/plain",
                String.format("Ожидалось: %s\nФактически: %s\nСообщение: %s",
                        expected, actual, message));
        Assertions.assertEquals(expected, actual, message);
    }

    @Step("Проверить, что значение не равно {unexpected}")
    public static <T> void assertNotEquals(T unexpected, T actual, String message) {
        Allure.addAttachment("Value Inequality Check", "text/plain",
                String.format("Не ожидалось: %s\nФактически: %s\nСообщение: %s",
                        unexpected, actual, message));
        Assertions.assertNotEquals(unexpected, actual, message);
    }

    @Step("Проверить, что объект не null")
    public static void assertNotNull(Object actual, String message) {
        Allure.addAttachment("Null Check", "text/plain",
                String.format("Ожидалось: не null\nФактически: %s\nСообщение: %s",
                        actual, message));
        Assertions.assertNotNull(actual, message);
    }

    @Step("Проверить, что объект null")
    public static void assertNull(Object actual, String message) {
        Allure.addAttachment("Null Check", "text/plain",
                String.format("Ожидалось: null\nФактически: %s\nСообщение: %s",
                        actual, message));
        Assertions.assertNull(actual, message);
    }

    @Step("Проверить, что условие истинно")
    public static void assertTrue(boolean condition, String message) {
        Allure.addAttachment("Boolean Check", "text/plain",
                String.format("Ожидалось: true\nФактически: %s\nСообщение: %s",
                        condition, message));
        Assertions.assertTrue(condition, message);
    }

    @Step("Проверить, что условие ложно")
    public static void assertFalse(boolean condition, String message) {
        Allure.addAttachment("Boolean Check", "text/plain",
                String.format("Ожидалось: false\nФактически: %s\nСообщение: %s",
                        condition, message));
        Assertions.assertFalse(condition, message);
    }

    @Step("Проверить, что строка не пустая")
    public static void assertNotEmpty(String actual, String message) {
        Allure.addAttachment("String Check", "text/plain",
                String.format("Ожидалось: не пустая строка\nФактически: '%s'\nСообщение: %s",
                        actual, message));
        assertThat(message, actual, not(emptyString()));
    }

    @Step("Проверить, что коллекция не пустая")
    public static <T> void assertNotEmpty(java.util.Collection<T> actual, String message) {
        Allure.addAttachment("Collection Check", "text/plain",
                String.format("Ожидалось: не пустая коллекция\nФактически: %s\nСообщение: %s",
                        actual, message));
        assertThat(message, actual, not(empty()));
    }

    @Step("Проверить, что размер равен {expectedSize}")
    public static void assertSize(int expectedSize, int actualSize, String message) {
        Allure.addAttachment("Size Check", "text/plain",
                String.format("Ожидаемый размер: %d\nФактический размер: %d\nСообщение: %s",
                        expectedSize, actualSize, message));
        Assertions.assertEquals(expectedSize, actualSize, message);
    }

    @Step("Проверить, что значение больше {expectedValue}")
    public static <T extends Comparable<T>> void assertGreaterThan(T expectedValue, T actualValue, String message) {
        Allure.addAttachment("Comparison Check", "text/plain",
                String.format("Ожидалось: значение больше %s\nФактически: %s\nСообщение: %s",
                        expectedValue, actualValue, message));
        assertThat(message, actualValue, greaterThan(expectedValue));
    }

    @Step("Проверить, что значение меньше {expectedValue}")
    public static <T extends Comparable<T>> void assertLessThan(T expectedValue, T actualValue, String message) {
        Allure.addAttachment("Comparison Check", "text/plain",
                String.format("Ожидалось: значение меньше %s\nФактически: %s\nСообщение: %s",
                        expectedValue, actualValue, message));
        assertThat(message, actualValue, lessThan(expectedValue));
    }

    @Step("Проверить, что строка содержит '{expectedSubstring}'")
    public static void assertContains(String expectedSubstring, String actualString, String message) {
        Allure.addAttachment("String Contains Check", "text/plain",
                String.format("Ожидалось: строка содержит '%s'\nФактически: '%s'\nСообщение: %s",
                        expectedSubstring, actualString, message));
        assertThat(message, actualString, containsString(expectedSubstring));
    }

    @Step("Проверить, что строка не содержит '{unexpectedSubstring}'")
    public static void assertNotContains(String unexpectedSubstring, String actualString, String message) {
        Allure.addAttachment("String Not Contains Check", "text/plain",
                String.format("Не ожидалось: строка содержит '%s'\nФактически: '%s'\nСообщение: %s",
                        unexpectedSubstring, actualString, message));
        assertThat(message, actualString, not(containsString(unexpectedSubstring)));
    }
}


