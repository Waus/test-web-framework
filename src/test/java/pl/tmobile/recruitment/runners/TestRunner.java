package pl.tmobile.recruitment.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "pl.tmobile.recruitment.steps",
        plugin = {"pretty", "json:target/cucumber-report.json", "html:target/cucumber-report.html"},
        monochrome = true
)
public class TestRunner {
}
