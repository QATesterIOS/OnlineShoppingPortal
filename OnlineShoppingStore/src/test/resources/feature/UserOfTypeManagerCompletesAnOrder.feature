Feature: User of type manager completes as order

  Scenario Outline: A user of type manager completes a new order
    Given A user of type manager navigates to online shopping application
    When login as manager1
    And completes a new order
    Then order should be completed successfully

    Examples:
