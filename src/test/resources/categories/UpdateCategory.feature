Feature: Feature testing update category
  Background:
    Given Restaurants in database to test update category
      | ID | Name | Address | Location | Status | Food    | Cuisine | Owner.Username | Owner.Password |
      | 1  | r1   | Address | Location | online | Seafood | Yemeni  | o1             | o1             |
      | 2  | r2   | Address | Location | online | Seafood | Yemeni  | o2             | o2             |
    Given Categories in database to test update category
      | ID  | name |
      | 1   | c1   |

  Scenario: Update category success
    When category with ID 1 is updated by o1 user with password o1 to restaurant with ID 1 with category details <categoryDetails>:
      | name     |
      | category |
    Then category updated successfully

  Scenario: Can not update category because restaurant not found
    When category with ID 1 is updated by o1 user with password o1 to restaurant with ID 3 with category details <categoryDetails>:
      | name     |
      | category |
    Then can not update category because restaurant not found

  Scenario: Can not update category because category not found
    When category with ID 2 is updated by o1 user with password o1 to restaurant with ID 1 with category details <categoryDetails>:
      | name     |
      | category |
    Then category can not be updated duo to missing values or category not belong to the restaurant

  Scenario: Can not update category because not authorized
    When category with ID 1 is updated by o2 user with password o3 to restaurant with ID 1 with category details <categoryDetails>:
      | name     |
      | category |
    Then not authorized to update category

  Scenario: Can not update category because name field is missing
    When category with ID 1 is updated by o1 user with password o1 to restaurant with ID 1 with category details <categoryDetails>:
      | aaaa     |
      | category |
    Then category can not be updated duo to missing values or category not belong to the restaurant
