package io.ratesapi.demo;

import org.springframework.stereotype.Component;

import java.util.Arrays;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;

/**
 * Converter for 4xx responses.
 */
@Component
@RequiredArgsConstructor
public class ErrorResponseConverter {

    private final ResponseConverter responseConverter;

    /**
     * Converts 4xx response body to {@link ErrorInfo} instance. Uses {@link ResponseConverter} to convert responses with {@code
     * application/json} content type and different mechanism for other types.
     *
     * @param response response to convert
     * @return {@link ErrorInfo} instance
     */
    public ErrorInfo convert(Response response) {
        int status = response.statusCode();
        if (status >= 400 && status < 500) {
            String contentType = response.contentType();
            if (Arrays.asList(ContentType.JSON.getContentTypeStrings()).contains(contentType)) {
                return responseConverter.convert(response, ErrorInfo.class);
            } else {
                ErrorInfo errorInfo = new ErrorInfo();
                errorInfo.setError(response.asString());
                return errorInfo;
            }
        }
        throw new IllegalArgumentException(
                String.format("Response with status code %d is not an 4xx error response and cannot be converted to ErrorInfo.", status));
    }

}
