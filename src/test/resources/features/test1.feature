Feature: Zadanie rekrutacyjne 1

  Scenario: Zadanie rekrutacyjne 1
    When Otwieram przeglądarkę
    When Przechodzę na stronę główną T-Mobile
    When Maksymalizuję okno przeglądarki
    When Na stronie "HomePage" klikam element "ACCEPT_ALL_COOKIES"
    When Na stronie "HomePage" najeżdżam myszką na element "MENU_ITEM_DEVICES"
    When Na stronie "HomePage" klikam element "SUBMENU_ITEM_SMARTWATCHES"
    When Na stronie "HomePage" klikam element "SMARTWATCHES_ITEM_0"
    When Na stronie "SmartwatchItemPage" klikam element "BUTTON_WITHOUT_SUBSCRIPTION" jeżeli jest widoczny
#  Czekamy 5s żeby cena się zaktualizowała po kliknięciu 'Bez abonamentu'
    When Czekam 5 sekund
    When Na stronie "SmartwatchItemPage" zapisuję do buffora wartość pola "INITIAL_PRICE"
    When Na stronie "SmartwatchItemPage" zapisuję do buffora wartość pola "MONTHLY_INSTALMENT"
    When Na stronie "SmartwatchItemPage" klikam element "BUTTON_ADD_TO_CART"
    When Na stronie "BasketPage" zapisuję do buffora wartość pola "BASKET_INITIAL_PRICE"
    When Na stronie "BasketPage" zapisuję do buffora wartość pola "BASKET_MONTHLY_INSTALMENT"
    When Poprawiam wartość w buforze dla klucza "basketInitialPrice"
    When Poprawiam wartość w buforze dla klucza "basketMonthlyInstalment"
    Then Porównuję wartości zapisane w buforze "initialPrice" i "basketInitialPrice"
    Then Porównuję wartości zapisane w buforze "monthlyInstalment" i "basketMonthlyInstalment"
    When Przechodzę na stronę główną T-Mobile
    #  Czekamy 5s żeby liczba produktów w koszyku się zaktualizowała
    When Czekam 5 sekund
    When Na stronie "HomePage" zapisuję do buffora wartość pola "ICON_BASKET_NUMBER_OF_PRODUCTS"
    Then Sprawdzam czy liczba produktów zapisana w buforze dla klucza "iconBasketNumberOfProducts" jest równa 1