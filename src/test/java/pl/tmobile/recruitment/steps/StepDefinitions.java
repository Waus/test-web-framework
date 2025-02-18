package pl.tmobile.recruitment.steps;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.tmobile.recruitment.pages.BasePage;
import pl.tmobile.recruitment.pages.BasketPage;
import pl.tmobile.recruitment.pages.HomePage;
import pl.tmobile.recruitment.pages.SmartwatchItemPage;
import pl.tmobile.recruitment.utils.Buffer;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.visible;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.codeborne.selenide.Selenide.*;

public class StepDefinitions {

    private static final Logger logger = LogManager.getLogger(StepDefinitions.class);
    private final Map<String, BasePage> pages = new HashMap<>();

    public StepDefinitions() {
        pages.put("BasePage", new BasePage());
        pages.put("HomePage", new HomePage());
        pages.put("SmartwatchItemPage", new SmartwatchItemPage());
        pages.put("BasketPage", new BasketPage());
    }

    @When("Otwieram przeglądarkę")
    public void openBrowser() {
        logger.info("Otwieram przeglądarkę...");
        open();
    }

    @When("Przechodzę na stronę główną T-Mobile")
    public void goToHomePage() {
        logger.info("Przechodzę na stronę T-Mobile...");
        open("https://www.t-mobile.pl");
        logger.info("Strona główna T-Mobile została załadowana.");
    }

    @When("Maksymalizuję okno przeglądarki")
    public void maximizeBrowserWindow() {
        logger.info("Maksymalizacja okna przeglądarki");
        webdriver().driver().getWebDriver().manage().window().maximize();
    }

    @When("Na stronie {string} klikam element {string}")
    public void clickElementOnPage(String pageName, String elementName) {
        BasePage page = pages.get(pageName);
        if (page == null) {
            throw new IllegalArgumentException("Nie znaleziono strony: " + pageName);
        }

        SelenideElement element = page.getElement(elementName);
        logger.info("Klikam element '{}' na stronie '{}'", elementName, pageName);

        try {
            element.shouldBe(visible, enabled);
            element.scrollIntoView("{behavior: 'smooth', block: 'center'}");
            element.click();
            logger.info("Kliknięto element: '{}'", elementName);
        } catch (Exception e) {
            logger.warn("Standardowe kliknięcie nie działa, próbuję JavaScript...");
            executeJavaScript("arguments[0].click();", element);
        }
    }

    @When("Na stronie {string} klikam element {string} jeżeli jest widoczny")
    public void clickElementOnPageIfVisible(String pageName, String elementName) {
        BasePage page = pages.get(pageName);
        if (page == null) {
            throw new IllegalArgumentException("Nie znaleziono strony: " + pageName);
        }

        SelenideElement element = page.getElement(elementName);
        logger.info("Sprawdzam, czy element '{}' na stronie '{}' jest dostępny...", elementName, pageName);

        boolean isElementPresent;

        try {
            element.should(Condition.exist, Duration.ofSeconds(5));
            isElementPresent = true;
        } catch (Exception e) {
            isElementPresent = false;
        }

        if (isElementPresent) {
            logger.info("Element '{}' istnieje, sprawdzam widoczność...", elementName);

            try {
                element.shouldBe(Condition.visible, Duration.ofSeconds(2));
                logger.info("Element '{}' jest widoczny, wykonuję kliknięcie...", elementName);
                clickElementOnPage(pageName, elementName);
            } catch (Exception e) {
                logger.warn("Element '{}' nie jest widoczny, pomijam kliknięcie.", elementName);
            }
        } else {
            logger.warn("Element '{}' nie pojawił się na stronie, pomijam kliknięcie.", elementName);
        }
    }

    @When("Czekam {int} sekund")
    public void waitForSeconds(int seconds) {
        try {
            logger.info("Czekam {} sekund...", seconds);
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Błąd podczas oczekiwania!", e);
        }
    }

    @When("Na stronie {string} najeżdżam myszką na element {string}")
    public void hoverOverElementOnPage(String pageName, String elementName) {
        BasePage page = pages.get(pageName);
        if (page == null) {
            throw new IllegalArgumentException("Nie znaleziono strony: " + pageName);
        }
        page.hoverOverElement(elementName);
    }

    @When("Na stronie {string} zapisuję do buffora wartość pola {string}")
    public void saveFieldValueToBuffer(String pageName, String fieldName) {
        String camelCaseKey = convertToCamelCase(fieldName);
        BasePage page = pages.get(pageName);

        if (page == null) {
            throw new IllegalArgumentException("Nie znaleziono strony: " + pageName);
        }

        String selector = page.getLocatorByName(fieldName);
        if (selector == null) {
            throw new IllegalArgumentException("Nie znaleziono pola: " + fieldName);
        }

        SelenideElement element = selector.startsWith("//") || selector.startsWith("(") ? $x(selector) : $(selector);
        String fieldValue = executeJavaScript("return arguments[0].innerText.trim();", element);

        if (fieldValue.isEmpty()) {
            throw new RuntimeException("Pole '" + fieldName + "' jest puste! Selector: " + selector);
        }

        Buffer.set(camelCaseKey, fieldValue);
        logger.info("Buffer: Zapisano [{}] = {}", camelCaseKey, fieldValue);
    }

    public static String convertToCamelCase(String input) {
        String[] words = input.toLowerCase().split("_");
        StringBuilder camelCaseString = new StringBuilder(words[0]);

        for (int i = 1; i < words.length; i++) {
            camelCaseString.append(Character.toUpperCase(words[i].charAt(0)))
                    .append(words[i].substring(1));
        }
        return camelCaseString.toString();
    }

    @Then("Porównuję wartości zapisane w buforze {string} i {string}")
    public void compareStoredValues(String firstKey, String secondKey) {
        String firstValue = Buffer.getString(firstKey);
        String secondValue = Buffer.getString(secondKey);

        logger.info("Porównanie wartości: [{}] = {} | [{}] = {}", firstKey, firstValue, secondKey, secondValue);
        assertEquals(firstValue, secondValue, "Wartości nie są zgodne!");
        logger.info("Wartości są zgodne.");
    }

    @When("Poprawiam wartość w buforze dla klucza {string}")
    public void updateBufferValueWithCurrency(String key) {
        String value = Buffer.getString(key);

        if (value == null) {
            logger.warn("⚠Klucz [{}] nie istnieje w buforze!", key);
            return;
        }

        String updatedValue = value + " zł";
        Buffer.set(key, updatedValue);

        logger.info("Zaktualizowano klucz [{}] na wartość: {}", key, updatedValue);
    }

    @Then("Sprawdzam czy liczba produktów zapisana w buforze dla klucza {string} jest równa 1")
    public void compareStoredValues(String key) {
        int productCount = Integer.parseInt(Buffer.getString(key));

        logger.info("W koszyku znajduje się następująca liczba produktów: {}", productCount);

        assertEquals(1, productCount, "Liczba produktów w koszyku nie jest równa 1");

        logger.info("Liczba produktów w koszyku jest równa 1.");
    }


}
