Feature: testing create restaurant

  Scenario: Create new restaurant the result is success
    When new restaurant is added by admin user with password admin with restaurant details <restaurantDetails>:
      |name|address|location|status  |food     |cuisine |owner.username|owner.password|
      | n1 | a1    | l1     | online | Seafood | Yemeni | o1           | o1           |
    Then restaurant created successfully

  Scenario: Create new restaurant the result is failed because missing fields
    When new restaurant is added by admin user with password admin with restaurant details <restaurantDetails>:
      |name|address|location|status  |food     |owner.username|owner.password|
      | n1 | a1    | l1     | online | Seafood | o2           | o2           |
    Then restaurant can not be created duo to missing values

  Scenario: Create new restaurant by non admin user the result is failed
    When new restaurant is added by user user with password user with restaurant details <restaurantDetails>:
      |name|address|location|status  |food     |cuisine |owner.username|owner.password|
      | n1 | a1    | l1     | online | Seafood | Yemeni | o3           | o3           |
    Then not authorized to add restaurant
