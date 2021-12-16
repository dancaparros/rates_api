package io.ratesapi.demo;

import org.springframework.stereotype.Service;

import java.util.Collection;

import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Class responsible for sending HTTP requests to ratesapi.io.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RatesApi {

    private static final String LATEST_PATH = "latest";
    private static final String BASE_PARAM = "base";
    private static final String SYMBOLS_PARAM = "symbols";

    private final RatesApiRequestSpecification ratesApi;

    /**
     * Gets the latest foreign exchange reference rates.
     *
     * @return API response
     */
    public Response getLatestExchangeRates() {
        return sendGetRequest(LATEST_PATH);
    }

    /**
     * Gets the latest foreign exchange reference rates for specific currency.
     *
     * @param base reference curency
     * @return API response
     */
    public Response getLatestExchangeRates(String base) {
        return getForBase(LATEST_PATH, base);
    }

    public Response getLatestExchangeRates(Collection<String> symbols) {
        return getForSymbols(LATEST_PATH, symbols);
    }

    public Response getLatestExchangeRates(String base, Collection<String> symbols) {
        return getForBaseAndSymbols(LATEST_PATH, base, symbols);
    }

    /**
     * Gets exchange rates for the specified date.
     *
     * @param date date to get exchange rates for. Must be specified in {@code yyyy-MM-dd} format
     * @return API response
     */
    public Response getExchangeRates(String date) {
        return sendGetRequest(date);
    }

    public Response getExchangeRates(String date, Collection<String> symbols) {
        return getForSymbols(date, symbols);
    }
    public Response getExchangeRates(String date, String base) {
        return getForBase(date, base);
    }

    public Response getExchangeRates(String date, String base, Collection<String> symbols) {
        return getForBaseAndSymbols(date, base, symbols);
    }

    public Response getExchangeRates(String date, String base, String symbols) {
        return getForBaseAndSymbols(date, base, symbols);
    }

    /**
     * Sends GET request to endpoint provided as parameter.
     *
     * @param path endpoint path
     */
    public Response sendGetRequest(String path) {
        return ratesApi.ratesApi()
                .get(path);
    }

    private Response getForBase(String path, String base) {
        return ratesApi.ratesApi()
                .queryParam(BASE_PARAM, base)
                .get(path);
    }

    private Response getForSymbols(String path, Collection<String> symbols) {
        return ratesApi.ratesApi()
                .queryParam(SYMBOLS_PARAM, convertToCommaSeparatedString(symbols))
                .get(path);
    }

    private Response getForBaseAndSymbols(String path, String base, Collection<String> symbols) {
        return ratesApi.ratesApi()
                .queryParam(BASE_PARAM, base)
                .queryParam(SYMBOLS_PARAM, convertToCommaSeparatedString(symbols))
                .get(path);
    }

    private Response getForBaseAndSymbols(String path, String base, String symbols) {
        return ratesApi.ratesApi()
                .queryParam(BASE_PARAM, base)
                .queryParam(SYMBOLS_PARAM, symbols)
                .get(path);
    }

    private String convertToCommaSeparatedString(Collection<String> collection) {
        return String.join(",", collection);
    }

}
