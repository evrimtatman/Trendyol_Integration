Feature: Trendyol Case Login Test

  Scenario: User can login to system with correct credentials
    Given User navigate to home page
    Then  User Click to login button in the home page
    Then  User enters username "*********" and password "*********" to inputs
    When User press the login button
    Then User logged in successfully

  Scenario: User click all boutiques and check images
    When user click "KADIN" section
    Then user goto url contains "/butik/liste/kadin" successfully
    And check broken images on boutique
    When user click "ERKEK" section
    Then user goto url contains "/butik/liste/erkek" successfully
    And check broken images on boutique
    When user click "ÇOCUK" section
    Then user goto url contains "/butik/liste/cocuk" successfully
    And check broken images on boutique
    When user click "EV & YAŞAM" section
    Then user goto url contains "/butik/liste/ev--yasam" successfully
    And check broken images on boutique
    When user click "SÜPERMARKET" section
    Then user goto url contains "/butik/liste/supermarket" successfully
    And check broken images on boutique
    When user click "KOZMETİK" section
    Then user goto url contains "/butik/liste/kozmetik" successfully
    And check broken images on boutique
    When user click "AYAKKABI & ÇANTA" section
    Then user goto url contains "/butik/liste/ayakkabi--canta" successfully
    And check broken images on boutique
    When user click "SAAT & AKSESUAR" section
    Then user goto url contains "/butik/liste/saat--aksesuar" successfully
    And check broken images on boutique
    When user click "ELEKTRONİK" section
    Then user goto url contains "/butik/liste/elektronik" successfully
    And check broken images on boutique

  Scenario: User select a product and add to basket
    Given User click to product boutique
    Then  User click a product
    When  Add product to basket
    Then  Product added to basket successfully


