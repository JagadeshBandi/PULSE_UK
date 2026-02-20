Feature: Real App Payday Friday Traffic Testing
  As a UK Mobile Banking User,
  I want to test real banking applications from Play Store and App Store during Friday 9:00 AM salary payments,
  So that I can ensure the actual mobile apps work reliably during peak traffic.

  Background:
    Given the real mobile banking app is installed and ready for testing

  @RealApp @PlayStore @Banking @Android
  Scenario: Real Android Banking App During Payday Friday
    Given the user is using a Samsung Galaxy S23
    And the user opens the Barclays Mobile Banking app from Play Store
    And it is Friday 9:00 AM during payday period
    And the mobile network is 4G with moderate congestion
    When the user logs into the banking app
    Then the app should load within 3 seconds
    And the user can check their balance within 2 seconds
    And the user can make a payment of £1,200 within 5 seconds
    And the payment should be completed successfully

  @RealApp @AppStore @Banking @iOS
  Scenario: Real iOS Banking App During Payday Friday
    Given the user is using an iPhone 14
    And the user opens the HSBC UK Mobile Banking app from App Store
    And it is Friday 9:00 AM during payday period
    And the mobile network is 4G with moderate congestion
    When the user logs into the banking app
    Then the app should load within 3 seconds
    And the user can check their balance within 2 seconds
    And the user can make a payment of £1,200 within 5 seconds
    And the payment should be completed successfully

  @RealApp @PlayStore @Banking @Pixel
  Scenario: Real Banking App on Google Pixel During Payday Friday
    Given the user is using a Google Pixel 7
    And the user opens the NatWest Mobile Banking app from Play Store
    And it is Friday 9:00 AM during payday period
    And the mobile network is 5G with excellent connectivity
    When the user logs into the banking app
    Then the app should load within 2 seconds
    And the user can check their balance within 1 second
    And the user can make a payment of £2,000 within 3 seconds
    And the payment should be completed successfully

  @RealApp @AppStore @Banking @iPhonePro
  Scenario: Real Banking App on iPhone Pro During Payday Friday
    Given the user is using an iPhone 14 Pro
    And the user opens the Lloyds Bank Mobile Banking app from App Store
    And it is Friday 9:00 AM during payday period
    And the mobile network is 5G with excellent connectivity
    When the user logs into the banking app
    Then the app should load within 2 seconds
    And the user can check their balance within 1 second
    And the user can make a payment of £2,000 within 3 seconds
    And the payment should be completed successfully

  @RealApp @PlayStore @Banking @PoorNetwork
  Scenario: Real Android Banking App with Poor Network
    Given the user is using a OnePlus 11
    And the user opens the Monzo Mobile Banking app from Play Store
    And the mobile network connection is poor 3G
    And it is Friday 9:00 AM during payday period
    When the user attempts to make a payment of £800
    Then the payment should complete within 8 seconds
    And the app should handle network interruptions gracefully
    And the user should see appropriate error messages

  @RealApp @AppStore @Banking @PoorNetwork
  Scenario: Real iOS Banking App with Poor Network
    Given the user is using an iPad Pro
    And the user opens the Starling Bank Mobile Banking app from App Store
    And the mobile network connection is poor 3G
    And it is Friday 9:00 AM during payday period
    When the user attempts to make a payment of £800
    Then the payment should complete within 8 seconds
    And the app should handle network interruptions gracefully
    And the user should see appropriate error messages

  @RealApp @PlayStore @Retail @Amazon
  Scenario: Real Amazon App During Payday Friday Shopping
    Given the user is using a Samsung Galaxy S23
    And the user opens the Amazon Shopping app from Play Store
    And it is Friday 9:00 AM during payday period
    And the mobile network is 4G with moderate congestion
    When the user searches for products and makes a purchase of £500
    Then the app should load products within 3 seconds
    And the checkout should complete within 5 seconds
    And the purchase should be completed successfully

  @RealApp @AppStore @Retail @Tesco
  Scenario: Real Tesco App During Payday Friday Shopping
    Given the user is using an iPhone 14
    And the user opens the Tesco Grocery app from App Store
    And it is Friday 9:00 AM during payday period
    And the mobile network is 4G with moderate congestion
    When the user adds items to basket and checks out for £300
    Then the app should load products within 3 seconds
    And the checkout should complete within 5 seconds
    And the purchase should be completed successfully

  @RealApp @MultiApp @StressTest
  Scenario: Multiple Real Apps During Payday Friday
    Given the user is using a high-end mobile device
    And it is Friday 9:00 AM during payday period
    When the user switches between banking and retail apps
    And makes payments in Barclays app (£1,500)
    And makes purchases in Amazon app (£800)
    And makes purchases in Tesco app (£400)
    Then all transactions should complete successfully
    And app switching should work smoothly
    And no app should crash during the process

  @RealApp @Performance @AppStartup
  Scenario: Real App Performance During Peak Hours
    Given the user is using a mobile device
    And it is Friday 9:00 AM during payday period
    When the user opens multiple real apps
    And the user opens Barclays Mobile Banking app
    And the user opens Amazon Shopping app
    And the user opens Tesco Grocery app
    Then all apps should load within 3 seconds
    And app startup should not exceed memory limits
    And apps should remain responsive during use

  @RealApp @Network @5G
  Scenario: Real Apps on 5G Network During Payday Friday
    Given the user is using a 5G-capable mobile device
    And the mobile network is 5G with excellent connectivity
    And it is Friday 9:00 AM during payday period
    When the user uses banking and retail apps
    Then all API calls should complete within 1 second
    And image loading should be instant
    And app performance should be optimal

  @RealApp @Network @WiFi
  Scenario: Real Apps on WiFi During Payday Friday
    Given the user is using a mobile device connected to WiFi
    And the WiFi connection is stable and fast
    And it is Friday 9:00 AM during payday period
    When the user uses banking and retail apps
    Then all API calls should complete within 500ms
    And media loading should be smooth
    And app performance should be excellent
