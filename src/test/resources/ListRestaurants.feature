#Feature: Feature testing listing available restaurants
#
#  Scenario: List all available restaurants
#    When I query available restaurants without filters
#    Then List all available restaurants is returned
#
#  Scenario: List all available restaurants with particular food and cuisine types
#    When I query available restaurants with food type Seafood and cuisine type Yemeni
#    Then List all available restaurants is given with food type Seafood and cuisine type Yemeni are returned
#
#  Scenario: List all available restaurants with particular cuisine type
#    When I query available restaurants with food type Seafood
#    Then List all available restaurants is given with food type Seafood are returned
#
#  Scenario: List all available restaurants with particular cuisine type
#    When I query available restaurants with cuisine type Yemeni
#    Then List all available restaurants is given with cuisine type Yemeni are returned