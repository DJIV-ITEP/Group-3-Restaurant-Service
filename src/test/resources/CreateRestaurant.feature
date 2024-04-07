# TODO
#Feature: Feature testing create restaurant
#
##  Background: List available restaurants query gets its data from database
##    Given the available restaurants
##      |name|address|location|status|food|cuisine|
##      | n1 | a1 | l1 | online | Seafood | Yemeni |
##      | n2 | a2 | l2 | online | Seafood | Indian |
##      | n3 | a3 | l3 | online | Vegan | Yemeni |
#  Scenario: Create new restaurant the result is success
#    When admin add new restaurant
#    Given
#    Then List all available restaurants is returned
#
#  Scenario:
#    When I query available restaurants with food type Seafood and cuisine type Yemeni
#    Then List all available restaurants is given with food type Seafood and cuisine type Yemeni are returned
#
#  Scenario:
#    When I query available restaurants with food type Seafood
#    Then List all available restaurants is given with food type Seafood are returned
#
#  Scenario:
#    When I query available restaurants with cuisine type Yemeni
#    Then List all available restaurants is given with cuisine type Yemeni are returned