package uk.pulse.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Test Runner for Pulse App Traffic Simulation Scenarios
 * Tests the comprehensive Pulse application demonstrating all Payday Friday traffic patterns
 */
@Test
@CucumberOptions(
    features = {"classpath:features"},
    glue = {"uk.pulse.steps"},
    plugin = {
        "pretty",
        "html:target/cucumber-reports/pulse-app-pretty.html",
        "json:target/cucumber-reports/pulse-app.json",
        "junit:target/cucumber-reports/pulse-app.xml"
    },
    monochrome = true,
    tags = "@Pulse"
)
public class PulseAppRunner extends AbstractTestNGCucumberTests {
    
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
