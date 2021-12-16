package io.ratesapi.demo;

import org.assertj.core.api.AutoCloseableSoftAssertions;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Currency;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RequiredArgsConstructor
public class RatesApiSteps {

    private final RatesApi ratesApi;
    private final TestContext testContext;
    private final ResponseConverter responseConverter;
    private final ErrorResponseConverter errorResponseConverter;

    @Given("{currency} as base currency")
    public void givenBaseCurrency(Currency base) {
        testContext.setBase(base);
    }

    @When("request is sent to endpoint {string}")
    public void incorrectPathIsProvided(String path) {
        Response response = ratesApi.sendGetRequest(path);
        testContext.setResponse(response);
    }

    @When("I get latest exchange rate for {currency}")
    public void getLatestExchangeRate(Currency currency) {
        Response response = ratesApi.getLatestExchangeRates(Objects.requireNonNull(testContext.getBase()).getSymbol(),
            List.of(currency.getSymbol()));
        testContext.setResponse(response);
    }

    @When("latest exchange rates are requested")
    public void requestLatestExchangeRates() {
        Response response = ratesApi.getLatestExchangeRates();
        testContext.setResponse(response);
    }

    @When("latest exchange rates for base currency {word} are requested")
    public void requestLatestExchangeRates(String base) {
        Response response = ratesApi.getLatestExchangeRates(base);
        testContext.setResponse(response);
    }

    @When("latest exchange rates for currenc(y|ies) {currencyListString} are requested")
    public void requestLatestExchangeRates(Collection<String> symbols) {
        Response response = ratesApi.getLatestExchangeRates(symbols);
        testContext.setResponse(response);
    }

    @When("latest exchange rates for currenc(y|ies) {currencyListString} and base currency {word} are requested")
    public void requestLatestExchangeRates(Collection<String> symbols, String base) {
        Response response = ratesApi.getLatestExchangeRates(base, symbols);
        testContext.setResponse(response);
    }

    @When("^exchange rates for date (\\d{4}-\\d{2}-\\d{2}) are requested$")
    public void requestExchangeRates(String dateString) {
        Response response = ratesApi.getExchangeRates(dateString);
        testContext.setResponse(response);
    }

    @When("^exchange rates for date (\\d{4}-\\d{2}-\\d{2}) and base currency (\\w+) are requested$")
    public void requestExchangeRates(String dateString, String base) {
        Response response = ratesApi.getExchangeRates(dateString, base);
        testContext.setResponse(response);
    }

    @When("exchange rates for date {word} and currencies {currencyListString} are requested")
    public void requestExchangeRates(String dateString, Collection<String> symbols) {
        Response response = ratesApi.getExchangeRates(dateString, symbols);
        testContext.setResponse(response);
    }

    @When("exchange rates for date {word}, currenc(y|ies) {currencyListString} and base currency {word} are requested")
    public void requestExchangeRates(String dateString, Collection<String> symbols, String base) {
        Response response = ratesApi.getExchangeRates(dateString, base, symbols);
        testContext.setResponse(response);
    }

    /**
     * Sends request for the exchange rates for the date equal to one year from current date.
     */
    @When("exchange rates for the next year are requested")
    public void getExchangeRatesForNextYear() {
        LocalDate date = LocalDate.now().plusYears(1);
        Response response = ratesApi.getExchangeRates(date.format(DateTimeFormatter.ISO_LOCAL_DATE));
        testContext.setResponse(response);
    }

    @Then("response body contains information about base currency, exchange rates and date")
    public void verifyResponseBodyContainsRequiredFields() {
        Response response = testContext.getResponse();
        ExchangeRateInfo exchangeRateInfo = responseConverter.convert(response, ExchangeRateInfo.class);
        try (AutoCloseableSoftAssertions softAssertions = new AutoCloseableSoftAssertions()) {
            softAssertions.assertThat(exchangeRateInfo.getBase())
                .overridingErrorMessage("Expected base currency to be not null")
                .isNotNull();
            softAssertions.assertThat(exchangeRateInfo.getRates())
                .overridingErrorMessage("Exchange rates are not included in response")
                .isNotEmpty();
            softAssertions.assertThat(exchangeRateInfo.getDate())
                .overridingErrorMessage("Expected date to be not null")
                .isNotNull();
        }
    }

    @Then("latest exchange rates are returned")
    public void latestExchangeRatesReturned() {
        Response actualResponse = testContext.getResponse();
        ExchangeRateInfo actualExchangeRates = responseConverter.convert(actualResponse, ExchangeRateInfo.class);
        LocalDate actualDate = actualExchangeRates.getDate();
        Response response = ratesApi.getLatestExchangeRates();
        ExchangeRateInfo latestExchangeRates = responseConverter.convert(response, ExchangeRateInfo.class);
        LocalDate latestDate = latestExchangeRates.getDate();
        try (AutoCloseableSoftAssertions softAssertions = new AutoCloseableSoftAssertions()) {
            softAssertions.assertThat(actualDate.isEqual(latestDate))
                .overridingErrorMessage("Expected to get exchange rates for date <%s>, but was <%s>", latestDate, actualDate)
                .isTrue();
            softAssertions.assertThat(actualExchangeRates.getRates()).containsAllEntriesOf(latestExchangeRates.getRates());
        }
    }

    @Then("exchange rate for {currency} is {bigdecimal}")
    public void exchangeRateIs(Currency currency, BigDecimal expectedRate) {
        ExchangeRateInfo exchangeRateInfo = responseConverter.convert(testContext.getResponse(), ExchangeRateInfo.class);
        BigDecimal actualRate = exchangeRateInfo.getRates().get(currency);
        assertThat(actualRate)
            .overridingErrorMessage("Expected exchange rate for %s to be equal to %s, but was %s",
                currency.getCurrencyCode(), expectedRate.toString(), actualRate.toString())
            .isEqualTo(expectedRate);
    }

    @Then("^base currency is ([A-Z]{3})$")
    public void verifyBaseCurrency(String expectedBase) {
        ExchangeRateInfo actualExchangeRateInfo = convertResponse(ExchangeRateInfo.class);
        String actualBase = actualExchangeRateInfo.getBase().getCurrencyCode();
        assertThat(actualBase)
            .overridingErrorMessage("Expected base currency to be %s, but was %s", expectedBase, actualBase)
            .isEqualTo(expectedBase);
    }

    @Then("exchange rates only for {currencyList} currencies are returned")
    public void verifyIncludedCurrencies(Collection<Currency> expectedSymbols) {
        ExchangeRateInfo actualExchangeRateInfo = convertResponse(ExchangeRateInfo.class);
        Collection<Currency> actualSymbols = actualExchangeRateInfo.getRates().keySet();
        assertThat(actualSymbols)
            .overridingErrorMessage("Expected exchange rates exactly for %s, but were for %s.", join(expectedSymbols), join(actualSymbols))
            .containsExactlyInAnyOrderElementsOf(expectedSymbols);
    }

    @Then("response status code is {int}")
    public void responseStatusCodeIs(int expectedStatus) {
        int actualStatus = testContext.getResponse().statusCode();
        assertThat(actualStatus)
            .overridingErrorMessage("Expected response status code to be %d, but was %d", expectedStatus, actualStatus)
            .isEqualTo(expectedStatus);
    }

    @Then("date is {date}")
    public void verifyDate(LocalDate expectedDate) {
        ExchangeRateInfo actualExchangeRateInfo = convertResponse(ExchangeRateInfo.class);
        LocalDate actualDate = actualExchangeRateInfo.getDate();
        assertThat(expectedDate.isEqual(actualDate))
            .overridingErrorMessage("Expected to get exchange rates for date <%s>, but was <%s>", expectedDate, actualDate)
            .isTrue();
    }

    @Then("error message is {string}")
    public void errorMessageIs(String expectedError) {
        ErrorInfo errorInfo = errorResponseConverter.convert(Objects.requireNonNull(testContext.getResponse()));
        String actualError = errorInfo.getError();
        assertThat(actualError)
            .overridingErrorMessage("Expected error message \"%s\", but was \"%s\"", expectedError, actualError)
            .isEqualTo(expectedError);
    }

    @Then("error message contains {string}")
    public void errorMessageContains(String expectedError) {
        ErrorInfo errorInfo = errorResponseConverter.convert(Objects.requireNonNull(testContext.getResponse()));
        String actualError = errorInfo.getError();
        assertThat(actualError)
            .overridingErrorMessage("Expected error message to contain \"%s\", but actual message was \"%s\"", expectedError, actualError)
            .contains(expectedError);
    }

    @Then("response content type is {string}")
    public void responseContentTypeIs(String expectedContentType) {
        String actualContentType = testContext.getResponse().contentType();
        assertThat(actualContentType)
            .overridingErrorMessage("Expected response content type <%s>, but was <%s>", expectedContentType, actualContentType)
            .isEqualTo(expectedContentType);
    }

    private <T> T convertResponse(Class<T> type) {
        return responseConverter.convert(Objects.requireNonNull(testContext.getResponse()), type);
    }

    private String join(Collection<Currency> currencies) {
        List<String> symbols = currencies.stream()
            .map(Currency::getCurrencyCode)
            .collect(Collectors.toList());
        return String.join(",", symbols);
    }

}
