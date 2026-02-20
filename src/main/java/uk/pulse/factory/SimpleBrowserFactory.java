package uk.pulse.factory;

import com.microsoft.playwright.*;

/**
 * Simple Browser Factory for Payday Friday Traffic Testing
 * Creates basic browser contexts for payment traffic simulation
 */
public class SimpleBrowserFactory {
    
    /**
     * Create mobile browser context for UK testing
     */
    public static BrowserContext createMobileContext(Browser browser) {
        return browser.newContext(new Browser.NewContextOptions()
            .setViewportSize(390, 844)
            .setDeviceScaleFactor(3.0)
            .setUserAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 15_0 like Mac OS X) AppleWebKit/605.1.15")
            .setLocale("en-GB")
            .setTimezoneId("Europe/London")
            .setHasTouch(true)
            .setIsMobile(true));
    }
    
    /**
     * Create desktop browser context
     */
    public static BrowserContext createDesktopContext(Browser browser) {
        return browser.newContext(new Browser.NewContextOptions()
            .setViewportSize(1920, 1080)
            .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36")
            .setLocale("en-GB")
            .setTimezoneId("Europe/London"));
    }
}
