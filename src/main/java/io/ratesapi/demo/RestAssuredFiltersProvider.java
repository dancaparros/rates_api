package io.ratesapi.demo;

import java.util.List;

import io.restassured.filter.Filter;

/**
 * Common interface for all Rest Assured filters providers.
 */
public interface RestAssuredFiltersProvider {

    /**
     * Gets Rest Assured filters which will be enabled when interacting ratesapi.io service.
     *
     * @return list containing Rest Assured filters
     */
    List<Filter> getFilters();

}
