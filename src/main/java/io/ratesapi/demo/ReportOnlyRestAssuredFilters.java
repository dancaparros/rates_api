package io.ratesapi.demo;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

import io.restassured.filter.Filter;
import lombok.RequiredArgsConstructor;

@Component
@ConditionalOnProperty(prefix = "framework", value = "console-request-logging", havingValue = "false")
@RequiredArgsConstructor
public class ReportOnlyRestAssuredFilters implements RestAssuredFiltersProvider {

    private final ReportAttachmentRestAssuredFilter reportAttachmentRestAssuredFilter;

    @Override
    public List<Filter> getFilters() {
        return List.of(reportAttachmentRestAssuredFilter);
    }

}