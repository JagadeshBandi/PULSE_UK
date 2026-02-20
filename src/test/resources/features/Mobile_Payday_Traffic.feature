Feature: Mobile Payday Friday Traffic Testing
  As a UK Mobile Banking User,
  I want to ensure my mobile banking app works reliably during Friday 9:00 AM salary payments,
  So that I can make payments on my mobile device without issues.

  Background:
    Given the mobile banking app is installed and ready for testing

  @Mobile @PaydayFriday @Android
  Scenario: Android Mobile Payment During Payday Friday
    Given the user is using an Android device
    And it is Friday 9:00 AM during payday period
    And the mobile network is 4G with moderate congestion
    When the user opens the mobile banking app
    Then the app should load within 3 seconds
    And the user can make a payment of £1,200 within 5 seconds
    And the payment should be completed successfully

  @Mobile @PaydayFriday @iOS
  Scenario: iOS Mobile Payment During Payday Friday
    Given the user is using an iOS device
    And it is Friday 9:00 AM during payday period
    And the mobile network is 4G with moderate congestion
    When the user opens the mobile banking app
    Then the app should load within 3 seconds
    And the user can make a payment of £1,200 within 5 seconds
    And the payment should be completed successfully

  @Mobile @NetworkStress @Android
  Scenario: Android App with Poor Mobile Network
    Given the user is using an Android device
    And the mobile network connection is poor 3G
    When the user attempts to make a payment of £800
    Then the payment should complete within 8 seconds
    And the app should handle network interruptions gracefully

  @Mobile @NetworkStress @iOS
  Scenario: iOS App with Poor Mobile Network
    Given the user is using an iOS device
    And the mobile network connection is poor 3G
    When the user attempts to make a payment of £800
    Then the payment should complete within 8 seconds
    And the app should handle network interruptions gracefully

  @Mobile @BatterySaving @Android
  Scenario: Android App in Battery Saving Mode
    Given the user is using an Android device
    And the device is in battery saving mode
    When the user makes a payment of £500
    Then the payment should complete within 10 seconds
    And the app should remain responsive

  @Mobile @BatterySaving @iOS
  Scenario: iOS App in Battery Saving Mode
    Given the user is using an iOS device
    And the device is in battery saving mode
    When the user makes a payment of £500
    Then the payment should complete within 10 seconds
    And the app should remain responsive

  @Mobile @MultiUser @StressTest
  Scenario: Multiple Mobile Users During Payday Friday
    Given 1,000 mobile users are simultaneously using the banking app
    And it is Friday 9:00 AM during payday period
    When each user makes a payment between £200-£1,500
    Then at least 92% of mobile payments should complete successfully
    And average mobile response time should not exceed 6 seconds

  @Mobile @TouchInteraction @Usability
  Scenario: Mobile Touch Payment Flow
    Given the user is using a mobile device
    And the mobile banking app is loaded
    When the user taps the payment button
    And the user enters the payment amount
    And the user confirms the payment with touch ID
    Then the touch interactions should respond within 500ms
    And the payment should be processed successfully

  @Mobile @AppLifecycle @Background
  Scenario: Mobile App Background/Foreground Testing
    Given the user is using a mobile banking app
    And the user starts a payment of £1,000
    When the app goes to background during payment
    And the app returns to foreground
    Then the payment should resume and complete successfully
    And the app should maintain session state
