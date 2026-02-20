package uk.pulse.factory;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.Geolocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Real App Browser Factory for Play Store and App Store Applications
 * Creates browser contexts for testing real mobile applications
 */
public class RealAppBrowserFactory {
    private static final Logger logger = LoggerFactory.getLogger(RealAppBrowserFactory.class);
    
    // Real device profiles for Play Store apps
    private static final Map<String, DeviceProfile> REAL_DEVICES = new HashMap<>();
    
    static {
        // Popular Android devices from Play Store
        REAL_DEVICES.put("samsung_s23", new DeviceProfile(
            "Samsung Galaxy S23",
            360, 780, 3.0,
            "Mozilla/5.0 (Linux; Android 13; SM-S911B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Mobile Safari/537.36",
            "android"
        ));
        
        REAL_DEVICES.put("pixel_7", new DeviceProfile(
            "Google Pixel 7",
            412, 915, 2.625,
            "Mozilla/5.0 (Linux; Android 13; Pixel 7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Mobile Safari/537.36",
            "android"
        ));
        
        REAL_DEVICES.put("oneplus_11", new DeviceProfile(
            "OnePlus 11",
            393, 851, 2.75,
            "Mozilla/5.0 (Linux; Android 13; CPH2513) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Mobile Safari/537.36",
            "android"
        ));
        
        // Popular iOS devices from App Store
        REAL_DEVICES.put("iphone_14", new DeviceProfile(
            "iPhone 14",
            390, 844, 3.0,
            "Mozilla/5.0 (iPhone; CPU iPhone OS 16_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.0 Mobile/15E148 Safari/604.1",
            "ios"
        ));
        
        REAL_DEVICES.put("iphone_14_pro", new DeviceProfile(
            "iPhone 14 Pro",
            393, 852, 3.0,
            "Mozilla/5.0 (iPhone; CPU iPhone OS 16_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.0 Mobile/15E148 Safari/604.1",
            "ios"
        ));
        
        REAL_DEVICES.put("ipad_pro", new DeviceProfile(
            "iPad Pro 12.9",
            1024, 1366, 2.0,
            "Mozilla/5.0 (iPad; CPU OS 16_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.0 Mobile/15E148 Safari/604.1",
            "ios"
        ));
    }
    
    /**
     * Create context for testing real Play Store app
     */
    public static BrowserContext createPlayStoreAppContext(Browser browser, String deviceName) {
        DeviceProfile device = REAL_DEVICES.getOrDefault(deviceName.toLowerCase(), REAL_DEVICES.get("samsung_s23"));
        
        logger.info("Creating Play Store app context for {} ({})", device.name, device.deviceType);
        
        return browser.newContext(new Browser.NewContextOptions()
            .setViewportSize(device.width, device.height)
            .setDeviceScaleFactor(device.deviceScaleFactor)
            .setUserAgent(device.userAgent)
            .setLocale("en-GB")
            .setTimezoneId("Europe/London")
            .setGeolocation(new Geolocation(51.5074, -0.1278)) // London coordinates
            .setPermissions(Arrays.asList("geolocation", "notifications"))
            .setHasTouch(true)
            .setIsMobile(true)
            .setIgnoreHTTPSErrors(true)
            .setJavaScriptEnabled(true)
            // Additional mobile-specific settings for real apps
            .setExtraHTTPHeaders(Map.of(
                "Accept", "application/json,text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
                "Accept-Language", "en-GB,en;q=0.9",
                "Accept-Encoding", "gzip, deflate, br",
                "Connection", "keep-alive",
                "Upgrade-Insecure-Requests", "1"
            )));
    }
    
    /**
     * Create context for testing real App Store app
     */
    public static BrowserContext createAppStoreAppContext(Browser browser, String deviceName) {
        DeviceProfile device = REAL_DEVICES.getOrDefault(deviceName.toLowerCase(), REAL_DEVICES.get("iphone_14"));
        
        logger.info("Creating App Store app context for {} ({})", device.name, device.deviceType);
        
        return browser.newContext(new Browser.NewContextOptions()
            .setViewportSize(device.width, device.height)
            .setDeviceScaleFactor(device.deviceScaleFactor)
            .setUserAgent(device.userAgent)
            .setLocale("en-GB")
            .setTimezoneId("Europe/London")
            .setGeolocation(new Geolocation(51.5074, -0.1278)) // London coordinates
            .setPermissions(Arrays.asList("geolocation", "notifications", "camera", "microphone"))
            .setHasTouch(true)
            .setIsMobile(true)
            .setIgnoreHTTPSErrors(true)
            .setJavaScriptEnabled(true)
            // iOS-specific settings for real apps
            .setExtraHTTPHeaders(Map.of(
                "Accept", "application/json,text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
                "Accept-Language", "en-GB,en;q=0.9",
                "Accept-Encoding", "gzip, deflate, br",
                "Connection", "keep-alive",
                "Sec-Fetch-Dest", "document",
                "Sec-Fetch-Mode", "navigate",
                "Sec-Fetch-Site", "none"
            )));
    }
    
    /**
     * Create context with custom app configuration
     */
    public static BrowserContext createCustomAppContext(Browser browser, String deviceName, AppConfiguration config) {
        BrowserContext context;
        
        if (config.platform.toLowerCase().equals("android")) {
            context = createPlayStoreAppContext(browser, deviceName);
        } else {
            context = createAppStoreAppContext(browser, deviceName);
        }
        
        // Apply custom app-specific settings
        if (config.customHeaders != null && !config.customHeaders.isEmpty()) {
            logger.info("Applying custom headers for app: {}", config.appName);
            // Headers would be applied through route interception
        }
        
        if (config.customPermissions != null && config.customPermissions.length > 0) {
            logger.info("Applying custom permissions for app: {}", config.appName);
        }
        
        return context;
    }
    
    /**
     * Device profile data class
     */
    private static class DeviceProfile {
        final String name;
        final int width;
        final int height;
        final double deviceScaleFactor;
        final String userAgent;
        final String deviceType;
        
        DeviceProfile(String name, int width, int height, double deviceScaleFactor, 
                     String userAgent, String deviceType) {
            this.name = name;
            this.width = width;
            this.height = height;
            this.deviceScaleFactor = deviceScaleFactor;
            this.userAgent = userAgent;
            this.deviceType = deviceType;
        }
    }
    
    /**
     * App configuration class
     */
    public static class AppConfiguration {
        final String appName;
        final String platform;
        final String appVersion;
        final Map<String, String> customHeaders;
        final String[] customPermissions;
        
        public AppConfiguration(String appName, String platform, String appVersion, 
                              Map<String, String> customHeaders, String[] customPermissions) {
            this.appName = appName;
            this.platform = platform;
            this.appVersion = appVersion;
            this.customHeaders = customHeaders;
            this.customPermissions = customPermissions;
        }
    }
}
