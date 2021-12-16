package io.ratesapi.demo

import io.restassured.response.Response
import spock.lang.Specification

import java.time.LocalDate

class ResponseConverterSpec extends Specification {

    ResponseConverter sut
    def response = Mock(Response)

    void setup() {
        sut = new ResponseConverter()
    }

    def "should convert response to ExchangeRateInfo"() {
        given:
        response.asString() >> EXCHANGE_RATE_INFO_JSON

        when:
        def actual = sut.convert(response, ExchangeRateInfo.class)

        then:
        actual.getDate() == LocalDate.parse('2020-08-07')
        actual.getBase() == Currency.getInstance('EUR')
        def actualRates = actual.getRates()
        actualRates.size() == 2
        actualRates.get(Currency.getInstance('GBP')) == new BigDecimal('0.90155')
        actualRates.get(Currency.getInstance('USD')) == new BigDecimal('1.24')
    }

    def "should throw exception if response body is not a valid json"() {
        given:
        response.asString() >> "Error: not a valid JSON"

        when:
        sut.convert(response, ErrorInfo.class)

        then:
        thrown(IllegalStateException)
    }

    static def EXCHANGE_RATE_INFO_JSON = '''{
  "base": "EUR",
  "rates": {
    "GBP": 0.90155,
    "USD": 1.24
  },
  "date": "2020-08-07"
}'''

}
