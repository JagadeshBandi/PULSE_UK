package uk.pulse;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.pulse.factory.SimpleBrowserFactory;
import uk.pulse.observability.MetricsClient;

/**
 * Simple Base Test class for Payday Friday Traffic Testing
 * Provides basic setup/teardown for payment traffic scenarios
 */
public abstract class SimpleBaseTest {
    protected static final Logger logger = LoggerFactory.getLogger(SimpleBaseTest.class);
    
    protected static Playwright playwright;
    protected static Browser browser;
    protected BrowserContext context;
    protected Page page;
    protected MetricsClient metricsClient;
    
    // Test configuration
    protected static final String BASE_URL = System.getProperty("test.base.url", "http://localhost:8080");
    protected static final String INFLUX_URL = System.getProperty("influx.url", "http://localhost:8086");
    protected static final String INFLUX_TOKEN = System.getProperty("influx.token", "pulse_uk_2026");
    protected static final String INFLUX_ORG = System.getProperty("influx.org", "pulse-uk");
    protected static final String INFLUX_BUCKET = System.getProperty("influx.bucket", "resilience_metrics");
    
    @Before(order = 0)
    public static void globalSetup() {
        if (playwright == null) {
            logger.info("Initializing Playwright for Payday Friday testing");
            playwright = Playwright.create();
            browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(true));
        }
    }
    
    @Before(order = 1)
    public void setup(Scenario scenario) {
        logger.info("Setting up test: {}", scenario.getName());
        
        // Initialize metrics client
        try {
            metricsClient = new MetricsClient(INFLUX_URL, INFLUX_TOKEN, INFLUX_ORG, INFLUX_BUCKET);
        } catch (Exception e) {
            logger.warn("Metrics client initialization failed: {}", e.getMessage());
            metricsClient = null;
        }
        
        // Create mobile context for realistic testing
        context = SimpleBrowserFactory.createMobileContext(browser);
        page = context.newPage();
        
        // Set timeouts
        page.setDefaultTimeout(30000);
        page.setDefaultNavigationTimeout(30000);
        
        // Navigate to test application
        page.navigate(BASE_URL);
    }
    
    @After(order = 1)
    public void tearDown(Scenario scenario) {
        logger.info("Tearing down test: {}", scenario.getName());
        
        // Record test completion metrics
        if (metricsClient != null) {
            long endTime = System.currentTimeMillis();
            metricsClient.recordTestExecution(scenario.getName(), !scenario.isFailed(), endTime);
        }
        
        // Take screenshot if test failed
        if (scenario.isFailed() && page != null) {
            byte[] screenshot = page.screenshot(new Page.ScreenshotOptions()
                .setFullPage(true));
            scenario.attach(screenshot, "image/png", "failure-screenshot");
        }
        
        // Clean up
        if (page != null) {
            page.close();
        }
        if (context != null) {
            context.close();
        }
    }
    
    @After(order = 0)
    public static void globalTearDown() {
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }
    
    // Helper methods for metrics recording
    protected void recordTransactionMetrics(String transactionType, long responseTimeMs, boolean success, String amount) {
        if (metricsClient != null) {
            metricsClient.recordPaymentTransaction(transactionType, responseTimeMs, success, amount);
        }
    }
    
    protected void recordSystemLoadMetrics(int concurrentUsers, String loadType) {
        if (metricsClient != null) {
            metricsClient.recordSystemLoad(concurrentUsers, loadType);
        }
    }
    
    protected void recordResponseTimeMetrics(String operation, long responseTimeMs) {
        if (metricsClient != null) {
            metricsClient.recordResponseTimeDistribution(operation, responseTimeMs);
        }
    }
    
    // Helper methods
    protected void clickElement(String selector) {
        page.locator(selector).first().click();
    }
    
    protected void fillInput(String selector, String value) {
        page.locator(selector).first().fill(value);
    }
    
    protected void waitForElement(String selector) {
        page.locator(selector).first().waitFor(new Locator.WaitForOptions().setTimeout(10000));
    }
    
    protected void waitForNavigation() {
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }
    
    protected void assertResponseTimeWithin(long actualTime, long maxTime, String operation) {
        if (actualTime > maxTime) {
            String message = String.format("%s took %dms, expected <= %dms", operation, actualTime, maxTime);
            logger.error(message);
            throw new AssertionError(message);
        }
        logger.info("{} completed in {}ms (threshold: {}ms)", operation, actualTime, maxTime);
    }
}
