Feature: testing create restaurant
  Background:
    Given Available restaurants in database to test get restaurant details
      | ID | Name | Address | Location | Status | Food | Cuisine | Owner.Username | Owner.Password |
      | 1  | r1   | Address | Location | online | Food | Cuisine | o1             | o1             |

  Scenario: Get exists restaurant details
    When I want to get restaurant details with restaurant ID 1
    Then restaurant details are returned for the specific ID 1 and its name is r1

  Scenario: Get not exists restaurant details
    When I want to get restaurant details with restaurant ID 10
    Then can not get restaurant details because restaurant not found
