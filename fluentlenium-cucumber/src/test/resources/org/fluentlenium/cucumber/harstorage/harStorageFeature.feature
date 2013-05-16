Feature: harstorage test with only one driver instance per feature

  Scenario Outline: scenario 1
    Given harstorage I use browser <browser> with <parameters>
    When I go on harPage
    Then I am on harPage
  Examples:
    | browser | parameters                                                                                                                                                                                          |
        | firefox | harstorage.api.proxy.port@9090;harstorage.recording.name@fluent;harstorage.port@5000;harstorage.host@127.0.0.1 |
#    | remote  | browser.name@firefox;os.name@linux;webdriver.remote.url@http://127.0.0.1:4444/wd/hub;harstorage.api.proxy.port@9090;harstorage.recording.name@fluent;harstorage.port@5000;harstorage.host@127.0.0.1 |

  Scenario Outline: scenario 2
    Given harstorage I use browser <browser> with <parameters>
    When I go on harPage
    Then I am on harPage
  Examples:
    | browser | parameters                                                                                                                                                                                          |
        | firefox | harstorage.api.proxy.port@9090;harstorage.recording.name@fluent;harstorage.port@5000;harstorage.host@127.0.0.1 |
#    | remote  | browser.name@firefox;os.name@linux;webdriver.remote.url@http://127.0.0.1:4444/wd/hub;harstorage.api.proxy.port@9090;harstorage.recording.name@fluent;harstorage.port@5000;harstorage.host@127.0.0.1 |

