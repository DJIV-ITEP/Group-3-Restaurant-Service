#Feature: Feature testing listing items
#  Background:
#    Given Restaurants in database to test list items
#      | ID | Name | Address | Location | Status | Food    | Cuisine | Owner.Username | Owner.Password |
#      | 1  | r1   | Address | Location | online | Seafood | Yemeni  | o1             | o1             |
#    Given Categories in database to test list items
#      | ID  | name |
#      | 1   | c1   |
#    Given Menus in database to test list items
#      | ID  | name |
#      | 1   | m1   |
#    Given Items in database to test list items
#      | id | name | description | price | quantity |
#      | 1  | i1   | desc        | 230.0 | 3.0      |
#
#
#  Scenario: Lis all items of menu
#    When I query a list of items of menu with ID 1 on category with ID 1 restaurant with ID 1
#    Then List all the 1 available items is returned
#
#  Scenario: Lis all menu items and the restaurant not found
#    When I query a list of items of menu with ID 1 on category with ID 1 restaurant with ID 2
#    Then can not get items because restaurant not found or menu not found
#
#  Scenario: Lis all menu items and the category not found
#    When I query a list of items of menu with ID 1 on category with ID 2 restaurant with ID 1
#    Then can not get items because category not found
#
#  Scenario: Lis all menu items and the menu not found
#    When I query a list of items of menu with ID 2 on category with ID 1 restaurant with ID 1
#    Then can not get items because restaurant not found or menu not found
