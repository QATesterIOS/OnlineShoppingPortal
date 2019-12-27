Feature: Place order using an existing user

  Scenario Outline: A registered user buy items of different product categories
    Given A registered user navigates to online shopping application
    When login as customer1
    And Add items of different categories into cart
    And proceed to checkout
    And place order
    Then order should be placed successfully
    Examples:
      |  |
