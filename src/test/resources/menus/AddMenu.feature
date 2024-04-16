Feature: Feature testing add menu
  Background:
    Given Restaurants in database to test add menu
      | ID | Name | Address | Location | Status | Food    | Cuisine | Owner.Username | Owner.Password |
      | 1  | r1   | Address | Location | online | Seafood | Yemeni  | o1             | o1             |
    Given Categories in database to test add menu
      | ID  | name |
      | 1   | c1   |

  Scenario: Add menu success
    When new menu is added by o1 user with password o1 to restaurant with ID 1 and in category with ID 1 with menu details <menuDetails>:
      | name |
      | m1   |
    Then menu added successfully

  Scenario: Can not add menu because restaurant not found
    When new menu is added by o1 user with password o1 to restaurant with ID 10 and in category with ID 1 with menu details <menuDetails>:
      | name |
      | m1   |
    Then can not add menu because restaurant not found

  Scenario: Can not add menu because not authorized
    When new menu is added by o2 user with password o2 to restaurant with ID 1 and in category with ID 1 with menu details <menuDetails>:
      | name |
      | m1   |
    Then not authorized to add menu

  Scenario: Can not add menu because name field is missing
    When new menu is added by o1 user with password o1 to restaurant with ID 1 and in category with ID 1 with menu details <menuDetails>:
      | aa |
      | m1   |
    Then menu can not be created duo to missing values
