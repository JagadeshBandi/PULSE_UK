package uk.pulse.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Test Runner for Mobile Payday Friday Scenarios
 * Tests Android and iOS mobile banking applications during peak traffic
 */
@Test
@CucumberOptions(
    features = {"classpath:features"},
    glue = {"uk.pulse.steps"},
    plugin = {
        "pretty",
        "html:target/cucumber-reports/mobile-payday-pretty.html",
        "json:target/cucumber-reports/mobile-payday.json",
        "junit:target/cucumber-reports/mobile-payday.xml"
    },
    monochrome = true,
    tags = "@Mobile"
)
public class MobilePaydayRunner extends AbstractTestNGCucumberTests {
    
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
