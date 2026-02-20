Feature: Payday Friday Payment Traffic Stress Testing
  As a UK Retail Bank,
  I want to ensure my payment application remains stable during Friday 9:00 AM salary payment peak traffic,
  So that all employee salary payments are processed successfully without delays or failures.

  Background:
    Given the payment application is available and ready for testing

  @PaydayFriday @PaymentTraffic @Critical
  Scenario: High Volume Payment Processing During Payday Friday
    Given it is Friday 9:00 AM during payday period
    And the system is experiencing 10,000 concurrent users
    When users attempt to make salary payments
    Then all payments should be processed within 5 seconds
    And the system should maintain 99% success rate
    And response times should be logged for analysis

  @PaydayFriday @PeakLoad @Regression
  Scenario: Peak Load Transaction Authorization
    Given the payment system is under maximum load
    When a user authorizes a payment of £1,500
    Then the authorization should complete within 3 seconds
    And the transaction should be recorded successfully

  @PaydayFriday @StressTest @Performance
  Scenario: Multiple Concurrent Salary Payments Under Stress
    Given 5,000 users are simultaneously making salary payments
    When each user processes a payment between £500-£2,000
    Then at least 95% of payments should complete successfully
    And average processing time should not exceed 4 seconds
