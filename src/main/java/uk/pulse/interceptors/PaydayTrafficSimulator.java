package uk.pulse.interceptors;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Payday Friday Traffic Simulator
 * Simulates high-volume payment traffic during Friday 9:00 AM payday period
 */
public class PaydayTrafficSimulator {
    private static final Logger logger = LoggerFactory.getLogger(PaydayTrafficSimulator.class);
    
    // Payday Friday specific constants
    private static final int BASE_DELAY_MS = 1500; // Base server load delay
    private static final int PEAK_DELAY_VARIATION = 2000; // Additional delay during peak
    private static final double FAILURE_RATE = 0.01; // 1% failure rate during normal payday
    private static final double PEAK_FAILURE_RATE = 0.05; // 5% failure rate during peak 9 AM
    
    /**
     * Simulates Payday Friday 9:00 AM traffic conditions
     */
    public static void applyPaydayFridayTraffic(Page page) {
        logger.info("Applying Payday Friday 9:00 AM traffic simulation");
        
        // Intercept payment processing APIs
        page.route("**/api/payments/**", route -> {
            try {
                // Simulate server load during payday
                int delay = calculatePaydayDelay();
                logger.debug("Payday payment delay: {}ms", delay);
                Thread.sleep(delay);
                
                // Simulate occasional failures during peak load
                if (shouldSimulateFailure()) {
                    logger.warn("Simulating payment failure during peak load");
                    route.fulfill(new Route.FulfillOptions()
                        .setStatus(500)
                        .setBody("{\"error\":\"Payment processing failed due to high load\"}"));
                    return;
                }
                
                route.resume();
                
            } catch (InterruptedException e) {
                logger.error("Payday traffic simulation interrupted", e);
                route.abort();
            }
        });
        
        // Intercept transaction authorization APIs
        page.route("**/api/transactions/authorize/**", route -> {
            try {
                int delay = calculateAuthorizationDelay();
                logger.debug("Authorization delay: {}ms", delay);
                Thread.sleep(delay);
                route.resume();
                
            } catch (InterruptedException e) {
                logger.error("Authorization simulation interrupted", e);
                route.abort();
            }
        });
        
        // Intercept balance check APIs
        page.route("**/api/balance/**", route -> {
            try {
                int delay = calculateBalanceCheckDelay();
                logger.debug("Balance check delay: {}ms", delay);
                Thread.sleep(delay);
                route.resume();
                
            } catch (InterruptedException e) {
                logger.error("Balance check simulation interrupted", e);
                route.abort();
            }
        });
    }
    
    /**
     * Calculate delay for payment processing during payday
     */
    private static int calculatePaydayDelay() {
        return BASE_DELAY_MS + ThreadLocalRandom.current().nextInt(0, PEAK_DELAY_VARIATION);
    }
    
    /**
     * Calculate delay for transaction authorization
     */
    private static int calculateAuthorizationDelay() {
        return (BASE_DELAY_MS / 2) + ThreadLocalRandom.current().nextInt(200, 800);
    }
    
    /**
     * Calculate delay for balance checks
     */
    private static int calculateBalanceCheckDelay() {
        return (BASE_DELAY_MS / 3) + ThreadLocalRandom.current().nextInt(100, 400);
    }
    
    /**
     * Determine if we should simulate a failure based on current load
     */
    private static boolean shouldSimulateFailure() {
        // Higher failure rate during peak 9 AM hour
        double currentFailureRate = isPeakHour() ? PEAK_FAILURE_RATE : FAILURE_RATE;
        return ThreadLocalRandom.current().nextDouble() < currentFailureRate;
    }
    
    /**
     * Check if it's currently peak hour (9:00 AM)
     */
    private static boolean isPeakHour() {
        int currentHour = java.time.LocalTime.now().getHour();
        return currentHour >= 9 && currentHour < 10;
    }
    
    /**
     * Apply extreme stress conditions for load testing
     */
    public static void applyExtremePaydayStress(Page page) {
        logger.info("Applying extreme Payday Friday stress conditions");
        
        page.route("**/api/payments/**", route -> {
            try {
                // Extreme delay conditions
                int extremeDelay = BASE_DELAY_MS * 2 + ThreadLocalRandom.current().nextInt(1000, 3000);
                logger.debug("Extreme stress delay: {}ms", extremeDelay);
                Thread.sleep(extremeDelay);
                
                // Higher failure rate during extreme stress
                if (ThreadLocalRandom.current().nextDouble() < 0.10) { // 10% failure rate
                    route.fulfill(new Route.FulfillOptions()
                        .setStatus(503)
                        .setBody("{\"error\":\"Service temporarily unavailable - high load\"}"));
                    return;
                }
                
                route.resume();
                
            } catch (InterruptedException e) {
                logger.error("Extreme stress simulation interrupted", e);
                route.abort();
            }
        });
    }
    
    /**
     * Clear all traffic simulations
     */
    public static void clearTrafficSimulation(Page page) {
        logger.info("Clearing Payday Friday traffic simulation");
        page.unroute("**/api/payments/**");
        page.unroute("**/api/transactions/authorize/**");
        page.unroute("**/api/balance/**");
    }
}
