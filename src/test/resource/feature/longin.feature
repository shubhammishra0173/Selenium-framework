Feature: login amazon

  Scenario Outline: User login the amazon web application
    Given user login into the amazon site <url> with <IBD> ,<userID>,<password>

    Examples:
    |url|IBD|userID|password|
    |   |   |      |        |