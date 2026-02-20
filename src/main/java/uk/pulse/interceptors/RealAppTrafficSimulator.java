package uk.pulse.interceptors;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Real App Traffic Simulator for Play Store and App Store Applications
 * Simulates realistic network conditions for real mobile applications
 */
public class RealAppTrafficSimulator {
    private static final Logger logger = LoggerFactory.getLogger(RealAppTrafficSimulator.class);
    
    // Real app network constants
    private static final int REAL_APP_BASE_DELAY = 400;  // Base real app latency
    private static final int API_CALL_DELAY = 800;  // API call delay
    private static final int IMAGE_LOAD_DELAY = 1200;  // Image loading delay
    private static final double REAL_APP_FAILURE_RATE = 0.03;  // 3% failure rate
    
    /**
     * Simulate real app startup and initial loading
     */
    public static void simulateRealAppStartup(Page page, String appName) {
        logger.info("Simulating real app startup for: {}", appName);
        
        // Simulate app initialization
        page.route("**/api/app/init", route -> {
            try {
                Thread.sleep(REAL_APP_BASE_DELAY + ThreadLocalRandom.current().nextInt(200, 800));
                route.resume();
            } catch (InterruptedException e) {
                logger.error("App startup simulation interrupted", e);
                route.abort();
            }
        });
        
        // Simulate user data loading
        page.route("**/api/user/profile", route -> {
            try {
                Thread.sleep(API_CALL_DELAY + ThreadLocalRandom.current().nextInt(300, 1000));
                route.resume();
            } catch (InterruptedException e) {
                logger.error("User data loading interrupted", e);
                route.abort();
            }
        });
        
        // Simulate configuration loading
        page.route("**/api/app/config", route -> {
            try {
                Thread.sleep(REAL_APP_BASE_DELAY + ThreadLocalRandom.current().nextInt(100, 500));
                route.resume();
            } catch (InterruptedException e) {
                logger.error("Config loading interrupted", e);
                route.abort();
            }
        });
    }
    
    /**
     * Simulate real banking app API calls
     */
    public static void simulateRealBankingApp(Page page, String bankName) {
        logger.info("Simulating real banking app API calls for: {}", bankName);
        
        // Banking API endpoints
        page.route("**/api/banking/accounts", route -> {
            try {
                Thread.sleep(API_CALL_DELAY + ThreadLocalRandom.current().nextInt(400, 1200));
                route.resume();
            } catch (InterruptedException e) {
                logger.error("Accounts API interrupted", e);
                route.abort();
            }
        });
        
        // Balance check API
        page.route("**/api/banking/balance", route -> {
            try {
                Thread.sleep(API_CALL_DELAY + ThreadLocalRandom.current().nextInt(200, 600));
                route.resume();
            } catch (InterruptedException e) {
                logger.error("Balance check interrupted", e);
                route.abort();
            }
        });
        
        // Transaction history
        page.route("**/api/banking/transactions", route -> {
            try {
                Thread.sleep(API_CALL_DELAY + ThreadLocalRandom.current().nextInt(500, 1500));
                route.resume();
            } catch (InterruptedException e) {
                logger.error("Transaction history interrupted", e);
                route.abort();
            }
        });
        
        // Payment processing
        page.route("**/api/banking/payment", route -> {
            try {
                int paymentDelay = API_CALL_DELAY * 2 + ThreadLocalRandom.current().nextInt(1000, 3000);
                
                // Simulate occasional payment failures
                if (ThreadLocalRandom.current().nextDouble() < REAL_APP_FAILURE_RATE) {
                    route.fulfill(new Route.FulfillOptions()
                        .setStatus(503)
                        .setBody("{\"error\":\"Payment service temporarily unavailable\"}"));
                    return;
                }
                
                Thread.sleep(paymentDelay);
                route.resume();
                
            } catch (InterruptedException e) {
                logger.error("Payment processing interrupted", e);
                route.abort();
            }
        });
        
        // Transfer API
        page.route("**/api/banking/transfer", route -> {
            try {
                Thread.sleep(API_CALL_DELAY * 2 + ThreadLocalRandom.current().nextInt(800, 2000));
                route.resume();
            } catch (InterruptedException e) {
                logger.error("Transfer API interrupted", e);
                route.abort();
            }
        });
    }
    
    /**
     * Simulate real retail app API calls
     */
    public static void simulateRealRetailApp(Page page, String retailName) {
        logger.info("Simulating real retail app API calls for: {}", retailName);
        
        // Product catalog
        page.route("**/api/products", route -> {
            try {
                Thread.sleep(API_CALL_DELAY + ThreadLocalRandom.current().nextInt(300, 800));
                route.resume();
            } catch (InterruptedException e) {
                logger.error("Product catalog interrupted", e);
                route.abort();
            }
        });
        
        // Product details
        page.route("**/api/products/*", route -> {
            try {
                Thread.sleep(API_CALL_DELAY + ThreadLocalRandom.current().nextInt(200, 600));
                route.resume();
            } catch (InterruptedException e) {
                logger.error("Product details interrupted", e);
                route.abort();
            }
        });
        
        // Shopping cart
        page.route("**/api/cart", route -> {
            try {
                Thread.sleep(API_CALL_DELAY + ThreadLocalRandom.current().nextInt(400, 1000));
                route.resume();
            } catch (InterruptedException e) {
                logger.error("Shopping cart interrupted", e);
                route.abort();
            }
        });
        
        // Checkout process
        page.route("**/api/checkout", route -> {
            try {
                int checkoutDelay = API_CALL_DELAY * 3 + ThreadLocalRandom.current().nextInt(1000, 3000);
                Thread.sleep(checkoutDelay);
                route.resume();
            } catch (InterruptedException e) {
                logger.error("Checkout process interrupted", e);
                route.abort();
            }
        });
    }
    
    /**
     * Simulate real app network conditions (4G, 5G, WiFi)
     */
    public static void simulateRealAppNetworkConditions(Page page, String networkType) {
        logger.info("Simulating real app network conditions: {}", networkType);
        
        int baseDelay;
        double failureRate;
        
        switch (networkType.toLowerCase()) {
            case "5g":
                baseDelay = 100;
                failureRate = 0.01;
                break;
            case "4g":
                baseDelay = 300;
                failureRate = 0.02;
                break;
            case "3g":
                baseDelay = 800;
                failureRate = 0.05;
                break;
            case "wifi":
                baseDelay = 150;
                failureRate = 0.01;
                break;
            case "poor":
                baseDelay = 2000;
                failureRate = 0.15;
                break;
            default:
                baseDelay = REAL_APP_BASE_DELAY;
                failureRate = REAL_APP_FAILURE_RATE;
        }
        
        page.route("**/api/**", route -> {
            try {
                // Add network-specific delay
                int delay = baseDelay + ThreadLocalRandom.current().nextInt(-100, 500);
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
     * Simulate real app image and media loading
     */
    public static void simulateRealAppMediaLoading(Page page) {
        logger.info("Simulating real app media loading");
        
        // Image loading
        page.route("**/*.jpg", route -> {
            try {
                Thread.sleep(IMAGE_LOAD_DELAY + ThreadLocalRandom.current().nextInt(200, 800));
                route.resume();
            } catch (InterruptedException e) {
                logger.error("Image loading interrupted", e);
                route.abort();
            }
        });
        
        // PNG loading
        page.route("**/*.png", route -> {
            try {
                Thread.sleep(IMAGE_LOAD_DELAY + ThreadLocalRandom.current().nextInt(100, 600));
                route.resume();
            } catch (InterruptedException e) {
                logger.error("PNG loading interrupted", e);
                route.abort();
            }
        });
        
        // Video loading
        page.route("**/*.mp4", route -> {
            try {
                Thread.sleep(IMAGE_LOAD_DELAY * 2 + ThreadLocalRandom.current().nextInt(500, 1500));
                route.resume();
            } catch (InterruptedException e) {
                logger.error("Video loading interrupted", e);
                route.abort();
            }
        });
    }
    
    /**
     * Simulate real app caching behavior
     */
    public static void simulateRealAppCaching(Page page) {
        logger.info("Simulating real app caching behavior");
        
        // Simulate cache hits (faster responses)
        page.route("**/api/cached/**", route -> {
            try {
                // Cache hits are much faster
                Thread.sleep(50 + ThreadLocalRandom.current().nextInt(50, 200));
                route.resume();
            } catch (InterruptedException e) {
                logger.error("Cache simulation interrupted", e);
                route.abort();
            }
        });
        
        // Simulate cache misses (slower responses)
        page.route("**/api/uncached/**", route -> {
            try {
                Thread.sleep(API_CALL_DELAY + ThreadLocalRandom.current().nextInt(500, 1500));
                route.resume();
            } catch (InterruptedException e) {
                logger.error("Cache miss simulation interrupted", e);
                route.abort();
            }
        });
    }
    
    /**
     * Simulate real app background sync
     */
    public static void simulateRealAppBackgroundSync(Page page) {
        logger.info("Simulating real app background sync");
        
        // Background sync operations
        page.route("**/api/sync/**", route -> {
            try {
                Thread.sleep(REAL_APP_BASE_DELAY * 3 + ThreadLocalRandom.current().nextInt(1000, 3000));
                route.resume();
            } catch (InterruptedException e) {
                logger.error("Background sync interrupted", e);
                route.abort();
            }
        });
    }
    
    /**
     * Simulate real app push notifications
     */
    public static void simulateRealAppPushNotifications(Page page) {
        logger.info("Simulating real app push notifications");
        
        // Push notification registration
        page.route("**/api/notifications/register", route -> {
            try {
                Thread.sleep(API_CALL_DELAY + ThreadLocalRandom.current().nextInt(200, 600));
                route.resume();
            } catch (InterruptedException e) {
                logger.error("Push notification registration interrupted", e);
                route.abort();
            }
        });
        
        // Notification fetching
        page.route("**/api/notifications/fetch", route -> {
            try {
                Thread.sleep(API_CALL_DELAY + ThreadLocalRandom.current().nextInt(100, 400));
                route.resume();
            } catch (InterruptedException e) {
                logger.error("Notification fetching interrupted", e);
                route.abort();
            }
        });
    }
    
    /**
     * Simulate real app analytics and tracking
     */
    public static void simulateRealAppAnalytics(Page page) {
        logger.info("Simulating real app analytics and tracking");
        
        // Analytics events
        page.route("**/api/analytics/track", route -> {
            try {
                // Analytics should be very fast to not impact user experience
                Thread.sleep(50 + ThreadLocalRandom.current().nextInt(50, 200));
                route.resume();
            } catch (InterruptedException e) {
                logger.error("Analytics tracking interrupted", e);
                route.abort();
            }
        });
        
        // Crash reporting
        page.route("**/api/crash/report", route -> {
            try {
                Thread.sleep(API_CALL_DELAY + ThreadLocalRandom.current().nextInt(200, 500));
                route.resume();
            } catch (InterruptedException e) {
                logger.error("Crash reporting interrupted", e);
                route.abort();
            }
        });
    }
    
    /**
     * Clear all real app simulations
     */
    public static void clearRealAppSimulations(Page page) {
        logger.info("Clearing real app simulations");
        page.unroute("**/api/**");
        page.unroute("**/*.jpg");
        page.unroute("**/*.png");
        page.unroute("**/*.mp4");
    }
}
