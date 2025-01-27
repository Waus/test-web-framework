package pl.tmobile.recruitment.pages;

import java.util.HashMap;
import java.util.Map;

public class BasketPage  extends BasePage {

    public static final String BASKET_INITIAL_PRICE = "BASKET_INITIAL_PRICE"; //wartość pola 'Płatność na start'
    public static final String BASKET_MONTHLY_INSTALMENT = "BASKET_MONTHLY_INSTALMENT"; //wartość pola 'Miesięcznie'

    private final Map<String, String> elements;

    public BasketPage() {
        elements = new HashMap<>(baseElements);
        elements.put(BASKET_INITIAL_PRICE, "//span[@data-qa='BKT_TotalupFront']");
        elements.put(BASKET_MONTHLY_INSTALMENT, "//span[@data-qa='BKT_TotalMonthly']");
    }

    public String getLocatorByName(String name) {
        String locator = elements.get(name);
        if (locator == null) {
            throw new IllegalArgumentException("Nie znaleziono elementu o nazwie: " + name);
        }
        return locator;
    }
}
