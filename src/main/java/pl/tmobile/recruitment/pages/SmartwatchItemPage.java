package pl.tmobile.recruitment.pages;

import java.util.HashMap;
import java.util.Map;

public class SmartwatchItemPage extends BasePage {

    public static final String BUTTON_ADD_TO_CART = "BUTTON_ADD_TO_CART";
    public static final String INITIAL_PRICE = "INITIAL_PRICE"; //wartość pola 'Do zapłaty na start'
    public static final String MONTHLY_INSTALMENT = "MONTHLY_INSTALMENT"; //wartość pola 'Do zapłaty miesięcznie'
    public static final String BUTTON_WITHOUT_SUBSCRIPTION = "BUTTON_WITHOUT_SUBSCRIPTION";

    private final Map<String, String> elements;

    public SmartwatchItemPage() {
        elements = new HashMap<>(baseElements);
        elements.put(BUTTON_ADD_TO_CART, "//*[@id=\"osAppInnerContainer\"]/main/section/section/div/span/div/div[2]/div/div/div/div[2]/section[1]/button/span/div"); // w html istnieją 2 elementy "Dodaj do koszyka" z identycznym html, z czego jeden niewidoczny i to on jest wybierany domyślnie. Nie udało się ładniejszego lokatora uzyskać.
        elements.put(INITIAL_PRICE, "//div[.//div[contains(text(), 'Do zapłaty na start')]]/following-sibling::div//div[@class='dt_price_change']//div");
        elements.put(MONTHLY_INSTALMENT, "//div[.//div[contains(text(), 'Do zapłaty miesięcznie')]]/following-sibling::div//div[@class='dt_price_change']//div");
        elements.put(BUTTON_WITHOUT_SUBSCRIPTION, "//div[@class='sc-feUZmu dOnJrC dt_typography variant_smallBold' and @data-qa='removeDefaultTariff' and text()='Bez abonamentu']");
    }

    @Override
    public String getLocatorByName(String name) {
        String locator = elements.get(name);
        if (locator == null) {
            throw new IllegalArgumentException("Nie znaleziono elementu o nazwie: " + name);
        }
        return locator;
    }
}
