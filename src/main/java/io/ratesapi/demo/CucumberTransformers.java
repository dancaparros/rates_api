package io.ratesapi.demo;

import com.google.common.base.Splitter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Currency;
import java.util.stream.Collectors;

import io.cucumber.java.ParameterType;

/**
 * Contains methods which tell Cucumber how to transform specific step parameters.
 */
public class CucumberTransformers {

    @ParameterType(name = "currency", value = "[A-Z]{3}")
    public Currency transformCurrency(String value) {
        return Currency.getInstance(value);
    }

    @ParameterType(name = "currencyListString", value = "\\S+")
    public Collection<String> transformCurrencyListString(String value) {
        return splitOnComma(value);
    }

    @ParameterType(name = "currencyList", value = "\\S+")
    public Collection<Currency> transformCurrencyList(String value) {
        return splitOnComma(value).stream()
            .map(Currency::getInstance)
            .collect(Collectors.toList());
    }

    @ParameterType(name = "date", value = "\\d{4}-\\d{2}-\\d{2}")
    public LocalDate transformDate(String value) {
        return LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    private Collection<String> splitOnComma(String value) {
        return Splitter.on(',').splitToList(value);
    }

}
