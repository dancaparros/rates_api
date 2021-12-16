package io.ratesapi.demo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * Representation of data returned by ratesapi.io.
 */
@Getter
@Setter
public class ExchangeRateInfo {

    private Currency base;
    private Map<Currency, BigDecimal> rates;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate date;

}
