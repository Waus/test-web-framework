package pl.tmobile.recruitment.pages;

import java.util.HashMap;
import java.util.Map;

public class HomePage extends BasePage {

    public static final String MENU_ITEM_DEVICES = "MENU_ITEM_DEVICES";
    public static final String SUBMENU_ITEM_SMARTWATCHES = "SUBMENU_ITEM_SMARTWATCHES";
    public static final String SMARTWATCHES_ITEM_0 = "SMARTWATCHES_ITEM_0";
    public static final String ICON_BASKET_NUMBER_OF_PRODUCTS = "ICON_BASKET_NUMBER_OF_PRODUCTS";

    private final Map<String, String> elements;

    public HomePage() {
        elements = new HashMap<>(baseElements);
        elements.put(SMARTWATCHES_ITEM_0, "//div[@data-qa='LST_ProductCard0']");
        elements.put(MENU_ITEM_DEVICES, "//nav[@id='main-menu']//button[contains(text(),'Urządzenia')]");
        elements.put(SUBMENU_ITEM_SMARTWATCHES, "//ul[contains(@class, 'sub-menu') and contains(@class, 'level-2')]//span[contains(text(),'Smartwatche')]");
        elements.put(ICON_BASKET_NUMBER_OF_PRODUCTS, "(//a[@title='Koszyk']//div[contains(@class, 'bg-base-primary-600')])[last()]");  // w html istnieją 2 elementy z ikoną koszyka z identycznym html, z czego jeden niewidoczny i to on jest wybierany domyślnie. Nie udało się ładniejszego lokatora uzyskać.
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
