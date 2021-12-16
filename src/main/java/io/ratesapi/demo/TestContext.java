package io.ratesapi.demo;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

import io.cucumber.spring.ScenarioScope;
import io.restassured.response.Response;
import lombok.Getter;
import lombok.Setter;

/**
 * Holds data which is passed between steps within scenario.
 */
@Getter
@Setter
@Component
@ScenarioScope
public class TestContext {

    private Currency base;
    private Response response;

}
