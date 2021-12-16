@Ratesapi
Feature: Ratesapi

  @TC1
  Scenario: Request for latest exchange rates returns a valid response

    When latest exchange rates are requested
    Then response status code is 200
    And response content type is "application/json"
    And response body contains information about base currency, exchange rates and date

  @TC2
  Scenario: Request for latest exchange rates for specific base currency returns a valid response

    When latest exchange rates for base currency PLN are requested
    Then response status code is 200
    And response content type is "application/json"
    And response body contains information about base currency, exchange rates and date
    And base currency is PLN

  @TC3
  Scenario: Request for latest exchange rates for specific currencies returns a valid response

    When latest exchange rates for currencies PLN,CZK,GBP are requested
    Then response status code is 200
    And response content type is "application/json"
    And response body contains information about base currency, exchange rates and date
    And exchange rates only for PLN,CZK,GBP currencies are returned

  @TC4
  Scenario: Request for latest exchange rates for specific currencies against specific base currency returns a valid response

    When latest exchange rates for currencies USD,ILS,CNY,RUB and base currency PLN are requested
    Then response status code is 200
    And response content type is "application/json"
    And response body contains information about base currency, exchange rates and date
    And exchange rates only for USD,ILS,CNY,RUB currencies are returned
    And base currency is PLN

  @TC5
  Scenario: Request for exchange rates for specific date returns a valid response

    When exchange rates for date 2004-06-14 are requested
    Then response status code is 200
    And response content type is "application/json"
    And response body contains information about base currency, exchange rates and date
    And date is 2004-06-14

  @TC6
  Scenario: Request for exchange rates for specific date and base currency returns a valid response

    When exchange rates for date 2004-06-14 and base currency USD are requested
    Then response status code is 200
    And response content type is "application/json"
    And response body contains information about base currency, exchange rates and date
    And date is 2004-06-14
    And base currency is USD

  @TC7
  Scenario: Request for exchange rates for specific date and specific currencies returns a valid response

    When exchange rates for date 2017-02-22 and currencies PLN,CNY,CZK,RUB,USD are requested
    Then response status code is 200
    And response content type is "application/json"
    And response body contains information about base currency, exchange rates and date
    And date is 2017-02-22
    And exchange rates only for PLN,CNY,CZK,RUB,USD currencies are returned

  @TC8
  Scenario: Request for exchange rates for specific date, specific currencies and base currency returns a valid response

    When exchange rates for date 2014-03-14, currencies PLN,RUB,CZK,CNY,EUR,GBP and base currency USD are requested
    Then response status code is 200
    And response content type is "application/json"
    And response body contains information about base currency, exchange rates and date
    And date is 2014-03-14
    And base currency is USD
    And exchange rates only for PLN,RUB,CZK,CNY,EUR,GBP currencies are returned

  @TC9
  Scenario: Exchange rate against the same currency should be 1

    Given USD as base currency
    When I get latest exchange rate for USD
    Then exchange rate for USD is 1.0

  @TC10
  Scenario Outline: Error is returned when incorrect URL is provided

    When request is sent to endpoint "<path>"
    Then response status code is <code>
    And response content type is "<contentType>"
    And error message is "<error>"

    Examples:
      | path                  | code | contentType               | error                                                     |
      |                       | 400  | application/json          | time data 'api' does not match format '%Y-%m-%d'          |
      | non_existing/endpoint | 404  | text/plain; charset=utf-8 | Error: Requested URL /api/non_existing/endpoint not found |

  @TC11
  Scenario: Latest exchange rate is provided when request for the future date exchange rate is sent

    When exchange rates for the next year are requested
    Then latest exchange rates are returned

  @TC12
  Scenario: Scenario failure demo - expected exchange rate for given currency is different than expected

    When latest exchange rates are requested
    Then exchange rate for PLN is 2.50

  @TC13
  Scenario: Requesting exchange rates for not supported base currency results in error

    When latest exchange rates for base currency ABC are requested
    Then response status code is 400
    And error message is "Base 'ABC' is not supported."
    When exchange rates for date 2020-01-01 and base currency XYZ are requested
    Then response status code is 400
    And error message is "Base 'XYZ' is not supported."

  @TC14
  Scenario: Requesting exchange rates for not supported currencies results in error

    When latest exchange rates for currencies USD,ABC,PLN are requested
    Then response status code is 400
    And error message contains "Symbols 'USD,ABC,PLN' are invalid"
    When exchange rates for date 2018-11-17 and currencies CNY,GBP,CZK,XYZ are requested
    Then response status code is 400
    And error message contains "Symbols 'CNY,GBP,CZK,XYZ' are invalid"
