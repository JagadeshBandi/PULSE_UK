package uk.pulse.interceptors;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Mobile Traffic Simulator for Android and iOS Applications
 * Simulates realistic mobile network conditions and app performance
 */
public class MobileTrafficSimulator {
    private static final Logger logger = LoggerFactory.getLogger(MobileTrafficSimulator.class);
    
    // Mobile-specific constants
    private static final int MOBILE_BASE_DELAY = 800;  // Base mobile latency
    private static final int APP_STARTUP_DELAY = 2000;  // App startup time
    private static final int TOUCH_DELAY = 100;  // Touch interaction delay
    private static final double MOBILE_FAILURE_RATE = 0.08;  // 8% failure rate on mobile
    
    /**
     * Simulate mobile app startup and initial load
     */
    public static void simulateMobileAppStartup(Page page) {
        logger.info("Simulating mobile app startup");
        
        // Simulate app loading time
        page.route("**/api/app/init/**", route -> {
            try {
                Thread.sleep(APP_STARTUP_DELAY + ThreadLocalRandom.current().nextInt(500, 1500));
                route.resume();
            } catch (InterruptedException e) {
                logger.error("App startup simulation interrupted", e);
                route.abort();
            }
        });
        
        // Simulate initial data loading
        page.route("**/api/data/load/**", route -> {
            try {
                Thread.sleep(MOBILE_BASE_DELAY + ThreadLocalRandom.current().nextInt(300, 800));
                route.resume();
            } catch (InterruptedException e) {
                logger.error("Data loading simulation interrupted", e);
                route.abort();
            }
        });
    }
    
    /**
     * Simulate mobile payment processing with touch interactions
     */
    public static void simulateMobilePaymentProcessing(Page page) {
        logger.info("Simulating mobile payment processing");
        
        // Payment API with mobile-specific delays
        page.route("**/api/payments/mobile/**", route -> {
            try {
                int mobileDelay = MOBILE_BASE_DELAY + TOUCH_DELAY + 
                                ThreadLocalRandom.current().nextInt(500, 2000);
                
                // Simulate occasional mobile failures
                if (ThreadLocalRandom.current().nextDouble() < MOBILE_FAILURE_RATE) {
                    logger.warn("Simulating mobile payment failure");
                    route.fulfill(new Route.FulfillOptions()
                        .setStatus(500)
                        .setBody("{\"error\":\"Mobile payment failed - poor connection\"}"));
                    return;
                }
                
                Thread.sleep(mobileDelay);
                route.resume();
                
            } catch (InterruptedException e) {
                logger.error("Mobile payment simulation interrupted", e);
                route.abort();
            }
        });
        
        // Mobile authorization with biometric/fingerprint delays
        page.route("**/api/auth/mobile/biometric/**", route -> {
            try {
                Thread.sleep(1500 + ThreadLocalRandom.current().nextInt(500, 1500));
                route.resume();
            } catch (InterruptedException e) {
                logger.error("Biometric auth simulation interrupted", e);
                route.abort();
            }
        });
    }
    
    /**
     * Simulate mobile network conditions (3G, 4G, WiFi)
     */
    public static void simulateMobileNetworkConditions(Page page, String networkType) {
        logger.info("Simulating {} network conditions", networkType);
        
        int baseDelay;
        double failureRate;
        
        switch (networkType.toLowerCase()) {
            case "3g":
                baseDelay = 1500;
                failureRate = 0.12;
                break;
            case "4g":
                baseDelay = 600;
                failureRate = 0.05;
                break;
            case "wifi":
                baseDelay = 200;
                failureRate = 0.02;
                break;
            case "poor":
                baseDelay = 3000;
                failureRate = 0.20;
                break;
            default:
                baseDelay = MOBILE_BASE_DELAY;
                failureRate = MOBILE_FAILURE_RATE;
        }
        
        page.route("**/api/**", route -> {
            try {
                // Add network-specific delay
                int delay = baseDelay + ThreadLocalRandom.current().nextInt(-200, 800);
                if (delay > 0) {
                    Thread.sleep(delay);
                }
                
                // Simulate network failures
                if (ThreadLocalRandom.current().nextDouble() < failureRate) {
                    route.fulfill(new Route.FulfillOptions()
                        .setStatus(503)
                        .setBody("{\"error\":\"Network unavailable\"}"));
                    return;
                }
                
                route.resume();
                
            } catch (InterruptedException e) {
                logger.error("Network simulation interrupted", e);
                route.abort();
            }
        });
    }
    
    /**
     * Simulate mobile app background/foreground transitions
     */
    public static void simulateAppLifecycle(Page page) {
        logger.info("Simulating mobile app lifecycle");
        
        // Background mode - reduced network activity
        page.route("**/api/sync/**", route -> {
            try {
                Thread.sleep(2000 + ThreadLocalRandom.current().nextInt(1000, 3000));
                route.resume();
            } catch (InterruptedException e) {
                logger.error("Background sync simulation interrupted", e);
                route.abort();
            }
        });
    }
    
    /**
     * Simulate mobile-specific payment scenarios
     */
    public static void simulateMobilePaydayScenario(Page page) {
        logger.info("Simulating mobile Payday Friday scenario");
        
        // High mobile traffic during payday
        page.route("**/api/payments/**", route -> {
            try {
                // Increased delay due to mobile network congestion
                int paydayDelay = MOBILE_BASE_DELAY * 2 + TOUCH_DELAY + 
                                ThreadLocalRandom.current().nextInt(1000, 3000);
                
                // Higher failure rate during mobile peak hours
                double peakFailureRate = MOBILE_FAILURE_RATE * 1.5;
                if (ThreadLocalRandom.current().nextDouble() < peakFailureRate) {
                    route.fulfill(new Route.FulfillOptions()
                        .setStatus(503)
                        .setBody("{\"error\":\"Service unavailable - high mobile traffic\"}"));
                    return;
                }
                
                Thread.sleep(paydayDelay);
                route.resume();
                
            } catch (InterruptedException e) {
                logger.error("Mobile payday simulation interrupted", e);
                route.abort();
            }
        });
        
        // Mobile balance check with caching delays
        page.route("**/api/balance/**", route -> {
            try {
                Thread.sleep(MOBILE_BASE_DELAY + ThreadLocalRandom.current().nextInt(200, 600));
                route.resume();
            } catch (InterruptedException e) {
                logger.error("Balance check simulation interrupted", e);
                route.abort();
            }
        });
    }
    
    /**
     * Simulate touch interaction delays
     */
    public static void simulateTouchInteractions(Page page) {
        logger.info("Adding touch interaction delays");
        
        // Add delay to all interactions that simulate touch
        page.addInitScript("() => {" +
            "window.originalSetTimeout = window.setTimeout;" +
            "window.setTimeout = function(callback, delay) {" +
            "return window.originalSetTimeout(callback, delay + " + TOUCH_DELAY + ");" +
            "};" +
            "}");
    }
    
    /**
     * Simulate mobile battery saving mode
     */
    public static void simulateBatterySavingMode(Page page) {
        logger.info("Simulating battery saving mode");
        
        // Slower network requests in battery saving mode
        page.route("**/api/**", route -> {
            try {
                Thread.sleep(MOBILE_BASE_DELAY * 2 + ThreadLocalRandom.current().nextInt(500, 1500));
                route.resume();
            } catch (InterruptedException e) {
                logger.error("Battery saving mode simulation interrupted", e);
                route.abort();
            }
        });
    }
    
    /**
     * Clear all mobile simulations
     */
    public static void clearMobileSimulations(Page page) {
        logger.info("Clearing mobile simulations");
        page.unroute("**/api/**");
    }
}
