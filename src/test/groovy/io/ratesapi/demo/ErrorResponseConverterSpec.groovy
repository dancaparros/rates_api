package io.ratesapi.demo

import io.ratesapi.demo.ErrorInfo
import io.ratesapi.demo.ErrorResponseConverter
import io.ratesapi.demo.ResponseConverter
import io.restassured.response.Response
import spock.lang.Specification

class ErrorResponseConverterSpec extends Specification {

    ErrorResponseConverter sut
    def responseConverter = Mock(ResponseConverter)
    def response = Mock(Response)

    void setup() {
        sut = new ErrorResponseConverter(responseConverter)
    }

    def "should transform error response with application/json content type"() {
        given:
        response.statusCode() >> 400
        response.contentType() >> 'application/json'

        when:
        sut.convert(response)

        then:
        1 * responseConverter.convert(response, ErrorInfo.class)
    }

    def "should transform error response if content type is other than application/json"() {
        given:
        response.statusCode() >> 400
        response.contentType() >> 'text/plain'
        def error = 'Some test error'

        when:
        def actual = sut.convert(response)

        then:
        1 * response.asString() >> 'Some test error'
        actual.getError() == error
    }

    def "should throw exception if response status is other than 4xx"() {
        given:
        response.statusCode() >> status

        when:
        sut.convert(response)

        then:
        def actual = thrown(IllegalArgumentException)
        actual.getMessage() == "Response with status code ${status} is not an 4xx error response and cannot be converted to ErrorInfo."

        where:
        status | _
        399    | _
        500    | _
    }

}
