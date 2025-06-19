package model;

import java.util.Date;
import java.util.Map;

public class report {
    private String reportId;
    private String reportName;
    private Date generatedDate;
    private Date startDate;
    private Date endDate;
    private Map<String, Object> metrics; // Key-value pairs of report data
    private Map<String, Integer> shipmentVolumes;
    private Map<String, Double> performanceMetrics;

    // Constructor
    public report(String reportName, Date startDate, Date endDate) {
        this.reportName = reportName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.generatedDate = new Date();
        this.reportId = "RPT-" + System.currentTimeMillis();
    }

    // Getters and Setters
    public String getReportId() { return reportId; }

    public String getReportName() { return reportName; }
    public void setReportName(String reportName) { this.reportName = reportName; }

    public Date getGeneratedDate() { return generatedDate; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public Map<String, Object> getMetrics() { return metrics; }
    public void setMetrics(Map<String, Object> metrics) { this.metrics = metrics; }

    public Map<String, Integer> getShipmentVolumes() { return shipmentVolumes; }
    public void setShipmentVolumes(Map<String, Integer> shipmentVolumes) {
        this.shipmentVolumes = shipmentVolumes;
    }

    public Map<String, Double> getPerformanceMetrics() { return performanceMetrics; }
    public void setPerformanceMetrics(Map<String, Double> performanceMetrics) {
        this.performanceMetrics = performanceMetrics;
    }

    // Helper methods
    public void addMetric(String key, Object value) {
        this.metrics.put(key, value);
    }

    public String toSummaryString() {
        return String.format("Report %s (%s to %s) - Generated %s",
                reportName,
                startDate.toString(),
                endDate.toString(),
                generatedDate.toString());
    }
}