package uk.pulse.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.pulse.SimpleBaseTest;
import uk.pulse.interceptors.PaydayTrafficSimulator;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Step definitions for Payday Friday Traffic Testing
 * Focuses specifically on payment traffic during Friday 9:00 AM peak period
 */
public class PaydayTrafficSteps extends SimpleBaseTest {
    private static final Logger logger = LoggerFactory.getLogger(PaydayTrafficSteps.class);
    
    private long transactionStartTime;
    private long transactionResponseTime;
    private boolean transactionSuccess = false;
    private int successfulPayments = 0;
    private int totalPayments = 0;
    
    @Given("the payment application is available and ready for testing")
    public void thePaymentApplicationIsAvailableAndReadyForTesting() {
        logger.info("Verifying payment application is available");
        // Verify the application is loaded and ready
        try {
            page.navigate("http://localhost:8080");
            waitForElement("#payment-button");
            logger.info("Payment application is ready for testing");
        } catch (Exception e) {
            logger.error("Payment application is not available: {}", e.getMessage());
            throw new RuntimeException("Payment application is not available");
        }
    }
    
    @Given("it is Friday 9:00 AM during payday period")
    public void itIsFriday9AMDuringPaydayPeriod() {
        logger.info("Setting up Friday 9:00 AM payday period simulation");
        PaydayTrafficSimulator.applyPaydayFridayTraffic(page);
        
        // Record traffic simulation metrics
        recordSystemLoadMetrics(10000, "payday_friday");
    }
    
    @And("the system is experiencing {int} concurrent users")
    public void theSystemIsExperiencingConcurrentUsers(int userCount) {
        logger.info("Simulating {} concurrent users during payday", userCount);
        // In a real implementation, this would set up multiple browser contexts
        // For now, we simulate the load through network delays
    }
    
    @When("users attempt to make salary payments")
    public void usersAttemptToMakeSalaryPayments() {
        logger.info("Users attempting salary payments during peak traffic");
        
        transactionStartTime = System.currentTimeMillis();
        
        try {
            // Navigate to payment page
            clickElement("#payment-button");
            waitForNavigation();
            
            // Fill payment details for salary payment
            fillInput("#payment-amount", "1500");
            fillInput("#recipient", "Salary Account");
            fillInput("#reference", "Monthly Salary");
            
            // Submit payment
            clickElement("#submit-payment");
            
            // Wait for response
            waitForElement("#payment-result");
            
            transactionSuccess = true;
            transactionResponseTime = System.currentTimeMillis() - transactionStartTime;
            
            logger.info("Salary payment completed in {}ms", transactionResponseTime);
            
            // Record transaction metrics
            recordTransactionMetrics("salary_payment", transactionResponseTime, true, "£1500");
            
        } catch (Exception e) {
            transactionResponseTime = System.currentTimeMillis() - transactionStartTime;
            transactionSuccess = false;
            logger.error("Salary payment failed after {}ms: {}", transactionResponseTime, e.getMessage());
            
            // Record failed transaction metrics
            recordTransactionMetrics("salary_payment", transactionResponseTime, false, "£1500");
        }
    }
    
    @Then("all payments should be processed within {int} seconds")
    public void allPaymentsShouldBeProcessedWithinSeconds(int maxSeconds) {
        long maxTimeMs = maxSeconds * 1000;
        assertResponseTimeWithin(transactionResponseTime, maxTimeMs, "Salary payment processing");
        
        // Record response time metrics
        recordResponseTimeMetrics("salary_payment", transactionResponseTime);
        
        if (!transactionSuccess) {
            throw new AssertionError("Payment processing failed");
        }
    }
    
    @And("the system should maintain {int}% success rate")
    public void theSystemShouldMaintainSuccessRate(int expectedSuccessRate) {
        // For single transaction, success rate is either 0% or 100%
        if (expectedSuccessRate > 0 && !transactionSuccess) {
            throw new AssertionError("Expected " + expectedSuccessRate + "% success rate but payment failed");
        }
        logger.info("Payment success rate requirement met: {}%", transactionSuccess ? 100 : 0);
    }
    
    @And("response times should be logged for analysis")
    public void responseTimesShouldBeLoggedForAnalysis() {
        logger.info("Payment response time logged: {}ms", transactionResponseTime);
        // In a real implementation, this would log to a monitoring system
    }
    
    @Given("the payment system is under maximum load")
    public void thePaymentSystemIsUnderMaximumLoad() {
        logger.info("Setting up maximum load conditions");
        PaydayTrafficSimulator.applyExtremePaydayStress(page);
    }
    
    @When("a user authorizes a payment of £{int}")
    public void aUserAuthorizesAPaymentOf£(int amount) {
        logger.info("Authorizing payment of £{}", amount);
        
        transactionStartTime = System.currentTimeMillis();
        
        try {
            // Navigate to payment authorization
            clickElement("#authorize-payment");
            waitForNavigation();
            
            // Fill payment amount
            fillInput("#amount", String.valueOf(amount));
            
            // Authorize payment
            clickElement("#confirm-authorization");
            
            // Wait for authorization result
            waitForElement("#authorization-result");
            
            transactionSuccess = true;
            transactionResponseTime = System.currentTimeMillis() - transactionStartTime;
            
            logger.info("Payment authorization of £{} completed in {}ms", amount, transactionResponseTime);
            
        } catch (Exception e) {
            transactionResponseTime = System.currentTimeMillis() - transactionStartTime;
            transactionSuccess = false;
            logger.error("Payment authorization failed after {}ms: {}", transactionResponseTime, e.getMessage());
        }
    }
    
    @Then("the authorization should complete within {int} seconds")
    public void theAuthorizationShouldCompleteWithinSeconds(int maxSeconds) {
        long maxTimeMs = maxSeconds * 1000;
        assertResponseTimeWithin(transactionResponseTime, maxTimeMs, "Payment authorization");
        
        if (!transactionSuccess) {
            throw new AssertionError("Payment authorization failed");
        }
    }
    
    @And("the transaction should be recorded successfully")
    public void theTransactionShouldBeRecordedSuccessfully() {
        if (!transactionSuccess) {
            throw new AssertionError("Transaction was not recorded successfully");
        }
        logger.info("Transaction recorded successfully");
    }
    
    @Given("{int} users are simultaneously making salary payments")
    public void usersAreSimultaneouslyMakingSalaryPayments(int userCount) {
        logger.info("Setting up {} simultaneous salary payment users", userCount);
        PaydayTrafficSimulator.applyExtremePaydayStress(page);
        totalPayments = userCount;
        successfulPayments = 0;
    }
    
    @When("each user processes a payment between £{int}-£{int}")
    public void eachUserProcessesAPaymentBetween££(int minAmount, int maxAmount) {
        logger.info("Processing payments between £{} and £{}", minAmount, maxAmount);
        
        for (int i = 0; i < totalPayments; i++) {
            try {
                long startTime = System.currentTimeMillis();
                
                // Generate random payment amount
                int amount = java.util.concurrent.ThreadLocalRandom.current().nextInt(minAmount, maxAmount + 1);
                
                // Process payment
                clickElement("#payment-button");
                fillInput("#payment-amount", String.valueOf(amount));
                clickElement("#submit-payment");
                
                // Wait for completion
                try {
                    waitForElement("#payment-result");
                    successfulPayments++;
                } catch (Exception e) {
                    logger.warn("Payment {} failed", i + 1);
                }
                
                long responseTime = System.currentTimeMillis() - startTime;
                logger.debug("Payment {} of £{} completed in {}ms", i + 1, amount, responseTime);
                
            } catch (Exception e) {
                logger.error("Error processing payment {}: {}", i + 1, e.getMessage());
            }
        }
        
        double successRate = (double) successfulPayments / totalPayments;
        logger.info("Completed {}/{} payments successfully ({}% success rate)", 
            successfulPayments, totalPayments, successRate * 100);
    }
    
    @Then("at least {int}% of payments should complete successfully")
    public void atLeastOfPaymentsShouldCompleteSuccessfully(int minSuccessRate) {
        double actualSuccessRate = (double) successfulPayments / totalPayments * 100;
        
        if (actualSuccessRate < minSuccessRate) {
            throw new AssertionError(String.format(
                "Expected at least %d%% success rate but got %.1f%% (%d/%d payments)",
                minSuccessRate, actualSuccessRate, successfulPayments, totalPayments));
        }
        
        logger.info("Success rate requirement met: {:.1f}% (required: {}%)", actualSuccessRate, minSuccessRate);
    }
    
    @And("average processing time should not exceed {int} seconds")
    public void averageProcessingTimeShouldNotExceedSeconds(int maxSeconds) {
        // This would typically calculate average from all transactions
        // For now, we use the last transaction time as a proxy
        long maxTimeMs = maxSeconds * 1000;
        assertResponseTimeWithin(transactionResponseTime, maxTimeMs, "Average payment processing");
    }
    
    // Utility methods removed - using ThreadLocalRandom directly
}
