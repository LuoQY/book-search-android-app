package qingyang.cmu.edu;

import java.sql.Timestamp;

public class LogBean {
    private String deviceInfo;
    private String searchTerm;
    private Timestamp timestamp;
    private BookBean bookInfo;
    private long responseTime;

    public LogBean(String deviceInfo, String searchTerm, Timestamp timestamp, BookBean bookInfo, long responseTime) {
        this.deviceInfo = deviceInfo;
        this.searchTerm = searchTerm;
        this.timestamp = timestamp;
        this.bookInfo = bookInfo;
        this.responseTime = responseTime;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public BookBean getBookInfo() {
        return bookInfo;
    }

    public long getResponseTime() {
        return responseTime;
    }
}
