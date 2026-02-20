Feature: Pulse App Payday Friday Traffic Simulator
  As a UK Financial Technology Developer,
  I want to demonstrate all Payday Friday traffic patterns in a single Pulse application,
  So that I can showcase the complete traffic simulation capabilities in one interface.

  Background:
    Given the Pulse traffic simulator app is loaded and ready

  @Pulse @PaydayFriday @Demo
  Scenario: Complete Payday Friday Traffic Demonstration
    Given it is Friday 9:00 AM during payday period
    When the user starts the Payday Friday simulation
    Then the app should display 10,000 concurrent users
    And the app should show real-time payment processing
    And the app should demonstrate all traffic patterns
    And the user should see live performance metrics

  @Pulse @Payment @Core
  Scenario: Single Payment Processing Demo
    Given the Pulse app is in normal mode
    When the user makes a payment
    Then the payment should process within 3 seconds
    And the balance should be updated
    And the response time should be logged
    And the success rate should remain at 100%

  @Pulse @Balance @Core
  Scenario: Balance Check Performance Demo
    Given the Pulse app is running
    When the user checks their balance
    Then the balance should retrieve within 2 seconds
    And the balance should display correctly
    And the response time should be logged

  @Pulse @Traffic @HighLoad
  Scenario: High Traffic Mode Demonstration
    Given the Pulse app is in normal mode
    When the user activates high traffic mode
    Then the app should simulate 5,000 concurrent users
    And 2,000 active payments should be shown
    And response times should increase appropriately
    And the system should handle the load gracefully

  @Pulse @Network @Failure
  Scenario: Network Failure Resilience Demo
    Given the Pulse app is processing payments
    When network failure is simulated
    Then the app should show error handling
    And the app should attempt recovery after 5 seconds
    And the system should restore normal operation
    And resilience metrics should be displayed

  @Pulse @Network @PoorConnection
  Scenario: Poor Connection Adaptation Demo
    Given the Pulse app is in normal operation
    When poor connection is simulated
    Then the app should adapt to slower response times
    And the app should maintain functionality
    And users should see connection status indicators
    And the app should recover when connection improves

  @Pulse @Performance @Optimal
  Scenario: Optimal Performance Demonstration
    Given the Pulse app is running
    When optimal performance mode is activated
    Then response times should be under 1 second
    And success rate should remain at 100%
    And all metrics should show "Excellent" status
    And the app should demonstrate peak performance

  @Pulse @Comprehensive @Demo
  Scenario: Comprehensive Traffic Pattern Demo
    Given the Pulse app is ready for demonstration
    When the user requests to show all traffic patterns
    Then the app should demonstrate optimal performance
    And the app should show balance checking
    And the app should process payments
    And the app should simulate high traffic
    And the app should handle poor connection
    And the app should recover from network failure
    And all scenarios should complete successfully

  @Pulse @RealTime @Metrics
  Scenario: Real-time Metrics Monitoring
    Given the Pulse app is running
    When the user observes the metrics panel
    Then concurrent users should update in real-time
    And active payments should be tracked
    And success rate should be calculated
    And average response time should be displayed
    And all metrics should update dynamically

  @Pulse @Background @Simulation
  Scenario: Background Traffic Simulation
    Given the Pulse app is running
    When background simulation is active
    Then payments should process automatically
    And metrics should update continuously
    And the app should remain responsive
    And all background operations should be logged
