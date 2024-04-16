#Feature: Feature testing getting item details
#  Background:
#    Given Restaurants in database to test get item details
#      | ID | Name | Address | Location | Status | Food    | Cuisine | Owner.Username | Owner.Password |
#      | 1  | r1   | Address | Location | online | Seafood | Yemeni  | o1             | o1             |
#    Given Categories in database to test get item details
#      | ID  | name |
#      | 1   | c1   |
#    Given Menus in database to test get item details
#      | ID  | name |
#      | 1   | m1   |
#    Given Items in database to test get item details
#      | id | name | description | price | quantity |
#      | 1  | i1   | desc        | 230.0 | 3.0      |
#
#  Scenario: Get item detail by ID
#    When I query an item with ID 1 of menu with ID 1 of category with ID 1 restaurant with ID 1
#    Then item detail with ID 1 is returned and its name is i1 and price 230
#
#  Scenario: Get item detail by ID but restaurant not found
#    When I query an item with ID 1 of menu with ID 1 of category with ID 1 restaurant with ID 2
#    Then can not get item details because item not found or menu not found or restaurant not found
#
#  Scenario: Get item detail by ID but category not found
#    When I query an item with ID 1 of menu with ID 1 of category with ID 2 restaurant with ID 1
#    Then can not get item details because category not found
#
#  Scenario: Get menu detail by ID but menu not found
#    When I query an item with ID 1 of menu with ID 2 of category with ID 1 restaurant with ID 1
#    Then can not get item details because item not found or menu not found or restaurant not found
#
#  Scenario: Get menu detail by ID but item not found
#    When I query an item with ID 3 of menu with ID 1 of category with ID 1 restaurant with ID 1
#    Then can not get item details because item not found or menu not found or restaurant not found
