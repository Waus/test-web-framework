package pl.tmobile.recruitment.pages;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.interactions.Actions;

import java.util.Map;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class BasePage {

    public static final String ACCEPT_ONLY_NECESSARY_COOKIES = "ACCEPT_ONLY_NECESSARY_COOKIES";
    public static final String ACCEPT_ALL_COOKIES = "ACCEPT_ALL_COOKIES";


    protected final Map<String, String> baseElements = Map.of(
            ACCEPT_ONLY_NECESSARY_COOKIES, "button[id='didomi-notice-disagree-button']",
            ACCEPT_ALL_COOKIES, "button[id='didomi-notice-agree-button']"
    );

    public String getLocatorByName(String name) {
        String locator = baseElements.get(name);
        if (locator == null) {
            throw new IllegalArgumentException("Nie znaleziono elementu o nazwie: " + name);
        }
        return locator;
    }

    public SelenideElement getElement(String name) {
        String locator = getLocatorByName(name);
        if (locator == null) {
            throw new IllegalArgumentException("Nie znaleziono elementu o nazwie: " + name);
        }

        SelenideElement element;
        if (locator.startsWith("//") || locator.startsWith("(")) {
            System.out.println("Lokator to Xpath dla: " + name);
            element = $x(locator);
        } else {
            System.out.println("Lokator to CSS dla: " + name);
            element = $(locator);
        }

        return element;
    }

    public void clickElement(String name) {
        SelenideElement element = getElement(name);

        System.out.println("Szukam elementu: " + name);

        // Debugowanie – sprawdzamy, czy element istnieje
        if (!element.exists()) {
            System.out.println("Element NIE istnieje!");
        } else {
            System.out.println("Element istnieje.");
        }

        // Debugowanie – sprawdzamy, czy element jest widoczny
        if (!element.isDisplayed()) {
            System.out.println("Element NIE jest widoczny!");
        } else {
            System.out.println("Element jest widoczny.");
        }

        // Próbujemy kliknąć element
        try {
            element.shouldBe(visible, enabled).click();
            System.out.println("Kliknięto element: " + name);
        } catch (Exception e) {
            System.out.println("Nie udało się kliknąć elementu normalnie, próbuję JavaScript...");
            executeJavaScript("arguments[0].click();", element);
        }
    }


    public void hoverOverElement(String name) {
        SelenideElement element = getElement(name);

        System.out.println("Próbuję najechać na: " + name);
        System.out.println("XPath: " + element);

        // Sprawdzamy, czy element istnieje
        if (!element.exists()) {
            throw new IllegalStateException("Element NIE istnieje w DOM!");
        } else {
            System.out.println("Element istnieje w DOM.");
        }

        // Sprawdzamy, czy element jest widoczny
        if (!element.isDisplayed()) {
            throw new IllegalStateException("Element NIE jest widoczny!");
        } else {
            System.out.println("Element jest widoczny.");
        }

        // Sprawdzamy style
        System.out.println("Wartość display: " + element.getCssValue("display"));
        System.out.println("Wartość opacity: " + element.getCssValue("opacity"));

        // Najeżdżamy myszką
        Actions actions = new Actions(webdriver().driver().getWebDriver());
        actions.moveToElement(element).perform();
        System.out.println("Najechano na element: " + name);
    }

}
