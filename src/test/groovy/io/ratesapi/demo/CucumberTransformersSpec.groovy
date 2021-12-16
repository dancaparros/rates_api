package io.ratesapi.demo

import spock.lang.Specification

import java.time.LocalDate

class CucumberTransformersSpec extends Specification {

    CucumberTransformers sut

    void setup() {
        sut = new CucumberTransformers()
    }

    def "should transform to LocalDate"() {
        given:
        def dateString = '2020-08-07'

        when:
        def actual = sut.transformDate(dateString)

        then:
        actual == LocalDate.parse(dateString)
    }

    def "should transform to currency symbols String list"() {
        when:
        def actual = sut.transformCurrencyListString('USD,EUR,GBP')

        then:
        actual as Set == ['USD', 'GBP', 'EUR'] as Set
    }

    def "should transform to currency list"() {
        when:
        def actual = sut.transformCurrencyList('USD,EUR,GBP')

        then:
        actual as Set == [Currency.getInstance('USD'), Currency.getInstance('EUR'), Currency.getInstance('GBP')] as Set
    }

    def "should transform currency"() {
        when:
        def actual = sut.transformCurrency('PLN')

        then:
        actual == Currency.getInstance('PLN')
    }

}
