package io.ratesapi.demo;

import org.testng.annotations.DataProvider;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
    features = {
        "src/test/resources/features/"
    },
    plugin = {
        "json:target/cucumber/report.json",
        "timeline:target/cucumber/timeline"
    },
    tags = "@Ratesapi"
)
public class CucumberTests extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }

}
