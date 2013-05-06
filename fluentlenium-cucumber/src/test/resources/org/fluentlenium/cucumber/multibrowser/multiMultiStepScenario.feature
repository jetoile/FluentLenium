Feature: multi browser test with steps into multi classes with one driver instance per scenario

  Scenario Outline: scenario 1
    Given multiscenario I use browser <browser> with <parameters>
    And multiscenario multi1 I am on the first page
    When multiscenario multi2 I click on next page
    Then multiscenario multi2 I am on the second page
  Examples:
    | browser   | parameters                                                            |
    | firefox   |                                                                       |
#    | phantomjs | phantomjs.binary.path@/opt/phantomjs-1.9.0-linux-x86_64/bin/phantomjs |
    #    | chrome    | webdriver.chrome.driver@/opt/chromedriver/chromedriver                |
#    | remote    | browser.name@firefox;webdriver.remote.url@http://10.147.2.83:4444/wd/hub;os.name@WIN7   |

  Scenario Outline: scenario 2
    Given multiscenario I use browser <browser> with <parameters>
    And multiscenario multi1 I am on the first page
    When multiscenario multi2 I click on next page
    Then multiscenario multi2 I am on the second page
  Examples:
    | browser   | parameters                                                            |
    | firefox   |                                                                       |
#    | phantomjs | phantomjs.binary.path@/opt/phantomjs-1.9.0-linux-x86_64/bin/phantomjs |