#Feature: Feature testing listing categories
#  Background:
#    Given Restaurants in database to test list categories
#      | ID | Name | Address | Location | Status | Food    | Cuisine | Owner.Username | Owner.Password |
#      | 1  | r1   | Address | Location | online | Seafood | Yemeni  | o1             | o1             |
#    Given Categories in database to test list categories
#      | ID  | name |
#      | 1   | c1   |
#      | 2   | c2   |
#      | 3   | c3   |
#
#  Scenario: Lis all available restaurant categories
#    When I query a list of categories of restaurant with ID 1
#    Then List all the 3 available categories is returned
#
#  Scenario: Lis all available restaurant categories and the restaurant not found
#    When I query a list of categories of restaurant with ID 2
#    Then can not get categories because restaurant not found