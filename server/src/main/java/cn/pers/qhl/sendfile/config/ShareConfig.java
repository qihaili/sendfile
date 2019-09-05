package cn.pers.qhl.sendfile.config;

public class ShareConfig {

    private TtlOption[] ttlOptions;

    private String scanInterval;

    private String maxFileSize;

    public TtlOption[] getTtlOptions() {
        return ttlOptions;
    }

    public void setTtlOptions(TtlOption[] ttlOptions) {
        this.ttlOptions = ttlOptions;
    }

    public String getScanInterval() {
        return scanInterval;
    }

    public void setScanInterval(String scanInterval) {
        this.scanInterval = scanInterval;
    }

    public String getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(String maxFileSize) {
        this.maxFileSize = maxFileSize;
    }
}
