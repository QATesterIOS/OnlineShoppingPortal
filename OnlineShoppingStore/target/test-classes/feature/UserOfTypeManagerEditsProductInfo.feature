Feature: User of type manager edits product info

  Scenario Outline: A user of type manager edits a product
    Given A user of type manager navigates to online shopping application
    When login as manager1
    And edits product info
    Then product information should be updated successfully

    Examples:
