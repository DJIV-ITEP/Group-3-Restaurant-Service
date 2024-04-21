#Feature: Feature testing update menu
#  Background:
#    Given Restaurants in database to test  update menu
#      | ID | Name | Address | Location | Status | Food    | Cuisine | Owner.Username | Owner.Password |
#      | 1  | r1   | Address | Location | online | Seafood | Yemeni  | o1             | o1             |
#    Given Categories in database to test  update menu
#      | ID  | name |
#      | 1   | c1   |
#    Given Menus in database to test update menu
#      | ID  | name |
#      | 1   | m1   |
#
#  Scenario: Update menu success
#    When menu with ID 1 in category with ID 1 is updated by o1 user with password o1 to restaurant with ID 1 with menu details <menuDetails>:
#      | name |
#      | menu |
#    Then menu updated successfully
#  Scenario: Can not update menu because restaurant not found
#    When menu with ID 1 in category with ID 1 is updated by o1 user with password o1 to restaurant with ID 3 with menu details <menuDetails>:
#      | name |
#      | menu |
#    Then can not update menu because restaurant not found or menu not found
#  Scenario: Can not update menu because category not found
#    When menu with ID 1 in category with ID 2 is updated by o1 user with password o1 to restaurant with ID 1 with menu details <menuDetails>:
#      | name |
#      | menu |
#    Then menu can not be updated duo to missing values or category not belong to the restaurant
#  Scenario: Can not update menu because menu not found
#    When menu with ID 2 in category with ID 1 is updated by o1 user with password o1 to restaurant with ID 1 with menu details <menuDetails>:
#      | name |
#      | menu |
#    Then can not update menu because restaurant not found or menu not found
##  Scenario: Can not update menu because name field is missing
##    When menu with ID 1 in category with ID 1 is updated by o1 user with password o1 to restaurant with ID 1 with menu details <menuDetails>:
##      | aa    |
##      | menu  |
##    Then menu can not be updated duo to missing values or category not belong to the restaurant
#  Scenario: Can not update menu because not authorized
#    When menu with ID 1 in category with ID 1 is updated by o2 user with password o2 to restaurant with ID 1 with menu details <menuDetails>:
#      | name |
#      | menu |
#    Then not authorized to update menu
