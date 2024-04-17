Feature: Feature testing getting menu details
  Background:
    Given Restaurants in database to test get menu details
      | ID | Name | Address | Location | Status | Food    | Cuisine | Owner.Username | Owner.Password |
      | 1  | r1   | Address | Location | online | Seafood | Yemeni  | o1             | o1             |
    Given Categories in database to test get menu details
      | ID  | name |
      | 1   | c1   |
    Given Menus in database to test get menu details
      | ID  | name |
      | 1   | m1   |

  Scenario: Get menu detail by ID
    When I query a menu with ID 1 of category with ID 1 restaurant with ID 1
    Then Menu detail with ID 1 is returned and its name is m1

  Scenario: Get menu detail by ID but restaurant not found
    When I query a menu with ID 1 of category with ID 1 restaurant with ID 2
    Then can not get menu details because menu not found or restaurant not found

  Scenario: Get menu detail by ID but category not found
    When I query a menu with ID 1 of category with ID 2 restaurant with ID 1
    Then can not get menu details because category not found

  Scenario: Get menu detail by ID but menu not found
    When I query a menu with ID 3 of category with ID 1 restaurant with ID 1
    Then can not get menu details because menu not found or restaurant not found