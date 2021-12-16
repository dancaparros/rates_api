package io.ratesapi.demo;

import java.time.temporal.ChronoUnit;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Class holding {@link Before} hooks.
 */
@Slf4j
@RequiredArgsConstructor
public class BeforeHooks {

    private final ScenarioState scenarioState;

    /**
     * Sets currently executing scenario for {@link ScenarioState}.
     *
     * @param scenario currently executing scenario provided by Cucumber
     */
    @Before(order = 0)
    public void before(Scenario scenario) {
        log.info("Running scenario \"{}\" at {}", scenario.getName(),
            scenarioState.getScenarioDateTime().truncatedTo(ChronoUnit.SECONDS).toString());
        scenarioState.setScenario(scenario);
    }

}
