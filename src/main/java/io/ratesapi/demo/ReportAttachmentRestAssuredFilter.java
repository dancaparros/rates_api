package io.ratesapi.demo;

import com.fasterxml.jackson.databind.ObjectMapper;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import lombok.RequiredArgsConstructor;

/**
 * A filter that embeds in test report a HTML attachment containing request and response parameters.
 */
@Component
@RequiredArgsConstructor
public class ReportAttachmentRestAssuredFilter implements Filter {

    private static final String REQUEST_RESPONSE_TEMPLATE = "request_response.ftlh";

    private final Configuration cfg;
    private final ObjectFactory<ScenarioState> scenarioStateFactory;

    @Override
    public Response filter(FilterableRequestSpecification filterableRequestSpecification,
                           FilterableResponseSpecification filterableResponseSpecification, FilterContext filterContext) {
        Response response = filterContext.next(filterableRequestSpecification, filterableResponseSpecification);
        String name = String.format("%s %s", filterableRequestSpecification.getMethod(), filterableRequestSpecification.getUserDefinedPath());
        scenarioStateFactory.getObject().attachToReport(processTemplate(filterableRequestSpecification, response).getBytes(), "text/html", name);
        return response;
    }

    private String processTemplate(FilterableRequestSpecification frs, Response response) {
        FreemarkerRequestAndResponse frar = buildFreemarkerRequestAndResponse(frs, response);
        try {
            Template template = cfg.getTemplate(REQUEST_RESPONSE_TEMPLATE);
            StringWriter sw = new StringWriter();
            template.process(frar, sw);
            return sw.toString();
        } catch (IOException | TemplateException e) {
            throw new IllegalStateException("Failed to process Freemarker template for report generation", e);
        }
    }

    private FreemarkerRequestAndResponse buildFreemarkerRequestAndResponse(FilterableRequestSpecification frs, Response response) {
        return FreemarkerRequestAndResponse.builder()
                .requestUri(frs.getURI())
                .requestQueryParams(frs.getQueryParams())
                .requestFormParams(frs.getFormParams())
                .requestMethod(frs.getMethod())
                .requestContentType(frs.getContentType())
                .requestHeaders(frs.getHeaders().asList())
                .requestBody(prettyRequestBody(frs))
                .responseStatusLine(response.getStatusLine())
                .responseContentType(response.getContentType())
                .responseHeaders(response.getHeaders().asList())
                .responseBody(prettyResponseBody(response))
                .build();
    }

    private String prettyRequestBody(FilterableRequestSpecification frs) {
        if (frs.getBody() != null && hasJsonContentType(frs)) {
            return JsonPath.from(frs.<String>getBody()).prettify();
        } else {
            return frs.getBody();
        }
    }

    private String prettyResponseBody(Response response) {
        String body = response.getBody().asString();
        try {
            new ObjectMapper().readTree(body);
        } catch (IOException e) {
            return body;
        }
        if (body.isBlank()) {
            return body;
        }
        return response.getBody().jsonPath().prettify();
    }

    private boolean hasJsonContentType(FilterableRequestSpecification frs) {
        return Arrays.stream(ContentType.JSON.getContentTypeStrings())
                .anyMatch(frs.getContentType()::contains);
    }

}
