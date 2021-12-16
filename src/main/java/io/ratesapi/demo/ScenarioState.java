package io.ratesapi.demo;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

import io.cucumber.java.Scenario;
import io.cucumber.spring.ScenarioScope;
import lombok.Getter;
import lombok.Setter;

/**
 * Holds information about currently executing scenario and allows to interact with it.
 */
@Getter
@Component
@ScenarioScope
public class ScenarioState {

    /**
     * Currently executing scenario
     */
    @Setter
    private Scenario scenario;

    /**
     * Scenario start time
     */
    private final LocalDateTime scenarioDateTime;

    public ScenarioState() {
        scenarioDateTime = LocalDateTime.now();
    }

    /**
     * Attaches custom data to test report.
     *
     * @param data     what to attach to report
     * @param mimeType data type
     * @param name     name for the attachment which will be displayed in report
     */
    public void attachToReport(byte[] data, String mimeType, String name) {
        Objects.requireNonNull(scenario).attach(data, mimeType, name);
    }

    /**
     * Writes some text information to test report.
     *
     * @param text information to write to report
     */
    public void writeToReport(String text) {
        Objects.requireNonNull(scenario).log(text);
    }
}
