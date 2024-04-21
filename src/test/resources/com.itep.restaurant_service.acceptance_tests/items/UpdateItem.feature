#Feature: Feature testing update item
#  Background:
#    Given Restaurants in database to test update item
#      | ID | Name | Address | Location | Status | Food    | Cuisine | Owner.Username | Owner.Password |
#      | 1  | r1   | Address | Location | online | Seafood | Yemeni  | o1             | o1             |
#      | 2  | r2   | Address | Location | online | Seafood | Yemeni  | o2             | o2             |
#    Given Categories in database to test update item
#      | ID  | name |
#      | 1   | c1   |
#    Given Menus in database to test update item
#      | ID  | name |
#      | 1   | m1   |
#    Given Items in database to test update item
#      | id | name | description | price | quantity |
#      | 1  | i1   | desc        | 230.0 | 3.0      |
#
#  Scenario: Update item success
#    When  item with ID 1 in menu with ID 1 in category with ID 1 is updated by o1 user with password o1 to restaurant with ID 1 with item details <itemDetails>:
#      | name | description | price | quantity |
#      | i1   | desc        | 400.0 | 3.0      |
#    Then item updated successfully
#
#  Scenario: Can not update item because restaurant not found
#    When  item with ID 1 in menu with ID 1 in category with ID 1 is updated by o1 user with password o1 to restaurant with ID 3 with item details <itemDetails>:
#      | name | description | price | quantity |
#      | i1   | desc        | 400.0 | 3.0      |
#    Then can not update item because restaurant not found or menu not found or item not found
#
#  Scenario: Can not update item because category not found
#    When  item with ID 1 in menu with ID 1 in category with ID 2 is updated by o1 user with password o1 to restaurant with ID 1 with item details <itemDetails>:
#      | name | description | price | quantity |
#      | i1   | desc        | 400.0 | 3.0      |
#    Then item can not be updated duo to missing values or category not belong to the restaurant
#
#  Scenario: Can not update item because menu not found
#    When  item with ID 1 in menu with ID 2 in category with ID 1 is updated by o1 user with password o1 to restaurant with ID 1 with item details <itemDetails>:
#      | name | description | price | quantity |
#      | i1   | desc        | 400.0 | 3.0      |
#    Then can not update item because restaurant not found or menu not found or item not found
#
#  Scenario: Can not update item because not authorized
#    When  item with ID 1 in menu with ID 1 in category with ID 1 is updated by o2 user with password o2 to restaurant with ID 1 with item details <itemDetails>:
#      | name | description | price | quantity |
#      | i1   | desc        | 400.0 | 3.0      |
#    Then not authorized to update item
#
#  Scenario: Can not update item because name field is missing
#    When  item with ID 1 in menu with ID 1 in category with ID 1 is updated by o1 user with password o1 to restaurant with ID 1 with item details <itemDetails>:
#      | aa   | description | price | quantity |
#      | i1   | desc        | 400.0 | 3.0      |
#    Then item can not be updated duo to missing values or category not belong to the restaurant
