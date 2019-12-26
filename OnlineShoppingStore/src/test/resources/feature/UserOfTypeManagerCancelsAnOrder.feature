Feature: User of type manager cancels an order

  Scenario Outline: A user of type manager cancel a new order
    Given A user of type manager navigates to online shopping application
    When login as manager1
    And cancel a new order
    Then product order should be canceled successfully

    Examples:
