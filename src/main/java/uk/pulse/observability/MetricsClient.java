package uk.pulse.observability;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

/**
 * Pulse-UK Metrics Client for InfluxDB/Grafana Observability
 * Records Payday Friday traffic testing metrics for real-time monitoring
 */
public class MetricsClient {
    private static final Logger logger = LoggerFactory.getLogger(MetricsClient.class);
    
    private final InfluxDBClient influxDBClient;
    private final WriteApi writeApi;
    private final String bucket;
    private final String org;
    
    public MetricsClient(String url, String token, String org, String bucket) {
        this.org = org;
        this.bucket = bucket;
        
        logger.info("Initializing InfluxDB client for {}/{}", org, bucket);
        
        try {
            this.influxDBClient = InfluxDBClientFactory.create(url, token.toCharArray(), org, bucket);
            this.writeApi = influxDBClient.makeWriteApi();
            
            // Test connection
            influxDBClient.health();
            logger.info("Successfully connected to InfluxDB");
            
        } catch (Exception e) {
            logger.error("Failed to connect to InfluxDB: {}", e.getMessage());
            throw new RuntimeException("InfluxDB connection failed", e);
        }
    }
    
    /**
     * Record payment transaction metrics
     */
    public void recordPaymentTransaction(String transactionType, long responseTimeMs, boolean success, String amount) {
        try {
            Point point = Point.measurement("payment_transactions")
                .addTag("type", transactionType)
                .addTag("status", success ? "success" : "failure")
                .addTag("environment", "pulse-uk-test")
                .addTag("amount_range", getAmountRange(amount))
                .addField("response_time_ms", responseTimeMs)
                .addField("success_rate", success ? 1.0 : 0.0)
                .addField("amount", amount.replace("£", ""))
                .time(Instant.now(), WritePrecision.MS);
            
            writeApi.writePoint(bucket, org, point);
            logger.debug("Recorded payment transaction: {} in {}ms (success: {})", 
                transactionType, responseTimeMs, success);
            
        } catch (Exception e) {
            logger.error("Failed to record payment transaction metrics: {}", e.getMessage());
        }
    }
    
    /**
     * Record system load metrics
     */
    public void recordSystemLoad(int concurrentUsers, String loadType) {
        try {
            Point point = Point.measurement("system_load")
                .addTag("load_type", loadType)
                .addTag("environment", "pulse-uk-test")
                .addField("concurrent_users", concurrentUsers)
                .addField("load_factor", calculateLoadFactor(concurrentUsers))
                .time(Instant.now(), WritePrecision.MS);
            
            writeApi.writePoint(bucket, org, point);
            logger.debug("Recorded system load: {} users, type: {}", concurrentUsers, loadType);
            
        } catch (Exception e) {
            logger.error("Failed to record system load metrics: {}", e.getMessage());
        }
    }
    
    /**
     * Record success rate metrics
     */
    public void recordSuccessRate(String scenario, double successRate, int totalTransactions) {
        try {
            Point point = Point.measurement("success_rates")
                .addTag("scenario", scenario)
                .addTag("environment", "pulse-uk-test")
                .addField("success_rate", successRate)
                .addField("total_transactions", totalTransactions)
                .addField("failed_transactions", totalTransactions - (int)(totalTransactions * successRate))
                .time(Instant.now(), WritePrecision.MS);
            
            writeApi.writePoint(bucket, org, point);
            logger.info("Recorded success rate for {}: {:.2f}% ({}/{} transactions)", 
                scenario, successRate * 100, (int)(totalTransactions * successRate), totalTransactions);
            
        } catch (Exception e) {
            logger.error("Failed to record success rate metrics: {}", e.getMessage());
        }
    }
    
    /**
     * Record response time distribution
     */
    public void recordResponseTimeDistribution(String operation, long responseTimeMs) {
        try {
            Point point = Point.measurement("response_times")
                .addTag("operation", operation)
                .addTag("environment", "pulse-uk-test")
                .addTag("time_range", getTimeRange(responseTimeMs))
                .addField("response_time_ms", responseTimeMs)
                .time(Instant.now(), WritePrecision.MS);
            
            writeApi.writePoint(bucket, org, point);
            logger.debug("Recorded response time: {}ms for operation: {}", responseTimeMs, operation);
            
        } catch (Exception e) {
            logger.error("Failed to record response time metrics: {}", e.getMessage());
        }
    }
    
    /**
     * Record traffic simulation metrics
     */
    public void recordTrafficSimulation(String simulationType, long avgDelayMs, double failureRate) {
        try {
            Point point = Point.measurement("traffic_simulation")
                .addTag("simulation_type", simulationType)
                .addTag("environment", "pulse-uk-test")
                .addField("average_delay_ms", avgDelayMs)
                .addField("failure_rate", failureRate)
                .addField("stress_level", getStressLevel(simulationType))
                .time(Instant.now(), WritePrecision.MS);
            
            writeApi.writePoint(bucket, org, point);
            logger.info("Recorded traffic simulation: {} ({}ms delay, {}% failure)", 
                simulationType, avgDelayMs, failureRate * 100);
            
        } catch (Exception e) {
            logger.error("Failed to record traffic simulation metrics: {}", e.getMessage());
        }
    }
    
    /**
     * Record test execution metrics
     */
    public void recordTestExecution(String testName, boolean passed, long durationMs) {
        try {
            Point point = Point.measurement("test_executions")
                .addTag("test_name", testName)
                .addTag("status", passed ? "passed" : "failed")
                .addTag("environment", "pulse-uk-test")
                .addField("duration_ms", durationMs)
                .addField("passed", passed ? 1 : 0)
                .time(Instant.now(), WritePrecision.MS);
            
            writeApi.writePoint(bucket, org, point);
            logger.info("Recorded test execution: {} ({}) in {}ms", testName, passed ? "PASSED" : "FAILED", durationMs);
            
        } catch (Exception e) {
            logger.error("Failed to record test execution metrics: {}", e.getMessage());
        }
    }
    
    // Helper methods
    private String getAmountRange(String amount) {
        try {
            double amt = Double.parseDouble(amount.replace("£", "").replace(",", ""));
            if (amt < 500) return "0-500";
            if (amt < 1000) return "500-1000";
            if (amt < 2000) return "1000-2000";
            return "2000+";
        } catch (Exception e) {
            return "unknown";
        }
    }
    
    private double calculateLoadFactor(int users) {
        // Simple load factor calculation (0.0 to 1.0)
        return Math.min(1.0, users / 10000.0);
    }
    
    private String getTimeRange(long responseTimeMs) {
        if (responseTimeMs < 1000) return "fast";
        if (responseTimeMs < 3000) return "normal";
        if (responseTimeMs < 5000) return "slow";
        return "very_slow";
    }
    
    private String getStressLevel(String simulationType) {
        switch (simulationType.toLowerCase()) {
            case "payday_friday": return "high";
            case "extreme_stress": return "very_high";
            case "normal": return "low";
            default: return "medium";
        }
    }
    
    /**
     * Close the metrics client
     */
    public void close() {
        try {
            if (writeApi != null) {
                writeApi.close();
            }
            if (influxDBClient != null) {
                influxDBClient.close();
            }
            logger.info("Metrics client closed successfully");
            
        } catch (Exception e) {
            logger.error("Error closing metrics client: {}", e.getMessage());
        }
    }
}
