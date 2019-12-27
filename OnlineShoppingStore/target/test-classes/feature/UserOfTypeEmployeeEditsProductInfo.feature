Feature: User of type employee edits product info

  Scenario Outline: A user of type employee edits a product
    Given A user of type employee navigates to online shopping application
    When login as employee1
    And edits product info
    Then product information should be updated successfully

    Examples:
