# Pulse-UK: Payday Friday Traffic Testing Framework

Enterprise-Grade Load Testing for UK FinTech and Retail Applications

Solving the 'Payday Friday' Glitch that costs UK brands billions in app abandonment during peak salary day traffic.

## Why Pulse-UK?

In the UK market, app stability during Friday 9:00 AM salary payments is critical. Pulse-UK uses Playwright Java to simulate realistic payment traffic conditions:

- **Payday Friday Simulation**: 10k+ concurrent user load testing
- **Payment Traffic Focus**: Specialized in payment processing scenarios
- **Realistic Delays**: Simulates actual server load during peak periods
- **Simple Architecture**: Focused and easy-to-maintain codebase

## Tech Stack

| Component | Technology | Purpose |
|-----------|------------|---------|
| **Engine** | Playwright Java 1.48.0 | Browser automation and network interception |
| **Logic** | Cucumber BDD + TestNG | Business-readable test scenarios |
| **Metrics** | InfluxDB + Grafana | Real-time quality observability and monitoring |
| **Infrastructure** | Docker Compose | One-click setup of complete testing environment |
| **Language** | Java 21 | Latest LTS with performance improvements |

## Quick Start

### Prerequisites
- Java 21+
- Maven 3.8+
- Docker & Docker Compose
- Git

### 1. Clone & Setup
```bash
git clone https://github.com/your-user/pulse-uk-resilience
cd pulse-uk-resilience
```

### 2. Start Infrastructure
```bash
# Start InfluxDB, Grafana, and mock banking app
docker-compose up -d
```

### 3. Install Dependencies
```bash
# Download Playwright browsers
mvn exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"

# Install project dependencies
mvn clean install
```

### 4. Run Payday Friday Tests
```bash
# Run all Payday Friday scenarios (web)
mvn test -Dcucumber.filter.tags="@PaydayFriday"

# Run specific test runner
mvn test -Dtest=PaydayTrafficRunner

# Run mobile app tests
mvn test -Dcucumber.filter.tags="@Mobile"

# Run mobile Android tests
mvn test -Dcucumber.filter.tags="@Mobile && @Android"

# Run mobile iOS tests
mvn test -Dcucumber.filter.tags="@Mobile && @iOS"

# Run real app tests
mvn test -Dcucumber.filter.tags="@RealApp"

# Run Play Store app tests
mvn test -Dcucumber.filter.tags="@RealApp && @PlayStore"

# Run App Store app tests
mvn test -Dcucumber.filter.tags="@RealApp && @AppStore"

# Run specific banking app tests
mvn test -Dcucumber.filter.tags="@RealApp && @Barclays"
mvn test -Dcucumber.filter.tags="@RealApp && @HSBC"

# Run Pulse app tests
mvn test -Dcucumber.filter.tags="@Pulse"
```

### 5. View Results
```bash
# Open Grafana Dashboard
open http://localhost:3000
# Username: admin, Password: pulse_uk_2026

# View HTML Reports
open target/cucumber-reports/payday-traffic-pretty.html
open target/cucumber-reports/mobile-payday-pretty.html
open target/cucumber-reports/real-app-payday-pretty.html

# View mobile banking app
open http://localhost:8081/mobile

# View real app testing guide
open REAL_APP_TESTING_GUIDE.md

# View Play Store publishing guide
open PLAY_STORE_PUBLISHING_GUIDE.md

# View App Store publishing guide
open APP_STORE_PUBLISHING_GUIDE.md
```

## Key Features

### Payday Friday Payment Testing
- **Peak Hour Simulation**: 9:00 AM salary payment traffic
- **High Volume Processing**: Multiple concurrent payments
- **Response Time Validation**: Ensures payments complete within SLA
- **Success Rate Monitoring**: Tracks payment completion rates

### Realistic Traffic Simulation
- **Server Load Delays**: 1.5-3.5 second response delays
- **Peak Hour Stress**: Extended delays during 9:00 AM period
- **Failure Simulation**: 1-5% failure rate during peak load
- **Payment Processing**: Authorization and balance check delays

### Real-time Metrics
- **Response Time Tracking**: Monitor payment processing times in real-time
- **Success Rate Monitoring**: Track payment completion rates during stress
- **System Load Metrics**: Visualize concurrent user load and performance
- **Traffic Simulation**: Record simulated network conditions and delays

### Mobile Application Testing
- **Android & iOS Support**: Test mobile banking applications on both platforms
- **Touch Interaction Testing**: Simulate real mobile touch interactions and delays
- **Mobile Network Simulation**: 3G, 4G, WiFi, and poor signal conditions
- **App Lifecycle Testing**: Background/foreground transitions and session management
- **Biometric Authentication**: Touch ID and fingerprint simulation
- **Battery Saving Mode**: Test app performance under reduced power conditions

### Real App Testing (Play Store & App Store)
- **Real Device Simulation**: Samsung Galaxy S23, Google Pixel 7, iPhone 14, iPad Pro
- **Actual Banking Apps**: Barclays, HSBC, NatWest, Monzo, Starling Bank
- **Real Retail Apps**: Amazon, Tesco, Sainsbury's, Asda, Morrisons
- **Play Store Testing**: Android app package simulation and API calls
- **App Store Testing**: iOS app simulation and performance metrics
- **Real Network Conditions**: 5G, 4G, 3G, WiFi with authentic delays
- **Device-Specific Testing**: Platform-optimized user agents and viewports



## Project Architecture

```
pulse-uk-resilience/
├── src/main/java/uk/pulse/
│   ├── factory/                    # SimpleBrowserFactory.java, MobileBrowserFactory.java, RealAppBrowserFactory.java
│   ├── interceptors/               # PaydayTrafficSimulator.java, MobileTrafficSimulator.java, RealAppTrafficSimulator.java
│   └── observability/              # MetricsClient.java
├── src/test/java/uk/pulse/
│   ├── SimpleBaseTest.java         # Base test class
│   ├── runners/                    # PaydayTrafficRunner.java, MobilePaydayRunner.java, RealAppPaydayRunner.java, PulseAppRunner.java
│   └── steps/                      # PaydayTrafficSteps.java, MobilePaydaySteps.java, RealAppPaydaySteps.java, PulseAppSteps.java
├── src/test/resources/features/
│   ├── Payday_Friday_Traffic.feature
│   ├── Mobile_Payday_Traffic.feature
│   ├── Real_App_Payday_Traffic.feature
│   └── Pulse_App_Traffic.feature
├── grafana/                         # Grafana dashboards and provisioning
├── mock-apps/
│   ├── banking/                    # Mock web and mobile banking apps
│   └── pulse/                      # Pulse traffic simulator app
├── docker-compose.yml              # InfluxDB, Grafana, and mock applications
├── pom.xml                         # Maven configuration
├── README.md                       # This documentation
├── REAL_APP_TESTING_GUIDE.md      # Comprehensive real app testing guide
├── PLAY_STORE_PUBLISHING_GUIDE.md  # Play Store publishing guide
└── APP_STORE_PUBLISHING_GUIDE.md   # App Store publishing guide
```

## Configuration

### Environment Variables
```bash
# Test Configuration (optional)
export TEST_BASE_URL="http://localhost:8080"
```

### Maven Profiles
- `dev`: Development testing
- `payday`: Payday Friday specific scenarios

## Advanced Usage

### Custom Traffic Simulation
```java
// Apply Payday Friday traffic conditions
PaydayTrafficSimulator.applyPaydayFridayTraffic(page);

// Apply extreme stress conditions
PaydayTrafficSimulator.applyExtremePaydayStress(page);
```

### Browser Context Creation
```java
// Create mobile context for UK testing
BrowserContext context = SimpleBrowserFactory.createMobileContext(browser);

// Create desktop context
BrowserContext context = SimpleBrowserFactory.createDesktopContext(browser);
```

### Parallel Test Execution
```bash
# Run tests in parallel for faster execution
mvn test -Dcucumber.filter.tags="@PaydayFriday" -Dparallel=methods -DthreadCount=4
```

## Grafana Dashboard

The framework includes pre-configured Grafana dashboards for real-time monitoring:

### Available Dashboards
- **Payment Transactions**: Real-time payment processing metrics
- **Response Times**: Track payment authorization and processing times
- **Success Rates**: Monitor payment completion rates during stress testing
- **System Load**: Visualize concurrent user load and system performance
- **Traffic Simulation**: Monitor simulated network conditions and delays

### Accessing Grafana
```bash
# Grafana is available at http://localhost:3000
Username: admin
Password: pulse_uk_2026
```

### Key Metrics
- **payment_transactions**: Payment processing times and success rates
- **response_times**: Response time distribution by operation type
- **success_rates**: Overall success rates for different scenarios
- **system_load**: Concurrent user load and stress levels
- **traffic_simulation**: Simulated network delays and failure rates

## Performance Benchmarks

| Scenario | Users | Response Time | Success Rate |
|----------|-------|---------------|--------------|
| Normal Load | 1,000 | <2s | 99% |
| Payday Friday | 10,000 | <5s | 95% |
| Extreme Stress | 5,000 | <4s | 90% |

## Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Add your resilience scenarios to `src/test/resources/features/`
4. Implement step definitions in `src/test/java/uk/pulse/steps/`
5. Submit Pull Request

## Support

- **Issues**: [GitHub Issues](https://github.com/your-user/pulse-uk-resilience/issues)
- **Discussions**: [GitHub Discussions](https://github.com/your-user/pulse-uk-resilience/discussions)
- **Documentation**: [Wiki](https://github.com/your-user/pulse-uk-resilience/wiki)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Next Steps

- [ ] Add mobile app testing capabilities
- [ ] Implement API-only testing mode  
- [ ] Add more UK-specific network profiles
- [ ] Create performance regression alerts
- [ ] Integrate with UK banking APIs for realistic testing

---

Built with dedication for the UK FinTech & Retail community

Transforming "Payday Friday" from a risk into a competitive advantage
