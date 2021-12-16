package io.ratesapi.demo;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents error response returned by ratesapi.io in case of 4xx response.
 */
@Getter
@Setter
public class ErrorInfo {

    /**
     * Error message
     */
    private String error;

}
