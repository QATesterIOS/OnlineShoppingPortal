Feature: Place order using an new created user

  Scenario Outline: A new user buy items of different product categories
    Given A new user navigates to online shopping application
    When signup with <email> <name> <password> <phone> <address>
    And Add items of different categories into cart
    And proceed to checkout
    And place order
    Then order should be placed successfully
    Examples:
      | email          | name      | password | phone     | address
      | test@gmail.com | test user | abc@123  | 079607040 | Amman - Jordan
