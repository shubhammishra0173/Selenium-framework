Feature: login amazon

  Scenario Outline: User login the amazon web application
    Given user login into the amazon site <url> with <userID>,<password>



    Examples:
    |url|IBD|userID|password|
    |   |   |      |        |

    Scenario Outline: User signup the amazon to create new account
      Given A workbook named "Amazon_signup" and sheet name "signup" is read

      Examples:
      ||||||