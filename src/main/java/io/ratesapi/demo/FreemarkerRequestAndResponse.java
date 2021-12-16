package io.ratesapi.demo;

import java.util.List;
import java.util.Map;

import io.restassured.http.Header;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FreemarkerRequestAndResponse {

    private final String requestUri;
    private final String requestMethod;
    private final String requestContentType;
    private final String requestBody;
    private final List<Header> requestHeaders;
    private final Map<String, String> requestQueryParams;
    private final Map<String, String> requestFormParams;
    private final String responseStatusLine;
    private final String responseContentType;
    private final String responseBody;
    private final List<Header> responseHeaders;

}
