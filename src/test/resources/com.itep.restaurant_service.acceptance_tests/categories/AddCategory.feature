#Feature: Feature testing add category
#  Background:
#    Given Restaurants in database to test add category
#      | ID | Name | Address | Location | Status | Food    | Cuisine | Owner.Username | Owner.Password |
#      | 1  | r1   | Address | Location | online | Seafood | Yemeni  | o1             | o1             |
#      | 2  | r2   | Address | Location | online | Seafood | Yemeni  | o2             | o2             |
#
#  Scenario: Add category success
#    When new category is added by o1 user with password o1 to restaurant with ID 1 with category details <categoryDetails>:
#      | name |
#      | c1   |
#    Then category added successfully
#
#  Scenario: Can not add category because restaurant not found
#    When new category is added by o1 user with password o1 to restaurant with ID 3 with category details <categoryDetails>:
#      | name |
#      | c1   |
#    Then can not add category because restaurant not found
#
#  Scenario: Can not add category because not authorized
#    When new category is added by o1 user with password o1 to restaurant with ID 2 with category details <categoryDetails>:
#      | name |
#      | c1   |
#    Then not authorized to add category
#
#  Scenario: Can not add category because name field is missing
#    When new category is added by o1 user with password o1 to restaurant with ID 1 with category details <categoryDetails>:
#      | aa |
#      | c1   |
#    Then category can not be created duo to missing values
