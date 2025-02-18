package pl.tmobile.recruitment.pages;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.interactions.Actions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
            if (locator == null) {
                throw new IllegalArgumentException("Nie znaleziono elementu o nazwie: " + name);
            }

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

        public void clickElement(String name) {
            SelenideElement element = getElement(name);

            logger.info("Szukam elementu: " + name);

            // Debugowanie – sprawdzamy, czy element istnieje
            if (!element.exists()) {
                logger.info("Element NIE istnieje!");
            } else {
                logger.info("Element istnieje.");
            }

            // Debugowanie – sprawdzamy, czy element jest widoczny
            if (!element.isDisplayed()) {
                logger.info("Element NIE jest widoczny!");
            } else {
                logger.info("Element jest widoczny.");
            }

            // Próbujemy kliknąć element
            try {
                element.shouldBe(visible, enabled).click();
                logger.info("Kliknięto element: " + name);
            } catch (Exception e) {
                logger.info("Nie udało się kliknąć elementu normalnie, próbuję JavaScript...");
                executeJavaScript("arguments[0].click();", element);
            }
        }


        public void hoverOverElement(String name) {
            SelenideElement element = getElement(name);

            logger.info("Próbuję najechać na: " + name);
            logger.info("XPath: " + element);

            // Sprawdzamy, czy element istnieje
            if (!element.exists()) {
                throw new IllegalStateException("Element NIE istnieje w DOM!");
            } else {
                logger.info("Element istnieje w DOM.");
            }

            // Sprawdzamy, czy element jest widoczny
            if (!element.isDisplayed()) {
                throw new IllegalStateException("Element NIE jest widoczny!");
            } else {
                logger.info("Element jest widoczny.");
            }

            // Sprawdzamy style
            logger.info("Wartość display: " + element.getCssValue("display"));
            logger.info("Wartość opacity: " + element.getCssValue("opacity"));

            // Najeżdżamy myszką
            Actions actions = new Actions(webdriver().driver().getWebDriver());
            actions.moveToElement(element).perform();
            logger.info("Najechano na element: " + name);
        }

    }
