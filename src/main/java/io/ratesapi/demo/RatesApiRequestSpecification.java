package io.ratesapi.demo;

import org.springframework.stereotype.Component;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RatesApiRequestSpecification {

    private static final String API_PATH = "api";

    private final FrameworkConfiguration frameworkConfiguration;
    private final RestAssuredFiltersProvider restAssuredFiltersProvider;

    /**
     * Provides basic request specification for interacting with ratesapi.io.
     *
     * @return an instance of {@link RequestSpecification} configured for interaction with ratesapi.io
     */
    public RequestSpecification ratesApi() {
        return RestAssured.given()
                .urlEncodingEnabled(false)
                .baseUri(frameworkConfiguration.getRatesapiUrl())
                .basePath(API_PATH)
                .filters(restAssuredFiltersProvider.getFilters());
    }

}
