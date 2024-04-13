Feature: testing set restaurant status
  Background:
    Given Available restaurants in database to test set status
      | ID | Name | Address | Location | Status | Food | Cuisine | Owner.Username | Owner.Password |
      | 1  | r1   | Address | Location | online | Food | Cuisine | o1             | o1             |
      | 2  | r2   | Address | Location | online | Food | Cuisine | o2             | o2             |

  Scenario: Set exists restaurant status by its owner
    When User o1 with password o1 wants to set restaurant status to offline for restaurant with ID 1
    Then restaurant status set successfully

  Scenario: Set exists restaurant invalid status by its owner
    When User o1 with password o1 wants to set restaurant status to invalid for restaurant with ID 1
    Then restaurant status can not be set because the invalid status

  Scenario: Set exists restaurant status by user not own the restaurant
    When User o2 with password o2 wants to set restaurant status to offline for restaurant with ID 1
    Then restaurant status can not be set because the user does not own the restaurant

  Scenario: Set exists restaurant status by unauthorized user
    When User o4 with password o4 wants to set restaurant status to offline for restaurant with ID 1
    Then restaurant status can not be set because the user is unauthorized

  Scenario: Set not exists restaurant status
    When User o1 with password o1 wants to set restaurant status to offline for restaurant with ID 10
    Then restaurant status can not be set because the restaurant not found
