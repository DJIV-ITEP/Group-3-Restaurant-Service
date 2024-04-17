Feature: Feature testing getting category details
  Background:
    Given Restaurants in database to test get category details
      | ID | Name | Address | Location | Status | Food    | Cuisine | Owner.Username | Owner.Password |
      | 1  | r1   | Address | Location | online | Seafood | Yemeni  | o1             | o1             |
    Given Categories in database to test get category details
      | ID  | name |
      | 1   | c1   |
      | 2   | c2   |
      | 3   | c3   |

  Scenario: Get category detail by ID
    When I query a category with ID 2 of restaurant with ID 1
    Then Category detail with ID 2 is returned and its name is c2

  Scenario: Get category detail by ID but restaurant not found
    When I query a category with ID 2 of restaurant with ID 2
    Then can not get category details because category not found or restaurant not found

  Scenario: Get category detail by ID but category not found
    When I query a category with ID 4 of restaurant with ID 1
    Then can not get category details because category not found or restaurant not found
