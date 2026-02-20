# Real App Testing Guide for Play Store and App Store

## Overview

This guide explains how to test real mobile applications from Google Play Store and Apple App Store using the Pulse-UK framework for Payday Friday traffic testing.

## Prerequisites

### For Android (Play Store) Testing:
1. **Android Device or Emulator**: Physical Android device or Android Studio emulator
2. **Android Debug Bridge (ADB)**: Installed and configured
3. **Appium Server**: For mobile app automation (optional for advanced testing)
4. **Real App Installation**: Target banking/retail apps installed on device

### For iOS (App Store) Testing:
1. **iOS Device or Simulator**: Physical iPhone/iPad or Xcode simulator
2. **Xcode**: Latest version with iOS simulator
3. **iOS WebKit Debugging**: Enabled for Safari testing
4. **Real App Installation**: Target banking/retail apps installed on device

## Supported UK Banking Apps

### Android (Play Store):
- **Barclays Mobile Banking**: `com.barclays.android.barclays`
- **HSBC UK Mobile Banking**: `com.hsbc.uk.mobilebanking`
- **NatWest Mobile Banking**: `com.natwest.mobilebank`
- **Monzo Mobile Banking**: `com.monzo.bank`
- **Starling Bank**: `com.starlingbank.android`
- **Lloyds Bank Mobile Banking**: `com.lloyds.mobilebank`

### iOS (App Store):
- **HSBC UK Mobile Banking**: `hsbc-uk-mobile-banking`
- **Lloyds Bank Mobile Banking**: `lloyds-bank-mobile-banking`
- **Barclays Mobile Banking**: `barclays-mobile-banking`
- **NatWest Mobile Banking**: `natwest-mobile-banking`
- **Monzo Mobile Banking**: `monzo-mobile-banking`
- **Starling Bank**: `starling-bank`

## Supported UK Retail Apps

### Android (Play Store):
- **Amazon Shopping**: `com.amazon.mShop.android.shopping`
- **Tesco Grocery**: `com.tesco.grocery.view`
- **Sainsbury's Groceries**: `com.sainsburys.groceries`
- **Asda Groceries**: `com.asda.grocery`
- **Morrisons Groceries**: `com.morrisons.loyalty`

### iOS (App Store):
- **Amazon Shopping**: `amazon-shopping`
- **Tesco Grocery**: `tesco-grocery`
- **Sainsbury's Groceries**: `sainsburys-groceries`
- **Asda Groceries**: `asda-grocery`
- **Morrisons**: `morrisons`

## Testing Approaches

### 1. Web-Based Testing (Current Implementation)
The current implementation simulates real app behavior through web browsers with mobile device profiles.

**Pros:**
- No device setup required
- Fast execution
- Easy to run in CI/CD
- Good for initial testing

**Cons:**
- Not testing actual app UI
- Limited to web-based interactions
- May not reflect real app performance

### 2. Hybrid Testing (Recommended)
Combine web-based simulation with real device testing for comprehensive coverage.

### 3. Full Device Testing (Advanced)
Use Appium or similar tools to test actual mobile applications.

## Implementation Steps

### Step 1: Setup Real App Testing Environment

```bash
# Install Appium for mobile app automation (optional)
npm install -g appium

# Start Appium server
appium --port 4723

# Verify device connection
adb devices  # Android
xcrun simctl list devices  # iOS
```

### Step 2: Configure Real App Testing

Update your test configuration to target real apps:

```java
// Example configuration for real app testing
RealAppBrowserFactory.AppConfiguration config = new RealAppBrowserFactory.AppConfiguration(
    "Barclays Mobile Banking",
    "android",
    "5.2.1",
    Map.of("X-App-Version", "5.2.1"),
    new String[]{"geolocation", "camera", "contacts"}
);
```

### Step 3: Run Real App Tests

```bash
# Run all real app tests
mvn test -Dcucumber.filter.tags="@RealApp"

# Run specific platform tests
mvn test -Dcucumber.filter.tags="@RealApp && @PlayStore"
mvn test -Dcucumber.filter.tags="@RealApp && @AppStore"

# Run specific app tests
mvn test -Dcucumber.filter.tags="@RealApp && @Barclays"
mvn test -Dcucumber.filter.tags="@RealApp && @HSBC"
```

## Real App Testing Scenarios

### Banking App Scenarios:
1. **App Startup Performance**: Measure app loading time during peak hours
2. **Login Performance**: Test authentication under stress
3. **Balance Check**: Verify balance retrieval performance
4. **Payment Processing**: Test payment completion during high traffic
5. **Transaction History**: Test transaction loading performance

### Retail App Scenarios:
1. **Product Search**: Test search performance during peak shopping
2. **Product Loading**: Verify product details loading speed
3. **Cart Operations**: Test add to cart performance
4. **Checkout Process**: Test checkout completion time
5. **Payment Integration**: Test payment gateway performance

## Network Simulation for Real Apps

### Mobile Network Conditions:
- **5G**: 100ms base delay, 1% failure rate
- **4G**: 300ms base delay, 2% failure rate
- **3G**: 800ms base delay, 5% failure rate
- **WiFi**: 150ms base delay, 1% failure rate
- **Poor Signal**: 2000ms base delay, 15% failure rate

### Real App Performance Metrics:
- **API Response Time**: Measure actual API call performance
- **Image Loading Time**: Track media loading performance
- **App Startup Time**: Measure app initialization time
- **Memory Usage**: Monitor app memory consumption
- **Battery Impact**: Assess battery usage during testing

## Advanced Real App Testing

### 1. Device Farm Integration
```java
// Example for device farm integration
public class DeviceFarmManager {
    public List<Device> getAvailableDevices() {
        // Connect to device farm (BrowserStack, Sauce Labs, etc.)
        return deviceFarm.getDevices();
    }
    
    public void runTestsOnDevice(Device device, String appPackage) {
        // Execute tests on specific device
        BrowserContext context = createDeviceContext(device, appPackage);
        // Run test scenarios
    }
}
```

### 2. Real API Integration
```java
// Example for real API testing
public class RealAPITester {
    public void testBankingAPI(String apiKey, String endpoint) {
        // Test actual banking API endpoints
        HttpResponse response = HttpClient.sendRequest(endpoint, apiKey);
        // Validate response time and content
    }
}
```

### 3. Performance Monitoring
```java
// Example for performance monitoring
public class AppPerformanceMonitor {
    public void monitorAppPerformance(String appPackage) {
        // Monitor CPU, memory, battery usage
        DeviceMetrics metrics = DeviceMonitor.getMetrics(appPackage);
        // Record metrics to InfluxDB/Grafana
    }
}
```

## Best Practices

### 1. Test Environment Setup
- Use dedicated test devices
- Clear app cache before each test
- Ensure consistent network conditions
- Monitor device resources during testing

### 2. Data Management
- Use test accounts only
- Reset app data between tests
- Avoid production data contamination
- Use mock payment methods for testing

### 3. Error Handling
- Implement proper error recovery
- Log detailed error information
- Capture screenshots on failures
- Monitor app crash reports

### 4. Performance Optimization
- Test during different times of day
- Monitor network latency impact
- Test with different device loads
- Optimize test execution time

## Troubleshooting

### Common Issues:

1. **App Not Found**: Ensure app is installed and package ID is correct
2. **Permission Denied**: Check app permissions and device settings
3. **Network Timeout**: Verify network connectivity and app server status
4. **Device Not Responding**: Restart device and check ADB connection
5. **App Crashes**: Check app logs and crash reports

### Debug Commands:
```bash
# Android debugging
adb logcat | grep "com.barclays.android.barclays"
adb shell dumpsys meminfo com.barclays.android.barclays

# iOS debugging
xcrun simctl spawn booted log stream --predicate 'process == "Barclays"'
xcrun simctl spawn booted debug --view
```

## Integration with CI/CD

### GitHub Actions Example:
```yaml
name: Real App Testing
on: [push, pull_request]
jobs:
  real-app-tests:
    runs-on: macos-latest
    steps:
      - uses: actions/checkout@v3
      - name: Setup Android Emulator
        run: |
          echo "y" | $ANDROID_HOME/tools/bin/sdkmanager --install "system-images;android-30;google_apis;x86"
          echo "no" | $ANDROID_HOME/tools/bin/avdmanager create avd -n test -d "system-images;android-30;google_apis;x86"
          $ANDROID_HOME/emulator/emulator -avd test -no-audio -no-window &
      - name: Install Test Apps
        run: |
          adb install apps/barclays.apk
          adb install apps/hsbc.apk
      - name: Run Real App Tests
        run: mvn test -Dcucumber.filter.tags="@RealApp"
```

## Reporting and Analytics

### Metrics to Track:
- **App Startup Time**: Time taken to launch app
- **API Response Time**: Average API call duration
- **Success Rate**: Percentage of successful operations
- **Error Rate**: Percentage of failed operations
- **Device Performance**: CPU, memory, battery usage

### Grafana Dashboard:
- Real-time app performance metrics
- Historical performance trends
- Device-specific performance data
- Network condition impact analysis

## Security Considerations

### Data Protection:
- Use test credentials only
- Never store real user data
- Encrypt sensitive test data
- Follow GDPR compliance guidelines

### API Security:
- Use test API endpoints
- Rotate test API keys regularly
- Monitor API usage during testing
- Implement rate limiting for test APIs

## Conclusion

Real app testing provides the most accurate assessment of mobile application performance during Payday Friday traffic scenarios. While it requires more setup and resources than web-based testing, it delivers the most realistic and valuable insights into actual user experience.

The Pulse-UK framework supports both web-based simulation and real app testing, allowing you to choose the approach that best fits your testing requirements and resources.
