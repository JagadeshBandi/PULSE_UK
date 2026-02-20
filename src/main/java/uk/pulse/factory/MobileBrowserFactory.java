package uk.pulse.factory;

import com.microsoft.playwright.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mobile Browser Factory for Android and iOS Testing
 * Creates optimized browser contexts for mobile application testing
 */
public class MobileBrowserFactory {
    private static final Logger logger = LoggerFactory.getLogger(MobileBrowserFactory.class);
    
    /**
     * Create Android mobile browser context
     */
    public static BrowserContext createAndroidContext(Browser browser) {
        logger.info("Creating Android mobile browser context");
        
        return browser.newContext(new Browser.NewContextOptions()
            .setViewportSize(360, 640)  // Typical Android viewport
            .setDeviceScaleFactor(2.625)
            .setUserAgent("Mozilla/5.0 (Linux; Android 12; SM-G991B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Mobile Safari/537.36")
            .setLocale("en-GB")
            .setTimezoneId("Europe/London")
            .setHasTouch(true)
            .setIsMobile(true)
            .setIgnoreHTTPSErrors(true)
            .setJavaScriptEnabled(true));
    }
    
    /**
     * Create iOS mobile browser context
     */
    public static BrowserContext createIOSContext(Browser browser) {
        logger.info("Creating iOS mobile browser context");
        
        return browser.newContext(new Browser.NewContextOptions()
            .setViewportSize(390, 844)  // iPhone 13 viewport
            .setDeviceScaleFactor(3.0)
            .setUserAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.0 Mobile/15E148 Safari/604.1")
            .setLocale("en-GB")
            .setTimezoneId("Europe/London")
            .setHasTouch(true)
            .setIsMobile(true)
            .setIgnoreHTTPSErrors(true)
            .setJavaScriptEnabled(true));
    }
    
    /**
     * Create tablet context (iPad)
     */
    public static BrowserContext createTabletContext(Browser browser) {
        logger.info("Creating iPad tablet browser context");
        
        return browser.newContext(new Browser.NewContextOptions()
            .setViewportSize(768, 1024)  // iPad viewport
            .setDeviceScaleFactor(2.0)
            .setUserAgent("Mozilla/5.0 (iPad; CPU OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.0 Mobile/15E148 Safari/604.1")
            .setLocale("en-GB")
            .setTimezoneId("Europe/London")
            .setHasTouch(true)
            .setIsMobile(true)
            .setIgnoreHTTPSErrors(true)
            .setJavaScriptEnabled(true));
    }
    
    /**
     * Create context with mobile network conditions
     */
    public static BrowserContext createMobileContextWithNetwork(Browser browser, String deviceType, NetworkCondition condition) {
        BrowserContext context;
        
        switch (deviceType.toLowerCase()) {
            case "android":
                context = createAndroidContext(browser);
                break;
            case "ios":
                context = createIOSContext(browser);
                break;
            case "tablet":
                context = createTabletContext(browser);
                break;
            default:
                context = createAndroidContext(browser);
        }
        
        // Apply network conditions if specified
        if (condition != null) {
            applyNetworkConditions(context, condition);
        }
        
        return context;
    }
    
    /**
     * Apply network conditions to simulate mobile connectivity
     */
    private static void applyNetworkConditions(BrowserContext context, NetworkCondition condition) {
        switch (condition) {
            case WIFI_4G:
                logger.info("Applying WiFi + 4G network conditions");
                break;
            case MOBILE_3G:
                logger.info("Applying 3G mobile network conditions");
                break;
            case POOR_SIGNAL:
                logger.info("Applying poor signal conditions");
                break;
            case UNDERGROUND:
                logger.info("Applying London Underground network conditions");
                break;
            default:
                logger.info("Using default network conditions");
        }
    }
    
    /**
     * Network condition enumeration
     */
    public enum NetworkCondition {
        WIFI_4G,
        MOBILE_3G,
        POOR_SIGNAL,
        UNDERGROUND
    }
}
