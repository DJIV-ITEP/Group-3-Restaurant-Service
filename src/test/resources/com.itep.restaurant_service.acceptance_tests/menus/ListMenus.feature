#Feature: Feature testing listing menus
#  Background:
#    Given Restaurants in database to test list menus
#      | ID | Name | Address | Location | Status | Food    | Cuisine | Owner.Username | Owner.Password |
#      | 1  | r1   | Address | Location | online | Seafood | Yemeni  | o1             | o1             |
#    Given Categories in database to test list menus
#      | ID  | name |
#      | 1   | c1   |
#    Given Menus in database to test list menus
#      | ID  | name |
#      | 1   | m1   |
#
#
#  Scenario: Lis all menus of category
#    When I query a list of menus of category with ID 1 restaurant with ID 1
#    Then List all the 1 available menus is returned
#
#  Scenario: Lis all category menus and the restaurant not found
#    When I query a list of menus of category with ID 1 restaurant with ID 2
#    Then can not get menus because restaurant not found
#
#  Scenario: Lis all category menus and the category not found
#    When I query a list of menus of category with ID 2 restaurant with ID 1
#    Then can not get menus because category not found
