package uk.pulse.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Test Runner for Payday Friday Traffic Scenarios
 * Focuses specifically on payment traffic during Friday 9:00 AM peak period
 */
@Test
@CucumberOptions(
    features = {"classpath:features"},
    glue = {"uk.pulse.steps"},
    plugin = {
        "pretty",
        "html:target/cucumber-reports/payday-traffic-pretty.html",
        "json:target/cucumber-reports/payday-traffic.json",
        "junit:target/cucumber-reports/payday-traffic.xml"
    },
    monochrome = true,
    tags = "@PaydayFriday"
)
public class PaydayTrafficRunner extends AbstractTestNGCucumberTests {
    
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
