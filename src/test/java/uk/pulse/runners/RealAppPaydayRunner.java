package uk.pulse.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Test Runner for Real App Payday Friday Scenarios
 * Tests actual Play Store and App Store applications during peak traffic
 */
@Test
@CucumberOptions(
    features = {"classpath:features"},
    glue = {"uk.pulse.steps"},
    plugin = {
        "pretty",
        "html:target/cucumber-reports/real-app-payday-pretty.html",
        "json:target/cucumber-reports/real-app-payday.json",
        "junit:target/cucumber-reports/real-app-payday.xml"
    },
    monochrome = true,
    tags = "@RealApp"
)
public class RealAppPaydayRunner extends AbstractTestNGCucumberTests {
    
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
