#Feature: Feature testing add item
#
#  Background:
#    Given Restaurants in database to test add item
#      | ID | Name | Address | Location | Status | Food    | Cuisine | Owner.Username | Owner.Password |
#      | 1  | r1   | Address | Location | online | Seafood | Yemeni  | o1             | o1             |
#
#    Given Categories in database to test add item
#      | ID  | name |
#      | 1   | c1   |
#
#    Given Menus in database to test add item
#      | ID  | name |
#      | 1   | m1   |
#
#  Scenario: Add item success
#    When new item is added by o1 user with password o1 to restaurant with ID 1 and in category with ID 1 in menu with ID 1 with item details <itemDetails>:
#      | name | description | price | quantity |
#      | i1   | desc        | 230.0 | 3.0      |
#
#    Then item added successfully
#
#  Scenario: Can not add item because restaurant not found
#    When new item is added by o1 user with password o1 to restaurant with ID 10 and in category with ID 1 in menu with ID 1 with item details <itemDetails>:
#      | name | description | price | quantity |
#      | i1   | desc        | 230.0 | 3.0      |
#
#    Then can not add item because restaurant not found
#
#  Scenario: Can not add item because not authorized
#    When new item is added by o2 user with password o2 to restaurant with ID 1 and in category with ID 1 in menu with ID 1 with item details <itemDetails>:
#      | name | description | price | quantity |
#      | i1   | desc        | 230.0 | 3.0      |
#    Then not authorized to add item
#
#  Scenario: Can not add item because name field is missing
#    When new item is added by o1 user with password o1 to restaurant with ID 1 and in category with ID 1 in menu with ID 1 with item details <itemDetails>:
#      | aa   | description | price | quantity |
#      | i1   | desc        | 230.0 | 3.0      |
#    Then item can not be created duo to missing values
