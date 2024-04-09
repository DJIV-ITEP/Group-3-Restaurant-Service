Feature: Feature testing listing available restaurants
  Background:
    Given Available restaurants in database to test set status
      | ID | Name | Address | Location | Status | Food    | Cuisine | Owner.Username | Owner.Password |
      | 1  | r1   | Address | Location | online | Seafood | Yemeni  | o1             | o1             |
      | 2  | r2   | Address | Location | online | Seafood | Indian  | o2             | o2             |
      | 3  | r3   | Address | Location | online | Vegan   | Yemeni  | o3             | o3             |

  Scenario: List all available restaurants
    When I query available restaurants without filters
    Then List all the 3 available restaurants is returned

  Scenario: List all available restaurants with particular food and cuisine types
    When I query available restaurants with food type Seafood and cuisine type Yemeni
    Then List all the 1 available restaurants is given with food type Seafood and cuisine type Yemeni are returned

  Scenario: List all available restaurants with particular food type
    When I query available restaurants with food type Seafood
    Then List all the 2 available restaurants is given with food type Seafood are returned

  Scenario: List all available restaurants with particular cuisine type
    When I query available restaurants with cuisine type Yemeni
    Then List all the 2 available restaurants is given with cuisine type Yemeni are returned
