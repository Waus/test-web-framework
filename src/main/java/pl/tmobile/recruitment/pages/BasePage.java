package pl.tmobile.recruitment.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class BasePage {
    private static final Logger logger = LogManager.getLogger(BasePage.class);

    public static final String ACCEPT_ONLY_NECESSARY_COOKIES = "ACCEPT_ONLY_NECESSARY_COOKIES";
    public static final String ACCEPT_ALL_COOKIES = "ACCEPT_ALL_COOKIES";

    protected Map<String, String> elements = new HashMap<>(Map.of(
            ACCEPT_ONLY_NECESSARY_COOKIES, "button[id='didomi-notice-disagree-button']",
            ACCEPT_ALL_COOKIES, "button[id='didomi-notice-agree-button']"
    ));

    public String getLocatorByName(String name) {
        String locator = elements.get(name);
        if (locator == null) {
            throw new IllegalArgumentException("Nie znaleziono elementu o nazwie: " + name);
        }
        return locator;
    }

    public SelenideElement getElement(String name) {
        String locator = getLocatorByName(name);

        SelenideElement element;
        if (locator.startsWith("//") || locator.startsWith("(")) {
            logger.info("Lokator to Xpath dla: " + name);
            element = $x(locator);
        } else {
            logger.info("Lokator to CSS dla: " + name);
            element = $(locator);
        }

        return element;
    }

    public void clickElement(String elementName) {
        SelenideElement element = getElement(elementName);

        logger.info("Szukam elementu: '{}'", elementName);

        if (!checkIfElementExists(elementName)) {
            throw new NoSuchElementException("Element '" + elementName + "' NIE istnieje!");
        }
        logger.info("Element '{}' istnieje.", elementName);

        element.scrollIntoView("{behavior: 'smooth', block: 'center'}");

        if (!checkIfElementIsVisible(elementName)) {
            throw new ElementNotInteractableException("Element '" + elementName + "' NIE istnieje!");
        }
        logger.info("Element '{}' jest widoczny.", elementName);

        if (!element.isEnabled()) {
            throw new NoSuchElementException("Element '" + elementName + "' NIE istnieje!");
        }
        logger.info("Element '{}' jest widoczny.", elementName);

        try {
            element.click();
            logger.info("Kliknięto element: '{}'", elementName);
        } catch (Exception e) {
            logger.warn("Nie udało się kliknąć elementu '{}' normalnie, próbuję JavaScript...", elementName);
            executeJavaScript("arguments[0].click();", element);
            logger.info("Kliknięcie JavaScript wykonane dla elementu: '{}'", elementName);
        }
    }


    public void hoverOverElement(String name) {
        SelenideElement element = getElement(name);

        logger.info("Próbuję najechać na: " + name);
        logger.info("XPath: " + element);

        if (!element.exists()) {
            throw new IllegalStateException("Element NIE istnieje w DOM!");
        } else {
            logger.info("Element istnieje w DOM.");
        }

        if (!element.isDisplayed()) {
            throw new IllegalStateException("Element NIE jest widoczny!");
        } else {
            logger.info("Element jest widoczny.");
        }

        logger.info("Wartość display: " + element.getCssValue("display"));
        logger.info("Wartość opacity: " + element.getCssValue("opacity"));
        
        Actions actions = new Actions(webdriver().driver().getWebDriver());
        actions.moveToElement(element).perform();
        logger.info("Najechano na element: " + name);
    }

    public boolean checkIfElementExists(String elementName) {
        String locator = getLocatorByName(elementName);
        if (locator == null) {
            throw new IllegalArgumentException("Nie znaleziono elementu o nazwie: " + elementName);
        }

        SelenideElement element = getElement(elementName);

        try {
            element.should(Condition.exist, Duration.ofSeconds(5));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean checkIfElementIsVisible(String elementName) {
        String locator = getLocatorByName(elementName);
        if (locator == null) {
            throw new IllegalArgumentException("Nie znaleziono elementu o nazwie: " + elementName);
        }

        SelenideElement element = getElement(elementName);

        try {
            element.shouldBe(Condition.visible);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
