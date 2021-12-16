package io.ratesapi.demo;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

import io.restassured.filter.Filter;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import lombok.RequiredArgsConstructor;

@Component
@ConditionalOnProperty(prefix = "framework", value = "console-request-logging", havingValue = "true")
@RequiredArgsConstructor
public class ConsoleAndReportRestAssuredFilters implements RestAssuredFiltersProvider {

    private final ReportAttachmentRestAssuredFilter reportAttachmentRestAssuredFilter;

    @Override
    public List<Filter> getFilters() {
        return List.of(new RequestLoggingFilter(), new ResponseLoggingFilter(), new ErrorLoggingFilter(), reportAttachmentRestAssuredFilter);
    }

}
