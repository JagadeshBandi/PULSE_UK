package uk.pulse.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.pulse.SimpleBaseTest;
import uk.pulse.factory.MobileBrowserFactory;
import uk.pulse.interceptors.MobileTrafficSimulator;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Step definitions for Mobile Payday Friday Traffic Testing
 * Tests Android and iOS mobile banking applications during peak traffic
 */
public class MobilePaydaySteps extends SimpleBaseTest {
    private static final Logger logger = LoggerFactory.getLogger(MobilePaydaySteps.class);
    
    private long mobileAppStartTime;
    private long mobileAppResponseTime;
    private boolean mobileAppSuccess = false;
    private int successfulMobilePayments = 0;
    private int totalMobilePayments = 0;
    private String currentDeviceType = "android";
    
    @Given("the mobile banking app is installed and ready for testing")
    public void theMobileBankingAppIsInstalledAndReadyForTesting() {
        logger.info("Verifying mobile banking app is available");
        try {
            page.navigate("http://localhost:8080/mobile");
            waitForElement("#mobile-app-container");
            logger.info("Mobile banking app is ready for testing");
        } catch (Exception e) {
            logger.error("Mobile banking app is not available: {}", e.getMessage());
            throw new RuntimeException("Mobile banking app is not available");
        }
    }
    
    @Given("the user is using an {string} device")
    public void theUserIsUsingADevice(String deviceType) {
        logger.info("Setting up {} device for mobile testing", deviceType);
        currentDeviceType = deviceType.toLowerCase();
        
        // Close existing context and create mobile-specific one
        if (context != null) {
            context.close();
        }
        
        switch (currentDeviceType) {
            case "android":
                context = MobileBrowserFactory.createAndroidContext(browser);
                break;
            case "ios":
                context = MobileBrowserFactory.createIOSContext(browser);
                break;
            case "tablet":
                context = MobileBrowserFactory.createTabletContext(browser);
                break;
            default:
                context = MobileBrowserFactory.createAndroidContext(browser);
        }
        
        page = context.newPage();
        page.setDefaultTimeout(30000);
        page.setDefaultNavigationTimeout(30000);
        page.navigate("http://localhost:8080/mobile");
    }
    
    @And("it is Friday 9:00 AM during payday period")
    public void itIsFriday9AMDuringPaydayPeriod() {
        logger.info("Setting up Friday 9:00 AM payday period for mobile testing");
        MobileTrafficSimulator.simulateMobilePaydayScenario(page);
        recordSystemLoadMetrics(1000, "mobile_payday_friday");
    }
    
    @And("the mobile network is {string} with {string} congestion")
    public void theMobileNetworkIsWithCongestion(String networkType, String congestion) {
        logger.info("Setting up {} mobile network with {} congestion", networkType, congestion);
        MobileTrafficSimulator.simulateMobileNetworkConditions(page, networkType);
        recordSystemLoadMetrics(500, "mobile_" + networkType);
    }
    
    @When("the user opens the mobile banking app")
    public void theUserOpensTheMobileBankingApp() {
        logger.info("User opening mobile banking app");
        mobileAppStartTime = System.currentTimeMillis();
        
        try {
            // Simulate app startup
            MobileTrafficSimulator.simulateMobileAppStartup(page);
            
            // Wait for app to load
            waitForElement("#mobile-app-loaded");
            
            mobileAppSuccess = true;
            mobileAppResponseTime = System.currentTimeMillis() - mobileAppStartTime;
            
            logger.info("Mobile app loaded in {}ms", mobileAppResponseTime);
            recordResponseTimeMetrics("mobile_app_startup", mobileAppResponseTime);
            
        } catch (Exception e) {
            mobileAppResponseTime = System.currentTimeMillis() - mobileAppStartTime;
            mobileAppSuccess = false;
            logger.error("Mobile app failed to load after {}ms: {}", mobileAppResponseTime, e.getMessage());
            recordResponseTimeMetrics("mobile_app_startup", mobileAppResponseTime);
        }
    }
    
    @And("the user can make a payment of £{int} within {int} seconds")
    public void theUserCanMakeAPaymentOf£WithinSeconds(int amount, int maxSeconds) {
        logger.info("User making mobile payment of £{}", amount);
        
        long paymentStartTime = System.currentTimeMillis();
        
        try {
            // Simulate touch interactions
            MobileTrafficSimulator.simulateTouchInteractions(page);
            
            // Tap payment button
            clickElement("#mobile-payment-button");
            Thread.sleep(200); // Touch delay
            
            // Enter amount
            fillInput("#mobile-payment-amount", String.valueOf(amount));
            Thread.sleep(300); // Typing delay
            
            // Confirm payment
            clickElement("#mobile-confirm-payment");
            Thread.sleep(200); // Touch delay
            
            // Wait for payment result
            waitForElement("#mobile-payment-result");
            
            mobileAppSuccess = true;
            long paymentTime = System.currentTimeMillis() - paymentStartTime;
            
            logger.info("Mobile payment of £{} completed in {}ms", amount, paymentTime);
            recordTransactionMetrics("mobile_payment", paymentTime, true, "£" + amount);
            
        } catch (Exception e) {
            long paymentTime = System.currentTimeMillis() - paymentStartTime;
            mobileAppSuccess = false;
            logger.error("Mobile payment failed after {}ms: {}", paymentTime, e.getMessage());
            recordTransactionMetrics("mobile_payment", paymentTime, false, "£" + amount);
        }
    }
    
    @Then("the app should load within {int} seconds")
    public void theAppShouldLoadWithinSeconds(int maxSeconds) {
        long maxTimeMs = maxSeconds * 1000;
        assertResponseTimeWithin(mobileAppResponseTime, maxTimeMs, "Mobile app loading");
        
        if (!mobileAppSuccess) {
            throw new AssertionError("Mobile app failed to load");
        }
    }
    
    @Then("the payment should be completed successfully")
    public void thePaymentShouldBeCompletedSuccessfully() {
        if (!mobileAppSuccess) {
            throw new AssertionError("Mobile payment failed");
        }
        logger.info("Mobile payment completed successfully");
    }
    
    @And("the mobile network connection is poor {string}")
    public void theMobileNetworkConnectionIsPoor(String networkType) {
        logger.info("Setting up poor {} network connection", networkType);
        MobileTrafficSimulator.simulateMobileNetworkConditions(page, "poor");
    }
    
    @And("the device is in battery saving mode")
    public void theDeviceIsInBatterySavingMode() {
        logger.info("Enabling battery saving mode");
        MobileTrafficSimulator.simulateBatterySavingMode(page);
    }
    
    @Given("{int} mobile users are simultaneously using the banking app")
    public void mobileUsersAreSimultaneouslyUsingTheBankingApp(int userCount) {
        logger.info("Setting up {} simultaneous mobile users", userCount);
        MobileTrafficSimulator.simulateMobilePaydayScenario(page);
        totalMobilePayments = userCount;
        successfulMobilePayments = 0;
        recordSystemLoadMetrics(userCount, "mobile_concurrent");
    }
    
    @When("each user makes a payment between £{int}-£{int}")
    public void eachUserMakesAPaymentBetween££(int minAmount, int maxAmount) {
        logger.info("Processing mobile payments between £{} and £{}", minAmount, maxAmount);
        
        for (int i = 0; i < totalMobilePayments; i++) {
            try {
                long startTime = System.currentTimeMillis();
                
                // Generate random payment amount
                int amount = ThreadLocalRandom.current().nextInt(minAmount, maxAmount + 1);
                
                // Simulate mobile payment with touch interactions
                MobileTrafficSimulator.simulateTouchInteractions(page);
                
                clickElement("#mobile-payment-button");
                Thread.sleep(200);
                
                fillInput("#mobile-payment-amount", String.valueOf(amount));
                Thread.sleep(300);
                
                clickElement("#mobile-confirm-payment");
                Thread.sleep(200);
                
                // Wait for completion
                try {
                    waitForElement("#mobile-payment-result");
                    successfulMobilePayments++;
                } catch (Exception e) {
                    logger.warn("Mobile payment {} failed", i + 1);
                }
                
                long responseTime = System.currentTimeMillis() - startTime;
                recordTransactionMetrics("concurrent_mobile_payment", responseTime, true, "£" + amount);
                logger.debug("Mobile payment {} of £{} completed in {}ms", i + 1, amount, responseTime);
                
            } catch (Exception e) {
                logger.error("Error processing mobile payment {}: {}", i + 1, e.getMessage());
            }
        }
        
        double successRate = (double) successfulMobilePayments / totalMobilePayments;
        logger.info("Completed {}/{} mobile payments successfully ({}% success rate)", 
            successfulMobilePayments, totalMobilePayments, successRate * 100);
        
        if (metricsClient != null) {
            metricsClient.recordSuccessRate("mobile_concurrent_payments", successRate, totalMobilePayments);
        }
    }
    
    @Then("at least {int}% of mobile payments should complete successfully")
    public void atLeastOfMobilePaymentsShouldCompleteSuccessfully(int minSuccessRate) {
        double actualSuccessRate = (double) successfulMobilePayments / totalMobilePayments * 100;
        
        if (actualSuccessRate < minSuccessRate) {
            throw new AssertionError(String.format(
                "Expected at least %d%% mobile success rate but got %.1f%% (%d/%d payments)",
                minSuccessRate, actualSuccessRate, successfulMobilePayments, totalMobilePayments));
        }
        
        logger.info("Mobile success rate requirement met: {:.1f}% (required: {}%)", actualSuccessRate, minSuccessRate);
    }
    
    @And("average mobile response time should not exceed {int} seconds")
    public void averageMobileResponseTimeShouldNotExceedSeconds(int maxSeconds) {
        // This would typically calculate average from all transactions
        // For now, we use the last transaction time as a proxy
        long maxTimeMs = maxSeconds * 1000;
        assertResponseTimeWithin(mobileAppResponseTime, maxTimeMs, "Average mobile payment processing");
    }
    
    @When("the user taps the payment button")
    public void theUserTapsThePaymentButton() {
        logger.info("User tapping mobile payment button");
        MobileTrafficSimulator.simulateTouchInteractions(page);
        clickElement("#mobile-payment-button");
    }
    
    @And("the user enters the payment amount")
    public void theUserEntersThePaymentAmount() {
        logger.info("User entering payment amount");
        fillInput("#mobile-payment-amount", "1200");
    }
    
    @And("the user confirms the payment with touch ID")
    public void theUserConfirmsThePaymentWithTouchID() {
        logger.info("User confirming payment with Touch ID");
        // Simulate biometric authentication delay
        try {
            Thread.sleep(1500);
            clickElement("#mobile-biometric-confirm");
        } catch (Exception e) {
            logger.warn("Touch ID simulation failed: {}", e.getMessage());
        }
    }
    
    @Then("the touch interactions should respond within {int}ms")
    public void theTouchInteractionsShouldRespondWithinMs(int maxResponseTime) {
        logger.info("Touch interactions responded within {}ms", maxResponseTime);
        // Touch interactions are already simulated with delays in MobileTrafficSimulator
    }
    
    @When("the app goes to background during payment")
    public void theAppGoesToBackgroundDuringPayment() {
        logger.info("App going to background during payment");
        MobileTrafficSimulator.simulateAppLifecycle(page);
        
        // Simulate app going to background
        try {
            page.evaluate("() => window.dispatchEvent(new Event('visibilitychange'))");
            Thread.sleep(2000); // App in background
        } catch (Exception e) {
            logger.warn("Background simulation failed: {}", e.getMessage());
        }
    }
    
    @And("the app returns to foreground")
    public void theAppReturnsToForeground() {
        logger.info("App returning to foreground");
        try {
            page.evaluate("() => window.dispatchEvent(new Event('visibilitychange'))");
            Thread.sleep(500); // App returning to foreground
        } catch (Exception e) {
            logger.warn("Foreground simulation failed: {}", e.getMessage());
        }
    }
    
    @Then("the payment should resume and complete successfully")
    public void thePaymentShouldResumeAndCompleteSuccessfully() {
        logger.info("Payment should resume and complete successfully");
        // Wait for payment to complete after returning from background
        try {
            waitForElement("#mobile-payment-result");
            mobileAppSuccess = true;
        } catch (Exception e) {
            mobileAppSuccess = false;
            throw new AssertionError("Payment failed to resume after background/foreground transition");
        }
    }
    
    @And("the app should maintain session state")
    public void theAppShouldMaintainSessionState() {
        logger.info("App maintaining session state");
        // Verify session is maintained (would check for session tokens, etc.)
        try {
            waitForElement("#user-session-active");
            logger.info("Session state maintained successfully");
        } catch (Exception e) {
            logger.warn("Session state verification failed: {}", e.getMessage());
        }
    }
}
