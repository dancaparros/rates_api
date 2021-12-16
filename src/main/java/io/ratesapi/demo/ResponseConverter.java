package io.ratesapi.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Component;

import io.restassured.response.Response;

/**
 * Class responsible for converting {@link io.restassured.response.Response} body to specified type.
 */
@Component
public class ResponseConverter {

    /**
     * Converts API response to given type. Method does not check if response content type is {@code application/json}.
     *
     * @param response API response
     * @param type     target type to convert response body to
     * @param <T>      generic type
     * @return instance of {@code T}
     */
    public <T> T convert(Response response, Class<T> type) {
        String responseString = response.asString();
        try {
            return new ObjectMapper().readValue(responseString, type);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(String.format("Failed to convert response %s to %s instance", responseString, type.getName()));
        }
    }

}
